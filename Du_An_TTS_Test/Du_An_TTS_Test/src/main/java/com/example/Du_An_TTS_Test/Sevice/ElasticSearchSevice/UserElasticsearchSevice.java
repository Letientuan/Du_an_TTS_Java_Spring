package com.example.Du_An_TTS_Test.Sevice.ElasticSearchSevice;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import com.example.Du_An_TTS_Test.Dto.ProductsElasticsearch;
import com.example.Du_An_TTS_Test.Dto.UserElasticsearch;
import com.example.Du_An_TTS_Test.Entity.Products;
import com.example.Du_An_TTS_Test.Entity.Users;
import com.example.Du_An_TTS_Test.Repository.UserElasticsearchRepository;
import com.example.Du_An_TTS_Test.Util.ElastcSearchUtil;
import com.example.Du_An_TTS_Test.Util.UserElasticSearch;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.RefreshPolicy;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Optional;
import java.util.function.Supplier;

@Service
public class UserElasticsearchSevice {

    @Autowired
    private UserElasticsearchRepository userElasticsearchRepository;

    @Autowired
    private ElasticsearchClient elasticsearchClient;

    public Iterable<UserElasticsearch> getUser() {
        return userElasticsearchRepository.findAll();
    }


    @KafkaListener(topics = "addUser", groupId = "products-group")
    public UserElasticsearch addUser(String logMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserElasticsearch user = objectMapper.readValue(logMessage, UserElasticsearch.class);
        return userElasticsearchRepository.save(user);
    }

    @KafkaListener(topics = "updateUser", groupId = "products-group")
    public void updateUser(String logMessage) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        UserElasticsearch user = objectMapper.readValue(logMessage, UserElasticsearch.class);
        Optional<UserElasticsearch> existingProductOpt = userElasticsearchRepository.findById(user.getId());
        if (existingProductOpt.isPresent()) {
            UserElasticsearch existingUser = existingProductOpt.get();
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
        Users user = objectMapper.readValue(logMessage, Users.class);
        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        Optional<UserElasticsearch> elasticsearch = userElasticsearchRepository.findById(user.getId());
        UserElasticsearch existingUser = elasticsearch.get();
        existingUser.setEmail(user.getEmail());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        existingUser.setUpdated_at(user.getUpdated_at());
        userElasticsearchRepository.save(existingUser);
    }

    public SearchResponse<UserElasticsearch> matchAllSeviceUser() throws IOException {
        Supplier<Query> supplier = ElastcSearchUtil.supplier();
        SearchResponse<UserElasticsearch> mapSearchResponse = elasticsearchClient.search(s -> s.index("users").query(supplier.get()), UserElasticsearch.class);
        System.out.println(":" + supplier.get().toString());

        return mapSearchResponse;
    }
    public SearchResponse<UserElasticsearch> fieldNameSeviceUser(String name) throws IOException {
        Supplier<Query> supplier = UserElasticSearch.suppliernameUser(name);
        SearchResponse<UserElasticsearch> mapSearchResponse = elasticsearchClient.search(s -> s.index("users").query(supplier.get()), UserElasticsearch.class);
        System.out.println(":" + supplier.get().toString());

        return mapSearchResponse;
    }


}
