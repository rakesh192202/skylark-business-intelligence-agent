package com.skylark.business_agent.model;

import java.util.HashMap;
import java.util.Map;

public class Deal {

    private String id;
    private String name;

    private Map<String, String> fields = new HashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}