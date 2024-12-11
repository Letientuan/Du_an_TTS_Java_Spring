package com.example.Du_An_TTS_Test.Controller;

import com.example.Du_An_TTS_Test.Entity.comments;
import com.example.Du_An_TTS_Test.Sevice.CommentSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/comment")
public class comentsController {

    @Autowired
    private CommentSevice commentSevice;

    @GetMapping("")
    public ResponseEntity<?> GetAll() {
        List<comments> commentsList = commentSevice.getAll();
        return ResponseEntity.ok(commentsList);
    }

    @PostMapping("add")
    public ResponseEntity<?> addComment(@RequestBody comments comments) {

        comments commentslist = commentSevice.addComment(comments);
        return ResponseEntity.ok(commentslist);
    }

    @PostMapping("update")
    public ResponseEntity<?> updateComment(@RequestBody comments Comments) {
        comments commentslist = commentSevice.update(Comments.getId(), Comments);
        return ResponseEntity.ok(commentslist);
    }

    @DeleteMapping("delete")
    public ResponseEntity<?> deleteComment(@RequestParam("id") Integer id) {
        commentSevice.deleteById(id);
        return ResponseEntity.ok(id);
    }
}
