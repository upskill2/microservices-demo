package com.microservices.demo.elastic.query.dataaccess.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Entity
@Data
public class UserPermission {

    @Id
    @NotNull
    private UUID id;
    @NotNull
    private String username;
    @NotNull
    private String documentId;
    @NotNull
    private String permissionType;
}
