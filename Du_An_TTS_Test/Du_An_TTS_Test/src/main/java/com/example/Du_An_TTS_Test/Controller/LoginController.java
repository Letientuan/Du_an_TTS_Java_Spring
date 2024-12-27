package com.example.Du_An_TTS_Test.Controller;

import com.example.Du_An_TTS_Test.Dto.Repon.ApiResponse;
import com.example.Du_An_TTS_Test.Dto.Repon.IntrospectRequet;
import com.example.Du_An_TTS_Test.Dto.Repon.introspectRepon;
import com.example.Du_An_TTS_Test.Dto.Repon.logoutRequet;
import com.example.Du_An_TTS_Test.Dto.Request.AuthenticationRequest;
import com.example.Du_An_TTS_Test.Entity.User;

import com.example.Du_An_TTS_Test.Sevice.AuthenticationSevice;
import com.example.Du_An_TTS_Test.Sevice.UsersSevice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginController {



    AuthenticationSevice authenticationSevice;



    @PostMapping("login")
    public ResponseEntity<IntrospectRequet> getAllUsers(@RequestBody AuthenticationRequest request) {
        var result = authenticationSevice.authenticated(request);
        return ResponseEntity.ok(result);

    }

    @PostMapping("introspect")
    public ResponseEntity<introspectRepon> checktoken(@RequestBody IntrospectRequet request) throws ParseException, JOSEException {
        var result = authenticationSevice.introspectRepon(request);
        return ResponseEntity.ok(result);

    }

    @PostMapping("logout")
    public ApiResponse<Void> logOut(@RequestBody logoutRequet request) throws ParseException, JOSEException {
        authenticationSevice.logOut(request);
        return ApiResponse.<Void>builder()
                .build();

    }

}
