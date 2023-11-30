package com.microservices.demo.elastic.query.security;

public enum PermissionType {

    READ ("READ"),
    WRITE ("WRITE"),
    MANAGE ("ADMIN");

    private String type;

    PermissionType (String type) {
        this.type = type;
    }

    public String getType () {
        return type;
    }


}
