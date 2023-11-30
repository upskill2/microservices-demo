package com.microservices.demo.elastic.query.security;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.microservices.demo.elastic.query.security.Constants.NA;

public class TwitterQueryUserJwtConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private static final String REALM_ACCESS_CLAIM = "realm_access";
    private static final String ROLES_CLAIM = "roles";
    private static final String SCOPE_CLAIM = "scope";
    private static final String USERNAME_CLAIM = "preferred_username";
    private static final String DEFAULT_ROLE_PREFIX = "ROLE_";
    private static final String DEFAULT_SCOPE_PREFIX = "SCOPE_";
    private static final String SCOPE_SEPARATOR = "";

    private final TwitterQueryUserDetailService twitterQueryUserDetailService;

    public TwitterQueryUserJwtConverter (TwitterQueryUserDetailService twitterQueryUserDetailService) {
        this.twitterQueryUserDetailService = twitterQueryUserDetailService;
    }

    @Override
    public AbstractAuthenticationToken convert (final Jwt jwt) {
        Collection<GrantedAuthority> authoritiesFromJwt =getAuthoritiesFromJwt (jwt);
        return Optional.ofNullable (twitterQueryUserDetailService.loadUserByUsername (jwt.getClaimAsString (USERNAME_CLAIM)))
                .map (userDetails ->{
                    ((TwitterQueryUser) (userDetails)).setAuthorities (authoritiesFromJwt);
                    return new UsernamePasswordAuthenticationToken (userDetails, NA, authoritiesFromJwt);
                })
                .orElseThrow (() -> new BadCredentialsException ("User details cannot be found"));
    }

    private Collection<GrantedAuthority> getAuthoritiesFromJwt (final Jwt jwt) {

       return getCombinedAuthorities (jwt).stream ()
                .map (SimpleGrantedAuthority::new)
                .collect (Collectors.toList ());
    }

    private Collection<String> getCombinedAuthorities (final Jwt jwt) {
        Collection<String> aouthorities = getRoles (jwt);
        aouthorities.addAll (getScopes (jwt));
        return aouthorities;
    }

    private Collection<String> getRoles(Jwt jwt) {
        Object roles = ((Map<String, Object>) jwt.getClaims().get(REALM_ACCESS_CLAIM)).get(ROLES_CLAIM);
        if (roles instanceof Collection) {
            return ((Collection<String>) roles).stream()
                    .map(authority -> DEFAULT_ROLE_PREFIX + authority.toUpperCase())
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    private Collection<String> getScopes (final Jwt jwt) {
        Object scopes = jwt.getClaims ().get (SCOPE_CLAIM);
        if (scopes instanceof Collection) {
            return ((Collection<String>) scopes).stream ()
                    .map (authorities -> DEFAULT_SCOPE_PREFIX + authorities.toUpperCase ())
                    .collect (Collectors.toList ());
        }
       return Collections.emptyList ();
    }

}
