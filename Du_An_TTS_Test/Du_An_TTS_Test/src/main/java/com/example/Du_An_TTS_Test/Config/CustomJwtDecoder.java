package com.example.Du_An_TTS_Test.Config;

import com.example.Du_An_TTS_Test.Dto.Repon.IntrospectRequet;
import com.example.Du_An_TTS_Test.Sevice.AuthenticationSevice;
import com.nimbusds.jose.JOSEException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {


    @Value("${jwt.signerky}")
    private String signerKey;
    @Autowired
    private AuthenticationSevice authenticationSevice;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            var respone = authenticationSevice.introspectRepon(IntrospectRequet.builder().token(token).build());
            if (respone.isValid()) throw new JwtException("token itvalid");
        } catch (ParseException | JOSEException e) {
            e.printStackTrace();
        }
        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(signerKey.getBytes(), "HS512");
            nimbusJwtDecoder = NimbusJwtDecoder.withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }


        return nimbusJwtDecoder.decode(token);
    }
}