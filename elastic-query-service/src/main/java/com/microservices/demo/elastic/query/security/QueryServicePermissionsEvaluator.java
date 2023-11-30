package com.microservices.demo.elastic.query.security;

import com.microservices.demo.common.model.ElasticQueryServiceRequestModel;
import com.microservices.demo.common.model.ElasticQueryServiceResponseModel;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Component
public class QueryServicePermissionsEvaluator implements PermissionEvaluator {

    private static final String SUPER_USER_ROLE = "APP_SUPER_USER_ROLE";

    private final HttpServletRequest httpServletRequest;

    public QueryServicePermissionsEvaluator (HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    @Override
    public boolean hasPermission (final Authentication authentication, final Object targetDomain, final Object permission) {
        if (isSuperUser ()) {
            return true;
        }

        if (targetDomain instanceof ElasticQueryServiceRequestModel) {
            return preAuthorize (authentication, ((ElasticQueryServiceRequestModel) targetDomain).getId (), permission);
        } else if (targetDomain instanceof ResponseEntity || targetDomain == null) {
            if (targetDomain == null) {
                return true;
            }
            List<ElasticQueryServiceResponseModel> responseBody =
                    ((ResponseEntity<List<ElasticQueryServiceResponseModel>>) targetDomain).getBody ();
            Objects.requireNonNull (responseBody);
            return postAuthorize (authentication, responseBody, permission);
        }
        return false;
    }

    @Override
    public boolean hasPermission (final Authentication authentication,
                                  final Serializable targetId,
                                  final String targetType,
                                  final Object permission) {

        if (isSuperUser ()) {
            return true;
        }

        if (targetId == null) {
            return false;
        }

        return preAuthorize (authentication, targetId.toString (), permission);
    }

    private boolean preAuthorize (final Authentication authentication, final String id, final Object permission) {

        TwitterQueryUser twitterQueryUser = (TwitterQueryUser) authentication.getPrincipal ();
        PermissionType permissionType = twitterQueryUser.getPermissions ().get (id);
        return hasPermission ((String) permission, permissionType);
    }

    private boolean postAuthorize (final Authentication authentication,
                                   final List<ElasticQueryServiceResponseModel> responseBody,
                                   final Object permission) {

        TwitterQueryUser twitterQueryUser = (TwitterQueryUser) authentication.getPrincipal ();
        for (ElasticQueryServiceResponseModel requestModel : responseBody) {
            PermissionType permissionType = twitterQueryUser.getPermissions ().get (requestModel.getId ());
            if (!hasPermission ((String) permission, permissionType)) {
                return false;
            }
        }
        return true;
    }

    private boolean hasPermission (String requiredPermission, PermissionType userPermission) {
        return userPermission != null && userPermission.getType ().equals (requiredPermission);
    }

    private boolean isSuperUser () {
        return httpServletRequest.isUserInRole (SUPER_USER_ROLE);
    }

}
