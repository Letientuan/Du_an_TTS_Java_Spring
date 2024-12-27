package com.example.Du_An_TTS_Test.Sevice;

import com.example.Du_An_TTS_Test.Dto.UserDto;
import com.example.Du_An_TTS_Test.Entity.Role;
import com.example.Du_An_TTS_Test.Entity.User;
import com.example.Du_An_TTS_Test.Map.UserMapper;
import com.example.Du_An_TTS_Test.Repository.RoleRepo;
import com.example.Du_An_TTS_Test.Repository.UsersRepo;
import com.example.Du_An_TTS_Test.exception.ErrorCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class UsersSevice {

    @Autowired
    private UsersRepo usersRepo;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    RoleRepo roleRepo;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    LocalDateTime currentDateTime = LocalDateTime.now();

    public List<User> findAll() {
        return usersRepo.findAll();
    }

    public void deleteUser(Integer id) {
        try {
            usersRepo.deleteById(id);
            String logMessage = "" + id;
            kafkaTemplate.send("deleteUser", logMessage);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public Boolean addUser(UserDto user) {
        try {
            User users = new User();

            users.setUsername(user.getUsername());

            Set<Role> role = new HashSet<>();
            Role roles = new Role();
            roles.setName("USER");
            roles.setDescription("WHITE");

            Optional<Role> roleid = roleRepo.findById(roles.getName());

            if (roleid.isEmpty()) {
                roleRepo.save(roles);
                role.add(roles);
            }

            users.setRoles(role);
            users.setPassword(passwordEncoder.encode(user.getPassword()));
            users.setCreatedAt(currentDateTime);
            users.setEmail(user.getEmail());

            User newUser = usersRepo.save(users);
            if (newUser != null) {

                String logMessage = objectMapper.writeValueAsString(newUser);
                kafkaTemplate.send("addUser", logMessage);

                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public Boolean updateUser(Integer id, UserDto users) {
        try {
            User optional = usersRepo.findById(id).orElseThrow(()
                    -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));

            optional.setUpdatedAt(currentDateTime);
            optional.setEmail(users.getEmail());
            optional.setPassword(passwordEncoder.encode(users.getPassword()));

            User newUser = usersRepo.save(optional);

            String logMessage = objectMapper.writeValueAsString(newUser);
            kafkaTemplate.send("updateUser", logMessage);

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
    }
}
