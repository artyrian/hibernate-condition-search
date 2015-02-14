package ru.artyrian.hibersearch.search.data;

import java.util.List;

/**
 * Created by PC on 29.06.14.
 */
public class PaginationResult<DM> {
    private List<DM> items;
    private Integer totalItemsCount;

    public PaginationResult(List<DM> items, Integer totalItemsCount) {
        this.items = items;
        this.totalItemsCount = totalItemsCount;
    }

    public List<DM> getItems() {
        return items;
    }

    public void setItems(List<DM> items) {
        this.items = items;
    }

    public Integer getTotalItemsCount() {
        return totalItemsCount;
    }

    public void setTotalItemsCount(Integer totalItemsCount) {
        this.totalItemsCount = totalItemsCount;
    }
}
