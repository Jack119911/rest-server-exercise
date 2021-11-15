package ninja.parkverbot.restserver.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @Order(1)
    public static class LoginWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/login/")
                    .authorizeRequests(authorize -> authorize.anyRequest().authenticated())
                    .csrf().disable() // Not for production
                    .httpBasic();
        }
    }

    @Configuration
    @Order(2)
    public static class ApiWebSecurityConfigurationAdapter extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .antMatcher("/user/**")
                    .authorizeRequests(authorize -> authorize.anyRequest().permitAll())
                    .csrf().disable(); // Not for Production
        }
    }

    @Configuration
    @Order
    public static class DenyAllWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        protected void configure(HttpSecurity http) throws Exception {
            http
                    .authorizeRequests().anyRequest()
                    .denyAll();
        }
    }



}
