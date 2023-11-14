package com.microservices.demo.elastic.query.config;


import com.microservices.demo.config.UserConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserConfig userConfig;

    @Value ("${security.paths-to-ignore}")
    private String[] pathsRoIgnore;

    public WebSecurityConfig (final UserConfig userConfig) {
        this.userConfig = userConfig;
    }

    @Override
    protected void configure (final AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication ()
                .withUser (userConfig.getUsername ())
                .password ("{noop}123")
                .roles (userConfig.getRole ());
    }

    @Override
    public void configure (final WebSecurity web) throws Exception {
        web
                .ignoring ()
                .antMatchers (pathsRoIgnore);
    }

    @Override
    protected void configure (final HttpSecurity http) throws Exception {
        // http.authorizeRequests ().antMatchers ("/**").permitAll ();
        http
                .httpBasic ()
                .and ()
                .authorizeRequests ()
                .antMatchers ("/**").hasRole ("USER")
                .and ()
                .csrf ().disable ();
    }

/*    @Bean
    protected PasswordEncoder passwordEncoder () {
        return new BCryptPasswordEncoder ();
    }*/
}
