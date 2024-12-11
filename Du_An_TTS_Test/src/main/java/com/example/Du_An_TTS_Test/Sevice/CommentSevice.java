package com.example.Du_An_TTS_Test.Sevice;

import com.example.Du_An_TTS_Test.Entity.comments;
import com.example.Du_An_TTS_Test.Repository.CommentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CommentSevice {

    @Autowired
    private CommentRepo commentRepo;

    public List<comments> getAll() {
        List<comments> commentsList = commentRepo.findAll();
        return commentsList;
    }

    public comments addComment(comments comment) {
        return commentRepo.save(comment);
    }

    public comments deleteById(Integer id) {
        Optional<comments> optional = commentRepo.findById(id);
        return optional.map(o -> {
            commentRepo.delete(o);
            return o;
        }).orElse(null);
    }

    public comments update(Integer id, comments newcomments) {
        Optional<comments> optional = commentRepo.findById(id);
        return optional.map(o -> {
            o.setComment_text(newcomments.getComment_text());
            o.setUser_id(newcomments.getUser_id());
            o.setProduct_id(newcomments.getProduct_id());
            o.setCreated_at(newcomments.getCreated_at());
            o.setComment_text(newcomments.getComment_text());
            return commentRepo.save(o);
        }).orElse(null);
    }

}
