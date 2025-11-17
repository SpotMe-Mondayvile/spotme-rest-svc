package com.mts.spotmerest.configs;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Value("${spotme.env-props.origin}")
    private String originUrl;

    @Value("${spotme.env-props.origin}")
    private String baseOriginUrl;

    @Value("${spotme.env-props.origin}")
    private String uiURL;

    @Value("${spotme.env-props.gateway-url}")
    private String gateWayURL;

    @Value("${spotme.env-props.node-url}")
    private String nodeURL;

    private static final String[] AUTH_WHITE_LIST = {
            "/swagger-ui/index.html",
            "/api/v1/auth/**",
            "/v3/**",
            "/api/v3/api-docs/**",
            "/swagger-ui/**",
            "/api-docs/**",
            "/v2/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
            "/bus/v3/api-docs/**",
            "/api/v1/info",
            "/h2-console/**",
            "/actuator/info",
            "/actuator/health",
            "/actuator/**",
            "/swagger**",
            "/rest/**",
            "/rest-docs/**",
            "/swagger-ui-custom.html**",
            "/swagger-ui/**",
            "/openapi/**",
            "/swagger-ui.html",
            "/webjars/**",
            "/v3/api-docs/**" 
    };

//    @Bean
//    CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.setAllowCredentials(true);
//        corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8100"));
//        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
//                "Accept", "Authorization", "X-Requested-With",
//                "Access-Control-Request-Method", "Access-Control-Request-Headers","Access-Control-Allow-Headers"));
//        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept",
//                "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", "Access-Control-Allow-Credentials"));
//        corsConfiguration.setAllowedMethods(Arrays.asList("Access-Control-Allow-Methods",
//                "GET", "POST", "PUT", "DELETE", "OPTIONS"));
//        corsConfiguration.addAllowedMethod("POST");
//        corsConfiguration.addAllowedMethod("OPTIONS");
//        corsConfiguration.addAllowedOriginPattern("*");
//        corsConfiguration.addAllowedHeader("*");
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", corsConfiguration);
//
//
//        return source;
//    }
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addAllowedHeader("*");
        // corsConfiguration.setAllowedOrigins(Arrays.asList("http://localhost:8100","mkp**",baseOriginUrl,"http://localhost:8083",gateWayURL,"http://localhost:8081","http://localhost:5173","http://localhost:3000","http://localhost:8080","http://localhost:8087",originUrl,nodeURL,nodeURL+":[*]","https://rest.spot-me-app.com/",uiURL,"https://ui.spot-me-app.com/"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("*"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);


        return source;
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf
                        .disable())
                .authorizeHttpRequests(requests -> requests
                        .requestMatchers(AUTH_WHITE_LIST)
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                .sessionManagement(management -> management
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        http.cors((cors)->cors.configurationSource(corsConfigurationSource()));
        http.headers(headers -> headers.frameOptions().disable());
        return http.build();
    }
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring().antMatchers("/v2/api-docs/**", "/swagger-ui/**", "/swagger-resources/**", "/v2/api-docs/**");
//    }


}
