package com.example.Du_An_TTS_Test.Controller;

import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.Du_An_TTS_Test.Dto.Repon.ApiResponse;
import com.example.Du_An_TTS_Test.Dto.UserDto;
import com.example.Du_An_TTS_Test.Entity.User;
import com.example.Du_An_TTS_Test.Sevice.ElasticSearchSevice.UserElasticsearchSevice;
import com.example.Du_An_TTS_Test.Sevice.UsersSevice;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/User")
@Slf4j
public class UserController {

    @Autowired
    private UserElasticsearchSevice userElasticsearchSevice;
    @Autowired
    private UsersSevice usersSevice;

    @GetMapping ("getAll")
    public ResponseEntity<?> getAll(){
        return ResponseEntity.ok(usersSevice.findAll());
    }

    @PostMapping("add")
    public ResponseEntity<?> addUser(@RequestBody UserDto user) {
        Boolean  addUser = usersSevice.addUser(user);
        if(addUser == true){
            return ResponseEntity.ok("add Thành công");
        }
        return ResponseEntity.ok("add không thành công");

    }


    @PutMapping("update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UserDto users) {
        Boolean upDate =  usersSevice.updateUser(id, users);
        if(upDate==true){
            return ResponseEntity.ok("Update Thành công");
        }
        return ResponseEntity.ok("Update không Thành công");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        usersSevice.deleteUser(id);

        return ResponseEntity.ok("delete thành công");
    }

    @GetMapping("matchAllUser")
    public List<UserDto> matchAllUser() throws IOException {

        SearchResponse<UserDto> mapSearchResponse = userElasticsearchSevice.matchAllSeviceUser();

        System.out.println("search" + mapSearchResponse.hits().hits().toString());
        List<Hit<UserDto>> hitList = mapSearchResponse.hits().hits();
        List<UserDto> list = new ArrayList<>();
        for (Hit<UserDto> hit : hitList

        ) {
            list.add(hit.source());
        }
        return list;

    }

    @GetMapping("matchAllUser/{name}")
    public List<UserDto> matchAllProductFiledName(@PathVariable String name) throws IOException {

        SearchResponse<UserDto> mapSearchResponse = userElasticsearchSevice.fieldNameSeviceUser(name);

        List<Hit<UserDto>> hitList = mapSearchResponse.hits().hits();
        List<UserDto> list = new ArrayList<>();
        for (Hit<UserDto> hit : hitList
        ) {
            list.add(hit.source());
        }
        return list;

    }
}
