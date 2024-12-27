package com.example.Du_An_TTS_Test.Repository;

import com.example.Du_An_TTS_Test.Dto.UserDto;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserElasticsearchRepository extends ElasticsearchRepository<UserDto, Integer> {
}

