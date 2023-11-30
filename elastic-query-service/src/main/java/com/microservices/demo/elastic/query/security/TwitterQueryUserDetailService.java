package com.microservices.demo.elastic.query.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class TwitterQueryUserDetailService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException {
        return TwitterQueryUser.builder ()
                .username (username)
                .build ();
    }
}
