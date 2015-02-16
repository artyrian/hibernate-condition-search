## hibernate-condition-search

This is a small util package for helping search by defined criteria.

Use SearchContainer for set conditions and search/sort by columns.
It's not hibernate fulltext searching, but really helpful util for searching by domain models with lazy init from frontend side.

How to use:
1. Create example of class SearchPaginatorHelper<T> where T is domain model class.
2. Call search() method with 5 params.
    a. commonDetachedCriteria - detached criteria created for current domain model class search (maybe with some conditions already set.
    b. session - current active session for execute queries to db.
    c. searchContainer - entity of SearchContainer class. it describes how to set below.
    d. pagination param: from. From which record in db start counting
    e. pagination param: size. How much records will be return.

For correctly searching you need init SearchContainer example of clas right.
There are 4 fields:
    1. page - current page of result
    2. countPerPage - count of records in one page. By default: 10.
    3. isPaginated - boolean flag for paginating or not result of seach() method.
    4. itemMap - key-value map of conditionds defined by 'path' and SearchItem examples.
        .. where 'path' is hibernate path for entities 'cause it's unique for searching values
            and SearchItem class is condition class for 1 condition searching.

SearchItem description:
    name - non required field. Need for some helpful reason on frontend (describe field, add name for searching value)
    path - correct path for searching condition Ex.: class User and path for specific role name is 'userRoles.name'.
    value - field for setting searching value. set up by frontend.
    type - type of value: integer, float, string, date
    operation - operation for searching: lk, eq, bw, isnull.
    isHidden - non required. flag for frontend which indicate to show or hide current search item on clientside.
    priority - priority of showing search item. If higher value then more important to show.
    order -  sorting order. If higher value then more important to order.
    desc - boolean flag to order desc if it is true

    aliasPath - stuff field for creating alias path on working search() method for deep search.
