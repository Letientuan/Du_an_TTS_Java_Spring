package com.example.Du_An_TTS_Test.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;


import javax.crypto.spec.SecretKeySpec;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    @Value("${jwt.signerky}")
    private String signerky;
    @Autowired
    CustomJwtDecoder customJwtDecoder;


    private final String[] PUBLIC_ENDPOINS = {
            "/User/add", "comment/add",
            "/auth/introspect", "/comment/**"
    };
    private final String[] PUBLIC_ADMIN_GET = {
            "/User/getAll", "/User/matchAllUser"
    };
    private final String[] PUBLIC_ADMIN_DELETE = {
            "Admin/products/Elasticsearch/deleteProduct",
            "ElasticSearch/User/delete"
    };
    private final String[] User = {
            "/Admin/products/ProductDetail/**",
            "/ElasticSearch/Product/matchAllProduct",
            "/auth/login", "/auth/logout"
    };


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity

                .authorizeHttpRequests(request -> request
                        .requestMatchers(User).permitAll()
                        .requestMatchers(HttpMethod.POST, PUBLIC_ENDPOINS).permitAll()
                        .requestMatchers(HttpMethod.GET, PUBLIC_ADMIN_GET).hasAuthority("SCOP_ADMIN")
                        .requestMatchers(HttpMethod.POST, "Admin/products/add/Elasticsearch").hasAuthority("SCOP_ADMIN")
                        .requestMatchers(HttpMethod.PUT, "admin/products/Elasticsearch/updateProduct/**").hasAuthority("SCOP_ADMIN")
                        .requestMatchers(HttpMethod.DELETE, PUBLIC_ADMIN_DELETE).hasAuthority("SCOP_ADMIN")
                        .anyRequest()
                        .authenticated())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                        .authenticationEntryPoint(new JwtAuthenticationEntryPoint()))
                .csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();
    }

    @Bean
    public JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(signerky.getBytes(), "HS512");
        return NimbusJwtDecoder.withSecretKey(secretKeySpec)
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
