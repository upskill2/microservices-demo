package com.microservices.demo.elastic.query.security;

import com.microservices.demo.config.ElasticQueryServiceConfigData;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.oauth2.core.AbstractOAuth2Token;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
@Qualifier ("elastic-query-service-audience-validator")
public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    public AudienceValidator (ElasticQueryServiceConfigData elasticQueryServiceConfigData) {
        this.elasticQueryServiceConfigData = elasticQueryServiceConfigData;
    }

    private final ElasticQueryServiceConfigData elasticQueryServiceConfigData;


    @Override
    public OAuth2TokenValidatorResult validate (final Jwt jwt) {
        if (jwt.getAudience ().contains (elasticQueryServiceConfigData.getCustomAudience ())) {
            return OAuth2TokenValidatorResult.success ();
        } else {
            OAuth2Error auth2Error = new OAuth2Error ("invalid_token", "The required audience "
                    + elasticQueryServiceConfigData.getCustomAudience () + " is missing", null);
            return OAuth2TokenValidatorResult.failure (auth2Error);
        }
    }
}
