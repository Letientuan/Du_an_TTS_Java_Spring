package com.example.Du_An_TTS_Test.Repository;

import com.example.Du_An_TTS_Test.Entity.comments;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepo extends JpaRepository<comments,Integer> {
    
}