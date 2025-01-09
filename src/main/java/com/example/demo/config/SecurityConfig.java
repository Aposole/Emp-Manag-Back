package com.example.demo.config;

import com.example.demo.component.JwtAuthenticationEntryPoint;
import com.example.demo.component.JwtRequestFilter;
import com.example.demo.service.AdminDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private AdminDetailsServiceImpl adminDetailsService;
    private JwtAuthenticationEntryPoint unauthorizedHandler;
    private PasswordEncoder passwordEncoder;

    public SecurityConfig(AdminDetailsServiceImpl adminDetailsService, JwtAuthenticationEntryPoint unauthorizedHandler, PasswordEncoder passwordEncoder) {
        this.adminDetailsService = adminDetailsService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public JwtRequestFilter jwtRequestFilter() {
        return new JwtRequestFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminDetailsService).passwordEncoder(passwordEncoder());
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(adminDetailsService)
                .passwordEncoder(passwordEncoder);

        return auth.build();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .requestMatchers("/api/auth/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler)
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.addFilterBefore(jwtRequestFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
