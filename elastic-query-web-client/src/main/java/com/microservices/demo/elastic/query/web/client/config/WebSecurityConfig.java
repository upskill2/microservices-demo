package com.microservices.demo.elastic.query.web.client.config;

import com.microservices.demo.config.UserConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@ConfigurationProperties
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserConfig userConfigData;

    public WebSecurityConfig (final UserConfig userConfigData) {
        this.userConfigData = userConfigData;
    }

    @Override
    protected void configure (final HttpSecurity http) throws Exception {
        http
                .httpBasic ()
                .and ()
                .authorizeRequests ()
                .antMatchers ("/actuator/**").permitAll ()
                .antMatchers ("/**").hasRole ("USER")
                .anyRequest ()
                .fullyAuthenticated ();
    }

    @Override
    protected void configure (final AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication ()
                .withUser (userConfigData.getUsername ())
                .password ("{noop}123")
                .roles (userConfigData.getRole ());
    }


/*    @Bean
    public PasswordEncoder passwordEncoder (){
        return new BCryptPasswordEncoder ();
    }*/
}
