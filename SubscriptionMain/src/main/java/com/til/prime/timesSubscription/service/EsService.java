package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.model.RawMxGaanaDbEntity;
import com.til.prime.timesSubscription.pojo.SearchTuple;

import java.util.List;

public interface EsService {
    void build();
    List<RawMxGaanaDbEntity> search(SearchTuple tuple);
}
