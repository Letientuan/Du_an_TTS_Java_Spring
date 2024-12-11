package com.example.Du_An_TTS_Test.Util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class JwtTokenUtil {
    @NonFinal
    @Value("${jwt.signerky}")
    protected String secretKey ;

    // Giải mã JWT và lấy thông tin người dùng từ token
    public Claims extractClaims(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();
    }

    // Lấy vai trò từ token (dự kiến token chứa vai trò dưới dạng danh sách trong claim "roles")
    public List<String> extractRoles(String token) {
        Claims claims = extractClaims(token);
        return (List<String>) claims.get("roles");
    }

    // Lấy thông tin quyền (permissions) từ token (dự kiến token chứa quyền dưới dạng danh sách trong claim "permissions")
    public List<String> extractPermissions(String token) {
        Claims claims = extractClaims(token);
        return (List<String>) claims.get("permissions");
    }
}
