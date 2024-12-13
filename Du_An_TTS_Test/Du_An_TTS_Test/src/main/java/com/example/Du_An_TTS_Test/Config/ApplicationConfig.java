package com.example.Du_An_TTS_Test.Config;

import com.example.Du_An_TTS_Test.Entity.Role;
import com.example.Du_An_TTS_Test.Entity.Users;
import com.example.Du_An_TTS_Test.Repository.RoleRepo;
import com.example.Du_An_TTS_Test.Repository.UsersRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationConfig {

    @Autowired
    PasswordEncoder passwordEncoder;

    RoleRepo roleRepo;

    @Bean
    ApplicationRunner applicationRunner(UsersRepo usersRepo) {
        return args -> {
            Set<Role> role = new HashSet<>();
            Role roles = new Role();
            roles.setName("ADMIN");
            role.add(roles);


            if (usersRepo.findByUsername("admin").isEmpty()) {
                Users users = Users.builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(role)
                        .build();
                usersRepo.save(users);
            }
        };
    }
}
