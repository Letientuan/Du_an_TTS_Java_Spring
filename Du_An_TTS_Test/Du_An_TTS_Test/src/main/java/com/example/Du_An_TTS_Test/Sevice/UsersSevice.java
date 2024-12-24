package com.example.Du_An_TTS_Test.Sevice;

import com.example.Du_An_TTS_Test.Dto.UserElasticsearch;
import com.example.Du_An_TTS_Test.Entity.Role;
import com.example.Du_An_TTS_Test.Entity.Users;
import com.example.Du_An_TTS_Test.Map.UserMapper;
import com.example.Du_An_TTS_Test.Repository.RoleRepo;
import com.example.Du_An_TTS_Test.Repository.UsersRepo;
import com.example.Du_An_TTS_Test.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

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

    public List<Users> getAll() {
        List<Users> usersList = usersRepo.findAll();
        return usersList;
    }

    public Users findByUsername(String username) {
        return usersRepo.findByUsername(username).orElseThrow(()
                -> new RuntimeException(ErrorCode.INVALID_NAME.getMessage()));
    }

    public void deleteUser(Integer id) {
        usersRepo.deleteById(id);
    }

    public Users addUser(Users user) {

        Users users = new Users();
        users.setUsername(user.getUsername());
        Set<Role> role = new HashSet<>();
        Role roles = new Role();
        roles.setName("USER");
        roles.setDescription("WHITE");

        Optional<Role> role1 = roleRepo.findById(roles.getName());
        if (role1.isEmpty()) {
            roleRepo.save(roles);
            role.add(roles);
        }

        users.setRoles(role);
        users.setPassword(passwordEncoder.encode(user.getPassword()));
        users.setCreated_at(user.getCreated_at());
        users.setUpdated_at(user.getCreated_at());
        users.setEmail(user.getEmail());

        return usersRepo.save(users);
    }

    public Users updateUser(Integer id, UserElasticsearch users) {
        Users optional = usersRepo.findById(id).orElseThrow(()
                -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));
        UserMapper.USER_MAPPER.updateUserFromDto(users, optional);
        return usersRepo.save(optional);

    }
}
