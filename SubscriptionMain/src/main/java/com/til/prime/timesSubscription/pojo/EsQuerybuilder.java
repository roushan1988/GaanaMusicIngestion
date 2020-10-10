package com.til.prime.timesSubscription.pojo;

import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import static org.elasticsearch.index.query.QueryBuilders.boolQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchPhraseQuery;
import static org.elasticsearch.index.query.QueryBuilders.matchQuery;

public class EsQuerybuilder {
    public static QueryBuilder build(String language, String songTitle) {
        BoolQueryBuilder query = boolQuery();
        if (StringUtils.isNotEmpty(language)) {
            query.must(matchQuery("language", language.toLowerCase()));
        }
        if (StringUtils.isNotEmpty(songTitle)) {
            query.must(matchPhraseQuery("trackTitle", songTitle.toLowerCase()));
        }
        return query;
    }
}
