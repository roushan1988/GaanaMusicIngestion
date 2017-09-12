package com.til.prime.timesSubscription.dto.internal;

import com.til.prime.timesSubscription.model.BaseModel;

import java.util.ArrayList;
import java.util.List;

public class AffectedModelDetails {
    private List<? extends BaseModel> affectedModels = new ArrayList<>();

    public List<? extends BaseModel> getAffectedModels() {
        return affectedModels;
    }

    public void setAffectedModels(List<? extends BaseModel> affectedModels) {
        this.affectedModels = affectedModels;
    }
}
