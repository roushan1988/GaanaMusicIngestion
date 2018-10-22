package com.til.prime.timesSubscription.dto.internal;

import java.util.ArrayList;
import java.util.List;

public class AffectedModelDetails<T> {
    private List<T> affectedModels = new ArrayList<>();

    public List<T> getAffectedModels() {
        return affectedModels;
    }

    public void setAffectedModels(List<T> affectedModels) {
        this.affectedModels = affectedModels;
    }
}
