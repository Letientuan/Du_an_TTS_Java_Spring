package com.example.Du_An_TTS_Test.Sevice.ElasticSearchSevice;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.Du_An_TTS_Test.Dto.UserDto;
import com.example.Du_An_TTS_Test.Entity.User;
import com.example.Du_An_TTS_Test.Repository.UserElasticsearchRepository;
import com.example.Du_An_TTS_Test.Util.ElastcSearchUtil;
import com.example.Du_An_TTS_Test.Util.UserElasticSearch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class UserElasticsearchSevice {

    @Autowired
    private UserElasticsearchRepository userElasticsearchRepository;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    LocalDateTime currentDateTime = LocalDateTime.now();

    public Iterable<UserDto> getUser() {
        return userElasticsearchRepository.findAll();
    }



    @KafkaListener(topics = "addUser", groupId = "products-group")
    public UserDto addUser(String logMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto user = objectMapper.readValue(logMessage, UserDto.class);
        return userElasticsearchRepository.save(user);
    }

    @KafkaListener(topics = "updateUser", groupId = "products-group")
    public void updateUser(String logMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserDto user = objectMapper.readValue(logMessage, UserDto.class);
        Optional<UserDto> existingProductOpt = userElasticsearchRepository.findById(user.getId());
        if (existingProductOpt.isPresent()) {
            UserDto existingUser = existingProductOpt.get();
            userElasticsearchRepository.save(existingUser);
        } else {

        }

    }

    @KafkaListener(topics = "deleteUser")
    public void delete(String id) {
        userElasticsearchRepository.deleteById(Integer.valueOf(id));
    }

    @KafkaListener(topics = "updateUser", groupId = "products-group")
    public void UpdateuserElasticsearch(String logMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(logMessage, User.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Optional<UserDto> elasticsearch = userElasticsearchRepository.findById(user.getId());
        UserDto existingUser = elasticsearch.get();
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setUpdatedAt(currentDateTime.toString());
        userElasticsearchRepository.save(existingUser);
    }

    public SearchResponse<UserDto> matchAllSeviceUser() throws IOException {
        Supplier<Query> supplier = ElastcSearchUtil.supplier();
        SearchResponse<UserDto> mapSearchResponse = elasticsearchClient.search(s -> s.index("users").query(supplier.get()), UserDto.class);
        System.out.println(":" + supplier.get().toString());

        return mapSearchResponse;
    }

    public SearchResponse<UserDto> fieldNameSeviceUser(String name) throws IOException {
        Supplier<Query> supplier = UserElasticSearch.suppliernameUser(name);
        SearchResponse<UserDto> mapSearchResponse = elasticsearchClient.search(s -> s.index("users").query(supplier.get()), UserDto.class);
        System.out.println(":" + supplier.get().toString());

        return mapSearchResponse;
    }


}
