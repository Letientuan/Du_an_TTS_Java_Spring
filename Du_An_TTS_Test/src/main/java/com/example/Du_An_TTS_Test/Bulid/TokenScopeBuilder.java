//package com.example.Du_An_TTS_Test.Bulid;
//
//import java.util.List;
//import java.util.StringJoiner;
//
//import com.example.Du_An_TTS_Test.Util.JwtTokenUtil;
//import org.springframework.util.CollectionUtils;
//
//public class TokenScopeBuilder {
//
//    private JwtTokenUtil jwtTokenUtil = new JwtTokenUtil();
//
//    public String buildScope(String token) {
//        StringJoiner stringJoiner = new StringJoiner(" ");
//
//        // Lấy vai trò từ token
//        List<String> roles = jwtTokenUtil.extractRoles(token);
//        if (!CollectionUtils.isEmpty(roles)) {
//            roles.forEach(role -> {
//                stringJoiner.add("ROLE_" + role);
//                // Lấy quyền (permissions) tương ứng với vai trò từ token
//                List<String> permissions = jwtTokenUtil.extractPermissions(token);
//                if (!CollectionUtils.isEmpty(permissions)) {
//                    permissions.forEach(permission -> stringJoiner.add(permission));
//                }
//            });
//        }
//
//        return stringJoiner.toString();
//    }
//}
