package ru.artyrian.hibersearch.search;


import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.joda.time.DateTime;
import ru.artyrian.hibersearch.search.data.PaginationResult;
import ru.artyrian.hibersearch.search.data.SearchContainer;
import ru.artyrian.hibersearch.search.data.SearchItem;
import ru.artyrian.hibersearch.search.exception.WrongSearchItemsException;
import ru.artyrian.hibersearch.search.util.DateConverter;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by artyrian on 11/23/2014.
 */
public class SearchPaginatorHelper<T> {
    private static final Log log = new Log4JLogger();

    public PaginationResult<T> search(final DetachedCriteria commonDetachedCriteria,
                                          Session session,
                                          final SearchContainer example,
                                          final Integer from,
                                          final Integer size)
    {
        DetachedCriteria detachedCriteria = initAliases(commonDetachedCriteria, example);
        detachedCriteria = buildDetachedCriteria(example, detachedCriteria);

        Criteria criteriaCount = detachedCriteria.getExecutableCriteria(session);
        Long totalResult = (Long) criteriaCount
                .setProjection(Projections.countDistinct("id"))
                .uniqueResult();
        totalResult = (totalResult != null) ? totalResult : 0;
        resetDetatchedCriteriaProjection(detachedCriteria);

        List<T> resultList;
        if (totalResult > 0) {
            Criteria criteriaObjects = detachedCriteria.getExecutableCriteria(session);
            criteriaObjects.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

            addOrder(criteriaObjects, example);

            criteriaObjects.setFirstResult(from);
            criteriaObjects.setMaxResults(size);

            resultList = criteriaObjects.list();
        } else {
            resultList = new ArrayList(0);
        }

        return new PaginationResult<T>(resultList, totalResult.intValue());
    }

    // set item.aliasPath to all usable searchItems.
    private DetachedCriteria initAliases(DetachedCriteria detachedCriteria, SearchContainer example) {
        List<String> aliasList = new ArrayList<>();
        for (Object objectItem : example.getItemMap().values()) {
            SearchItem item = (SearchItem) objectItem;
            if (item.getValue() != null || item.getOrder() > 0) {
                detachedCriteria = connectWithAlias(detachedCriteria, item, aliasList);
            }
        }

        return detachedCriteria;
    }

    private void addOrder(Criteria criteria, SearchContainer container) {
        List<SearchItem> list = new ArrayList(container.getItemMap().values());
        Comparator<SearchItem> comparator = new Comparator<SearchItem>() {
            public int compare(SearchItem searchItem1, SearchItem searchItem2) {
                return searchItem1.getOrder() - searchItem2.getOrder();
            }
        };

        Collections.sort(list, comparator);

        for (SearchItem searchItem : list) {
            if (searchItem.getOrder() > 0) {
                if (searchItem.isDesc()) {
                    criteria.addOrder(Order.desc(searchItem.aliasPath));
                } else {
                    criteria.addOrder(Order.asc(searchItem.aliasPath));
                }
            }
        }
    }

    private static Object cast(Object value, String type) throws ClassCastException {
        try {
            switch (type) {
                case "integer":
                    value = Integer.parseInt(value.toString());
                    break;
                case "boolean":
                case "string":
                    // it's ok
                    break;
                case "float":
                    value = Float.parseFloat(value.toString());
                    break;
                case "date":
                    value = DateConverter.convertToDateTime(value.toString());
                default :
                    log.error("Unknown value type. Still unparsed. Type: " + type);
            }
        } catch (NumberFormatException e) {
            log.error("Cannot parse " + value + " and convert to " + type);
            throw new ClassCastException("Cannot cast " + value + " to " + type);
        } catch (ParseException e) {
            log.error("Cannot parse " + value + " and convert to " + type);
            throw new ClassCastException("Cannot parse " + value + " to " + type);
        }

        return value;
    }

    private static DetachedCriteria buildDetachedCriteria(SearchContainer example, DetachedCriteria detachedCriteria) {
        if (!example.getItemMap().isEmpty()) {
            //List<String> aliasList = new ArrayList<>();

            for (Object objectItem : example.getItemMap().values()) {
                SearchItem item = (SearchItem) objectItem;

                if (item.getValue() != null) {
                    //detachedCriteria = connectWithAlias(detachedCriteria, item, aliasList);
                    String path = item.aliasPath;

                    Object value = cast(item.getValue(), item.getType());

                    Object valueTo = null;

                    switch (item.getOperation()) {
                        case "lk":
                            detachedCriteria.add(Restrictions.like(path, "%".concat((String) value).concat("%")));
                            break;
                        case "eq":
                            detachedCriteria.add(Restrictions.eq(path, value));
                            break;
                        case "bw":
                            if (item.getType().equals("date")) {
                                valueTo = ((DateTime) value).plusDays(1);
                            }
                            detachedCriteria.add(Restrictions.between(path, value, valueTo));
                            break;
                        case "isnull" :
                            Boolean isActive = (Boolean) value;
                            if (isActive != null && isActive) {
                                detachedCriteria.add(Restrictions.isNull(path));
                            } else if (isActive != null && !isActive) {
                                detachedCriteria.add(Restrictions.isNotNull(path));
                            }

                            break;
                        default:
                            log.error("This function not realized yet. Operation:" + item.getOperation() + ", field: " + path + ".");
                    }
                }
            }
        }

        return detachedCriteria;
    }

    private static void resetDetatchedCriteriaProjection(DetachedCriteria criteria) {
        criteria.setProjection(null);
    }

    private static DetachedCriteria connectWithAlias(DetachedCriteria detachedCriteria, SearchItem item, List<String> aliasList) {
        String path = item.getPath();
        if (path == null) {
            throw new WrongSearchItemsException("Path item not setted. Cannot find " + item.getName() + " with null alias");
        }

        if (path.contains(".")) {
            String[] splitPath = path.split("\\.");
            String startAssotiativePath = splitPath[0];
            String startAlias = splitPath[0];
            if (!aliasList.contains(startAlias)) {
                detachedCriteria.createAlias(startAssotiativePath, startAlias, JoinType.LEFT_OUTER_JOIN);
                aliasList.add(startAlias);
            }

            for (int i = 1; i < splitPath.length - 1; ++i) {
                startAssotiativePath = startAssotiativePath + "." + splitPath[i];
                startAlias = startAlias + splitPath[i].substring(0, 1).toUpperCase() + splitPath[i].substring(1);
                if (!aliasList.contains(startAlias)) {
                    detachedCriteria.createAlias(startAssotiativePath, startAlias, JoinType.LEFT_OUTER_JOIN);
                    aliasList.add(startAlias);
                }
            }

            item.aliasPath = startAlias + "." + splitPath[splitPath.length - 1];
        } else {
            item.aliasPath = item.getPath();
        }

        return detachedCriteria;
    }
}
