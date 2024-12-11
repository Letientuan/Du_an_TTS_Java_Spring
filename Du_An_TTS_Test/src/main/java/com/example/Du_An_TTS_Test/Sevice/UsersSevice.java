package com.example.Du_An_TTS_Test.Sevice;

import com.example.Du_An_TTS_Test.Entity.Role;
import com.example.Du_An_TTS_Test.Entity.Users;
import com.example.Du_An_TTS_Test.Repository.UsersRepo;
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

    public List<Users> getAll() {
        List<Users> usersList = usersRepo.findAll();
        return usersList;
    }

    public Optional<Users> findByUsername(String username) {
        return usersRepo.findByUsername(username);
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
        role.add(roles);
        users.setRoles(role);

        users.setPassword(passwordEncoder.encode(user.getPassword()));
        users.setCreated_at(user.getCreated_at());
        users.setUpdated_at(user.getCreated_at());
        users.setEmail(user.getEmail());

        return usersRepo.save(users);
    }

    public Users updateUser(Integer id, Users users) {
        Optional<Users> optional = usersRepo.findById(id);
        return optional.map(o -> {
            o.setEmail(users.getEmail());
            o.setUpdated_at(users.getUpdated_at());
            o.setUsername(users.getUsername());
            o.setPassword(users.getPassword());
            Users save = usersRepo.save(o);
            return save;
        }).orElse(null);
    }
}
