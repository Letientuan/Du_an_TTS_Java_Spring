package com.example.Du_An_TTS_Test.Controller.ElasticShearchController;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.example.Du_An_TTS_Test.Dto.ProductsElasticsearch;
import com.example.Du_An_TTS_Test.Dto.Request.AuthenticationRequest;
import com.example.Du_An_TTS_Test.Dto.UserElasticsearch;
import com.example.Du_An_TTS_Test.Entity.Role;
import com.example.Du_An_TTS_Test.Entity.Users;
import com.example.Du_An_TTS_Test.Sevice.ElasticSearchSevice.UserElasticsearchSevice;
import com.example.Du_An_TTS_Test.Sevice.UsersSevice;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@RestController
@RequestMapping("/User")
@Slf4j
public class UserElsaticSearchController {

    @Autowired
    private UserElasticsearchSevice userElasticsearchSevice;
    @Autowired
    private UsersSevice usersSevice;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private ObjectMapper objectMapper;


    @GetMapping("getAll")
    public ResponseEntity<?> getAll() {

        var authentication = SecurityContextHolder.getContext().getAuthentication();
        log.info("UserName :{}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ResponseEntity.ok(userElasticsearchSevice.getUser());
    }


    @PostMapping("add")
    public ResponseEntity<?> addUser(@RequestBody Users users) throws JsonProcessingException {

        Users userid = usersSevice.addUser(users);
        if (userid != null) {

            String logMessage = objectMapper.writeValueAsString(userid);
            kafkaTemplate.send("addUser", logMessage);

            return ResponseEntity.ok("add thành công");
        } else {
            return ResponseEntity.ok("add false");
        }
    }


    @PutMapping("update/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Integer id, @RequestBody UserElasticsearch users) throws JsonProcessingException {

        users.setId(id);
        Users userid = usersSevice.updateUser(id, users);
        String logMessage = objectMapper.writeValueAsString(userid);
        kafkaTemplate.send("updateUser", logMessage);
        return ResponseEntity.ok("upadate thành công");
    }

    @DeleteMapping("delete/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id) {

        usersSevice.deleteUser(id);
        String logMessage = "" + id;
        kafkaTemplate.send("deleteUser", logMessage);
        return ResponseEntity.ok("delete thành công");
    }

    @GetMapping("matchAllUser")
    public List<UserElasticsearch> matchAllUser() throws IOException {

        SearchResponse<UserElasticsearch> mapSearchResponse = userElasticsearchSevice.matchAllSeviceUser();

        System.out.println("search" + mapSearchResponse.hits().hits().toString());
        List<Hit<UserElasticsearch>> hitList = mapSearchResponse.hits().hits();
        List<UserElasticsearch> list = new ArrayList<>();
        for (Hit<UserElasticsearch> hit : hitList

        ) {
            list.add(hit.source());
        }
        return list;

    }

    @GetMapping("matchAllUser/{name}")
    public List<UserElasticsearch> matchAllProductFiledName(@PathVariable String name) throws IOException {

        SearchResponse<UserElasticsearch> mapSearchResponse = userElasticsearchSevice.fieldNameSeviceUser(name);

        List<Hit<UserElasticsearch>> hitList = mapSearchResponse.hits().hits();
        List<UserElasticsearch> list = new ArrayList<>();
        for (Hit<UserElasticsearch> hit : hitList
        ) {
            list.add(hit.source());
        }
        return list;

    }
}
