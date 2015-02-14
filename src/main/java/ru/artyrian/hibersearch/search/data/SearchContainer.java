package ru.artyrian.hibersearch.search.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.impl.Log4JLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by PC on 29.05.14.
 */
public class SearchContainer {
    private static final Log log = new Log4JLogger();


    public static final Integer COUNT_PER_PAGE = 10;

    private Integer page = 1;
    private Integer countPerPage = COUNT_PER_PAGE;
    private boolean isPaginated = true;
    private Map<String, SearchItem> itemMap = new HashMap<String, SearchItem>();


    public SearchContainer() {
    }

    public SearchContainer(List<SearchItem> items) {
        for (SearchItem item : items) {
            itemMap.put(item.getPath(), item);
        }
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getCountPerPage() {
        return countPerPage;
    }

    public void setCountPerPage(Integer countPerPage) {
        this.countPerPage = countPerPage;
    }

    public boolean getIsPaginated() {
        return isPaginated;
    }

    public void setIsPaginated(boolean isPaginated) {
        this.isPaginated = isPaginated;
    }

    public SearchItem getItem(String itemName) {
        SearchItem item = itemMap.get(itemName);

        if (item == null) {
            log.error("Field " + itemName + " not found. Cannot execute search method");
            throw new RuntimeException("Cannot execute search method. Field " + itemName + " not ready for search.");
        }

        return item;
    }

    public void setItem(SearchItem insertItem) {
        Map<String, SearchItem> searchItemMap = getItemMap();
        searchItemMap.put(insertItem.getPath(), insertItem);
    }

    public Map getItemMap() {
        return itemMap;
    }

    public void setItemMap(Map<String, SearchItem> itemMap) {
        this.itemMap = itemMap;
    }

    public void putValue(String key, Object value) {
        SearchItem searchItem = this.getItem(key);
        searchItem.setValue(value);
        this.setItem(searchItem);
    }

    public void setItems(List<SearchItem> items) {
        for (SearchItem item : items) {
            itemMap.put(item.getPath(), item);
        }
    }
}
