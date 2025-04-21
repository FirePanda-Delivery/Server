package ru.diplom.fpd.configuration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
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
                .httpBasic(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(config ->
                        config.requestMatchers("/register/user", "/login/user").permitAll()
                                .anyRequest().permitAll())
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
//                .requestMatchers("/user/*").hasRole("user")
//                .requestMatchers(HttpMethod.POST, "/restaurant").hasRole("restaurant_admin")
//                .requestMatchers(HttpMethod.PUT, "/restaurant").hasRole("restaurant_admin")
//                .requestMatchers(HttpMethod.DELETE, "/restaurant").hasRole("restaurant_admin")
                .build();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
