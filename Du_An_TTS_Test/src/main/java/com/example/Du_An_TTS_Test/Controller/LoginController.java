package com.example.Du_An_TTS_Test.Controller;

import com.example.Du_An_TTS_Test.Dto.Repon.ApiResponse;
import com.example.Du_An_TTS_Test.Dto.Repon.IntrospectRequet;
import com.example.Du_An_TTS_Test.Dto.Repon.introspectRepon;
import com.example.Du_An_TTS_Test.Dto.Request.AuthenticationRequest;
import com.example.Du_An_TTS_Test.Entity.Users;

import com.example.Du_An_TTS_Test.Sevice.AuthenticationSevice;
import com.example.Du_An_TTS_Test.Sevice.UsersSevice;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.text.ParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class LoginController {


    @Autowired
    private UsersSevice usersService;

    AuthenticationSevice authenticationSevice;
    @Autowired
    PasswordEncoder passwordEncoder;


    @PostMapping("login")
    public ApiResponse<IntrospectRequet> getAllUsers(@RequestBody AuthenticationRequest request) {
        var result = authenticationSevice.authenticated(request);
        return ApiResponse.<IntrospectRequet>builder()
                .result(result)
                .build();

    }

    @PostMapping("introspect")
    public ApiResponse<introspectRepon> getAllUsers(@RequestBody IntrospectRequet request) throws ParseException, JOSEException {
        var result = authenticationSevice.introspectRepon(request);
        return ApiResponse.<introspectRepon>builder()
                .result(result)
                .build();

    }

    @PostMapping("user/login")
    public ResponseEntity<String> login(
            @RequestParam(name = "username") String username,
            @RequestParam(name = "password") String password) {

        Optional<Users> userOpt = usersService.findByUsername(username);

        if (userOpt.isPresent()) {
            Users user = userOpt.get();

            if (passwordEncoder.matches(password, user.getPassword())) {
                return ResponseEntity.ok("Login successful for user: " + username);
            } else {
                return ResponseEntity.status(401).body("Incorrect password");
            }
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }


    @PostMapping("addUser")
    public ResponseEntity<?> addUser(@RequestBody Users users) {
        usersService.addUser(users);
        return ResponseEntity.ok("đăng ký thành công");
    }
}
