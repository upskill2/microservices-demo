package com.microservices.demo.elastic.query.service.impl;

import com.microservices.demo.elastic.query.dataaccess.entity.UserPermission;
import com.microservices.demo.elastic.query.dataaccess.repository.UserPermissionRepository;
import com.microservices.demo.elastic.query.service.QueryUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class QueryUserServiceImpl implements QueryUserService {

    private final UserPermissionRepository userPermissionRepository;

    public QueryUserServiceImpl (UserPermissionRepository userPermissionRepository) {
        this.userPermissionRepository = userPermissionRepository;
    }

    @Override
    public Optional<List<UserPermission>> finaAllUserPermissionsByUsername (final String username) {
        log.info ("Querying user permissions for username: {}", username);
        return userPermissionRepository.findPermissionByUsername (username);
    }
}
