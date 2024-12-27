package com.example.Du_An_TTS_Test.Repository;

import com.example.Du_An_TTS_Test.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsersRepo extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
