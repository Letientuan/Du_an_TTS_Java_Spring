package com.example.Du_An_TTS_Test.Controller;


import com.example.Du_An_TTS_Test.Dto.CommentDto;
import com.example.Du_An_TTS_Test.Sevice.CommentSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;



@Controller
@RequestMapping("/comment")
public class ComentController {

    @Autowired
    private CommentSevice commentSevice;


    @PostMapping("add")
    public ResponseEntity<?> addComment(@RequestBody CommentDto comment) {


        CommentDto comments = commentSevice.addComment(comment);

        return ResponseEntity.ok(comments);

    }

    @PostMapping("update/{id}")
    public ResponseEntity<?> updateComment(@PathVariable Integer id, @RequestBody CommentDto commentDto) {
     CommentDto comment = commentSevice.update(id,commentDto);

        return ResponseEntity.ok(comment);

    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> deleteComment(@PathVariable Integer id) {
        Boolean delete = commentSevice.deleteById(id);
        if(delete==true){
            return ResponseEntity.ok("delete thành công ");
        }
        return ResponseEntity.ok("delete không thành công ");
    }
}
