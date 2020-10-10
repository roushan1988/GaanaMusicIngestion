package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.model.MxGaanaDbEntity;

import java.util.List;

public interface FileWriterService {
    void readExcel() throws Exception;
    boolean prepareExcel(List<MxGaanaDbEntity> list) throws Exception;
}
