package ru.artyrian.hibersearch.search.data;

/**
 * Created by PC on 03.07.14.
 */
public class SearchItem {
    private String name;
    private String path;
    private Object value;
    private String type;
    private String operation;
    private boolean isHidden = false;
    private int priority = 0;
    private int order = 0;
    private boolean desc = false;

    public String aliasPath;


    public SearchItem() {}

    public SearchItem(String name, String path, String type, String operation) {
        this.name = name;
        this.path = path;
        this.type = type;
        this.operation = operation;
    }

    public SearchItem(String name, String path, String type, String operation, boolean isHidden, int priority) {
        this(name, path, type, operation);
        this.isHidden = isHidden;
        this.priority = priority;
    }

    public SearchItem(String name,
                      String path,
                      String type,
                      String operation,
                      boolean isHidden,
                      int priority,
                      int order)
    {
        this(name, path, type, operation, isHidden, priority);
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public boolean isHidden() {
        return isHidden;
    }

    public void setHidden(boolean isHidden) {
        this.isHidden = isHidden;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public boolean isDesc() {
        return desc;
    }

    public void setDesc(boolean desc) {
        this.desc = desc;
    }
}

