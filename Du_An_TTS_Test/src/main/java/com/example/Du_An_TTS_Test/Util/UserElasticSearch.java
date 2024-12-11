package com.example.Du_An_TTS_Test.Util;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.val;

import java.util.function.Supplier;

public class UserElasticSearch {

    public static Supplier<Query> suppliernameUser(String name) {
        Supplier<Query> supplier = () -> Query.of(q -> q.match(matchAllQueryName(name)));
        return supplier;
    }

    public static MatchQuery matchAllQueryName(String fieldName) {

        val MatchQuery = new MatchQuery.Builder();
        return MatchQuery.field("username").query(fieldName).build();
    }
}
