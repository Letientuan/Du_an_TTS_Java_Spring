package com.example.Du_An_TTS_Test.Sevice;

import com.example.Du_An_TTS_Test.Dto.Repon.IntrospectRequet;
import com.example.Du_An_TTS_Test.Dto.Repon.introspectRepon;
import com.example.Du_An_TTS_Test.Dto.Repon.logoutRequet;
import com.example.Du_An_TTS_Test.Dto.Request.AuthenticationRequest;
import com.example.Du_An_TTS_Test.Entity.InvalidatedToken;
import com.example.Du_An_TTS_Test.Entity.User;
import com.example.Du_An_TTS_Test.Repository.InvalidateTonkenRepo;

import com.example.Du_An_TTS_Test.Repository.UsersRepo;
import com.example.Du_An_TTS_Test.exception.AppException;
import com.example.Du_An_TTS_Test.exception.ErrorCode;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.converter.ClaimTypeConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationSevice {

    UsersRepo usersRepo;


    InvalidateTonkenRepo invalidateTonkenRepo;

    @NonFinal
    @Value("${jwt.signerky}")
    protected String KEYS;

    @NonFinal
    @Value("${jwt.valid-duration}")
    protected long VALID_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-duration}")
    protected long REFRESHABLE_DURATION;

    public introspectRepon introspectRepon(IntrospectRequet requet) throws JOSEException, ParseException {
        var token = requet.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (AppException e) {
            isValid = false;
        }
        return introspectRepon.builder()
                .valid(isValid)
                .build();

    }

    public void logOut(logoutRequet request) throws ParseException, JOSEException {

        var signedToken = verifyToken(request.getToken(), true);


        String jwtId = signedToken.getJWTClaimsSet().getJWTID();
        Date expiryTime = signedToken.getJWTClaimsSet().getExpirationTime();


        InvalidatedToken invalidatedToken = InvalidatedToken.builder()
                .id(jwtId)
                .expiryTime(expiryTime)
                .build();
        invalidateTonkenRepo.save(invalidatedToken);
    }


    private SignedJWT verifyToken(String token, boolean isRefresh) throws JOSEException, ParseException {
        JWSVerifier verifier = new MACVerifier(KEYS.getBytes());

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = (isRefresh)
                ? new Date(signedJWT
                .getJWTClaimsSet()
                .getIssueTime()
                .toInstant()
                .plus(REFRESHABLE_DURATION, ChronoUnit.SECONDS)
                .toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        var verified = signedJWT.verify(verifier);

        if (!(verified && expiryTime.after(new Date()))) throw new AppException(ErrorCode.UNAUTHENTICATED);

        if (invalidateTonkenRepo.existsById(signedJWT.getJWTClaimsSet().getJWTID()))
            throw new AppException(ErrorCode.UNAUTHENTICATED);

        return signedJWT;
    }


    public IntrospectRequet authenticated(AuthenticationRequest request) {

        String username = request.getUsername();
        var user = usersRepo.findByUsername(username)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authentication = passwordEncoder.matches(request.getPassword(), user.getPassword());
        if (!authentication)
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        var token = generateToken(user);
        return IntrospectRequet.builder()
                .token(token)
                .build();
    }

    private String generateToken(User user) {
        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(String.valueOf(user.getUsername()))
                .issuer("vissoft.com")
                .issueTime(new Date())
                .jwtID(UUID.randomUUID().toString())
                .expirationTime(new Date(
                        Instant.now().plus(VALID_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                .claim("scop", buildScope(user))
                .claim("id_user", user.getId())
                .build();
        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(KEYS.getBytes(StandardCharsets.UTF_8)));
        } catch (JOSEException e) {
            e.printStackTrace();
        }
        return jwsObject.serialize();

    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if (!CollectionUtils.isEmpty(user.getRoles()))
            user.getRoles().forEach(role -> {
                stringJoiner.add(role.getName());
//                if (!CollectionUtils.isEmpty(role.getPermissions()))
//                    role.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
            });

        return stringJoiner.toString();
    }

}



