package com.example.Du_An_TTS_Test.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.signerky}")
    private String signerky;

    @Autowired
    private JwtDecoder jwtDecoder;

    public final String[] PUBLIC_ENDPOINS = {
            "/User/add", "comment/add", "login"
    };
    public final String[] PUBLIC_ADMIN_GET = {
            "/User/getAll", "/User/matchAllUser"
    };
    public final String[] PUBLIC_ADMIN_DELETE = {
            "Admin/products/Elasticsearch/deleteProduct", "ElasticSearch/User/delete"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(request ->
                request
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_ADMIN_GET).hasAuthority("SCOP_ADMIN")
                        .requestMatchers(HttpMethod.POST, "Admin/products/add/Elasticsearch").hasAuthority("SCOP_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "dmin/products/Elasticsearch/updateProduct").hasAuthority("SCOP_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, PUBLIC_ADMIN_DELETE).hasAuthority("SCOP_ADMIN")
                        .anyRequest().authenticated()
        );
        http.oauth2ResourceServer(outh2 ->
                outh2.jwt(jwtConfigurer -> jwtConfigurer.decoder(jwtDecoder()))
        );
        http.csrf(AbstractHttpConfigurer::disable);

        return http.build();
    }


    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerky.getBytes(), "HS512");
        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();

    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();


        JwtGrantedAuthoritiesConverter grantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

        grantedAuthoritiesConverter.setAuthoritiesClaimName("scop");
        grantedAuthoritiesConverter.setAuthorityPrefix("SCOP_");

        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(grantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
