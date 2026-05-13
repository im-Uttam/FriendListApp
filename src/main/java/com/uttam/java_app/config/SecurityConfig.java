package com.uttam.java_app.config;

import com.uttam.java_app.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/", "/login", "/css/**", "/oauth2/**", "/login/oauth2/**").permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                .defaultSuccessUrl("/dashboard", true)
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(oauth2UserService())
                )
            )
            .logout(logout -> logout
                .logoutSuccessUrl("/")
            );

        return http.build();
    }

    @Bean
    public org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService oauth2UserService() {
        return new org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService() {
            @Override
            public org.springframework.security.oauth2.core.user.OAuth2User loadUser(
                org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest request) {
                var oauth2User = super.loadUser(request);
                userService.saveOrUpdateUser(oauth2User); // save to DB
                return oauth2User;
            }
        };
    }
}