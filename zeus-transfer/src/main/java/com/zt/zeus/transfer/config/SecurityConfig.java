package com.zt.zeus.transfer.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Created with IntelliJ IDEA.
 *
 * @author zhang
 * date: 2021/8/17 9:52
 * description:
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.NEVER);
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/actuator/**").anonymous()
                .antMatchers("/api/pull/**").permitAll()
                .anyRequest().authenticated().and()
                .formLogin()
                .successForwardUrl("/swagger-ui/index.html")
                .defaultSuccessUrl("/swagger-ui/index.html").and()
                .httpBasic();

    }
}
