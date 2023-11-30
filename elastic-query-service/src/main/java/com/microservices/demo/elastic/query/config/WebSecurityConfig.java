package com.microservices.demo.elastic.query.config;


import com.microservices.demo.config.UserConfig;
import com.microservices.demo.elastic.query.security.TwitterQueryUserDetailService;
import com.microservices.demo.elastic.query.security.TwitterQueryUserJwtConverter;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.*;

@Configuration
@EnableGlobalMethodSecurity (prePostEnabled = true)

public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private final TwitterQueryUserDetailService twitterQueryUserDetailService;
    private final OAuth2ResourceServerProperties oAuth2ResourceServerProperties;

    public WebSecurityConfig (TwitterQueryUserDetailService twitterQueryUserDetailService, OAuth2ResourceServerProperties oAuth2ResourceServerProperties) {
        this.twitterQueryUserDetailService = twitterQueryUserDetailService;
        this.oAuth2ResourceServerProperties = oAuth2ResourceServerProperties;
    }


    @Value ("${security.paths-to-ignore}")
    private String[] pathsRoIgnore;

    @Override
    protected void configure (final HttpSecurity http) throws Exception {
        http
                .sessionManagement ()
                .sessionCreationPolicy (SessionCreationPolicy.STATELESS)
                .and ()
                .csrf ()
                .disable ()
                .authorizeRequests ()
                .anyRequest ()
                .fullyAuthenticated ()
                .and ()
                .oauth2ResourceServer ()
                .jwt ()
                .jwtAuthenticationConverter (twitterQueryUserJwtConverter ());
    }

    @Bean
    JwtDecoder jwtDecoder (@Qualifier ("elastic-query-service-audience-validator") OAuth2TokenValidator<Jwt> audienceValidator) {
        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder) JwtDecoders.fromOidcIssuerLocation (
                oAuth2ResourceServerProperties.getJwt ().getIssuerUri ());
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer (
                oAuth2ResourceServerProperties.getJwt ().getIssuerUri ());
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<> (withIssuer, audienceValidator);
        jwtDecoder.setJwtValidator (withAudience);

        return jwtDecoder;
    }

        @Bean
        public Converter<Jwt, ? extends AbstractAuthenticationToken> twitterQueryUserJwtConverter() {
            return new TwitterQueryUserJwtConverter (twitterQueryUserDetailService);
        }

        @Override
        public void configure ( final WebSecurity web) throws Exception {
            web
                    .ignoring ()
                    .antMatchers (pathsRoIgnore);
        }

    }
