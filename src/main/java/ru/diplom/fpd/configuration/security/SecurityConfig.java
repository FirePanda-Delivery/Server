package ru.diplom.fpd.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.diplom.fpd.configuration.security.jwt.JwtFilter;

@Configuration
//@EnableWebSecurity
public class SecurityConfig {

    @Lazy
    private final JwtFilter jwtFilter;

    @Autowired
    public SecurityConfig(@Lazy JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    @Bean
    protected SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http
                .httpBasic().disable()
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                .requestMatchers("/user/*").hasRole("user")
                .requestMatchers("/register/user", "/login/user").permitAll()
                .requestMatchers(HttpMethod.POST, "/restaurant").hasRole("restaurant_admin")
                .requestMatchers(HttpMethod.PUT, "/restaurant").hasRole("restaurant_admin")
                .requestMatchers(HttpMethod.DELETE, "/restaurant").hasRole("restaurant_admin")
                .anyRequest().permitAll()
                .and()
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
