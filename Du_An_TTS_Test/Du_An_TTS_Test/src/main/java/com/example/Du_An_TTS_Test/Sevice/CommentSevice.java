package com.example.Du_An_TTS_Test.Sevice;

import com.example.Du_An_TTS_Test.Dto.CommentDto;
import com.example.Du_An_TTS_Test.Entity.Comment;
import com.example.Du_An_TTS_Test.Entity.Product;
import com.example.Du_An_TTS_Test.Entity.User;
import com.example.Du_An_TTS_Test.Map.CommentMapper;
import com.example.Du_An_TTS_Test.Repository.CommentRepo;
import com.example.Du_An_TTS_Test.Repository.ProductsRepo;
import com.example.Du_An_TTS_Test.Repository.UsersRepo;
import com.example.Du_An_TTS_Test.Util.JwtTokenUtil;
import com.example.Du_An_TTS_Test.exception.ErrorCode;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class CommentSevice {

    @Autowired
    private CommentRepo commentRepo;

    @Autowired
    private UsersRepo userRepo;

    @Autowired
    private ProductsRepo productRepo;



    LocalDateTime currentDateTime = LocalDateTime.now();

    public List<Comment> getCommentsByProductId(Integer productId) {
        return commentRepo.findCommentsByProductId(productId);
    }

    public CommentDto addComment(CommentDto commentDto) {
        try {
//            Integer userId = Integer.valueOf(jwtTokenUtil.id_user(token));
            User user = userRepo.findById(1).orElseThrow(()
                    -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));

            Product product = productRepo.findById(commentDto.getProduct()).orElseThrow(()
                    -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));


            Comment comment = new Comment();
            comment.setUser(user);
            comment.setProduct(product);
            comment.setCommentText(commentDto.getCommentText());
            comment.setCreatedAt(currentDateTime);

            commentDto.setCreatedAt(currentDateTime.toString());

            commentRepo.save(comment);
            return commentDto;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }

    public Comment findbyid(Integer Id) {
        return commentRepo.findById(Id).orElseThrow(() -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));
    }

    public Boolean deleteById(Integer id) {
       try {
           Comment comment = findbyid(id);
           commentRepo.delete(comment);
           return true;
       }catch (Exception exception){
           System.out.println(exception.getMessage());
           return false;
       }
    }

    public CommentDto update(Integer id, CommentDto newcommentDto) {
       try {
           Comment comment = findbyid(id);
           comment.setCommentText(newcommentDto.getCommentText());
           commentRepo.save(comment);
           return  newcommentDto;
       } catch (Exception exception){
        System.out.println(exception.getMessage());
        return null;
    }
    }

}
