package com.example.Du_An_TTS_Test.Sevice;

import com.example.Du_An_TTS_Test.Entity.Comments;
import com.example.Du_An_TTS_Test.Repository.CommentRepo;
import com.example.Du_An_TTS_Test.exception.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentSevice {

    @Autowired
    private CommentRepo commentRepo;

    public List<Comments> getAll() {
        List<Comments> commentsList = commentRepo.findAll();
        return commentsList;
    }

    public Comments addComment(Comments comment) {
        commentRepo.save(comment);
        return comment;
    }

    public Comments deleteById(Integer id) {
        Optional<Comments> optional = commentRepo.findById(id);
        return optional.map(o -> {
            commentRepo.delete(o);
            return o;
        }).orElseThrow(()
                -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));
    }

    public Comments update(Integer id, Comments newcomments) {
        Optional<Comments> optional = commentRepo.findById(id);
        return optional.map(o -> {
            o.setComment_text(newcomments.getComment_text());
            o.setUser_id(newcomments.getUser_id());
            o.setProduct_id(newcomments.getProduct_id());
            o.setCreated_at(newcomments.getCreated_at());
            o.setComment_text(newcomments.getComment_text());
            return commentRepo.save(o);
        }).orElseThrow(()
                -> new RuntimeException(ErrorCode.INVALID_ID.getMessage()));
    }

}
