package com.microservices.demo.elastic.query.security;

import com.microservices.demo.elastic.query.service.QueryUserService;
import com.microservices.demo.elastic.query.transformer.UserPermissionToUserDetailTransformer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TwitterQueryUserDetailService implements UserDetailsService {
  private final QueryUserService queryUserService;
  private final UserPermissionToUserDetailTransformer userPermissionToUserDetailTransformer;

    public TwitterQueryUserDetailService (QueryUserService queryUserService, UserPermissionToUserDetailTransformer userPermissionToUserDetailTransformer) {
        this.queryUserService = queryUserService;
        this.userPermissionToUserDetailTransformer = userPermissionToUserDetailTransformer;
    }

    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        return queryUserService
                .finaAllUserPermissionsByUsername (username)
                .map (userPermissionToUserDetailTransformer::getUserDetails)
                .orElseThrow (
                        () -> new UsernameNotFoundException ("User " + username + " not found"));
    }

}
