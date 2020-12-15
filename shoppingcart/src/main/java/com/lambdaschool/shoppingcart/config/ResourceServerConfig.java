package com.lambdaschool.shoppingcart.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;

@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    private static String RESOURCE_ID = "resource_id";

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId(RESOURCE_ID).stateless(false);
    }

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/",
                        "/h2-console/**",
                        "swagger-resources/**",
                        "swagger-resource/**",
                        "swagger-ui.html",
                        "/v2/api-docs.html",
                        "/webjars/**",
                        "/createnewuser").permitAll()
                .antMatchers(HttpMethod.POST, "/users/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users/**").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("ADMIN")
                .antMatchers("/roles/**").hasAnyRole("ADMIN")
                .antMatchers("/products/**").hasAnyRole("ADMIN")
                .antMatchers("/carts/**").hasAnyRole("ADMIN")
                .antMatchers("/oauth/revoke-token",
                        "/logout",
                        "/users/myinfo").authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedHandler(new OAuth2AccessDeniedHandler());

        http.headers().frameOptions().disable();

        http.logout().disable();

        super.configure(http);
    }
}
