package com.example.Du_An_TTS_Test.Repository;

import com.example.Du_An_TTS_Test.Entity.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidateTonkenRepo extends JpaRepository<InvalidatedToken,String> {
}
