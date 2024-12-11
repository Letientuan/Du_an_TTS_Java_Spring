package com.example.Du_An_TTS_Test.Util;

import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import lombok.val;

import java.util.function.Supplier;

public class ElastcSearchUtil {

    public static Supplier<Query> supplier() {
        Supplier<Query> supplier = () -> Query.of(q -> q.matchAll(matchAllQuery()));
        return supplier;
    }

    public static MatchAllQuery matchAllQuery() {

        val MatchAllQuery = new MatchAllQuery.Builder();
        return MatchAllQuery.build();
    }

    public static Supplier<Query> suppliername(String name) {
        Supplier<Query> supplier = () -> Query.of(q -> q.match(matchAllQueryName(name)));
        return supplier;
    }

    public static MatchQuery matchAllQueryName(String fieldName) {

        val MatchQuery = new MatchQuery.Builder();
        return MatchQuery.field("name").query(fieldName).build();
    }
}
