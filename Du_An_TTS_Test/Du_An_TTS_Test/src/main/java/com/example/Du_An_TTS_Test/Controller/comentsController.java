package com.example.Du_An_TTS_Test.Controller;

import com.example.Du_An_TTS_Test.Dto.daoComments;
import com.example.Du_An_TTS_Test.Entity.Comments;
import com.example.Du_An_TTS_Test.Repository.CommentRepo;
import com.example.Du_An_TTS_Test.Sevice.CommentSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/comment")
public class comentsController {

    @Autowired
    private CommentSevice commentSevice;

    @Autowired
    private CommentRepo commentRepo;
    daoComments daoComments;

    @GetMapping("")
    public ResponseEntity<?> GetAll() {
        List<Comments> commentsList = commentSevice.getAll();
        return ResponseEntity.ok(commentsList);
    }

    @PostMapping("add")
    public ResponseEntity<?> addComment(@RequestBody Comments comments) {

        Comments commentslist = commentSevice.addComment(comments);
        return ResponseEntity.ok(commentslist);

    }

    @PostMapping("update/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Integer id, @RequestBody Comments Comments) {
        Optional<Comments> optional = commentRepo.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.ok("Không tìm thấy comments có id : " + id);
        }
        Comments commentslist = commentSevice.update(id, Comments);
        return ResponseEntity.ok(commentslist);

    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer id) {
        Optional<Comments> optional = commentRepo.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity.ok("Không tìm thấy comments có id : " + id);
        }
        commentSevice.deleteById(id);
        return ResponseEntity.ok("delete thành công ");
    }
}
