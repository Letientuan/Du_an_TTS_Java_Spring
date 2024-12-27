package com.example.Du_An_TTS_Test.Repository;

import com.example.Du_An_TTS_Test.Entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CommentRepo extends JpaRepository<Comment,Integer> {

    @Query("SELECT c FROM Comment c WHERE c.product.id = :id")
    List<Comment> findCommentsByProductId(@Param("id") Integer id);

}
