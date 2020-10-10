package com.til.prime.timesSubscription.model;

import java.util.Date;

public interface IBaseModel {
    long serialVersionUID = 1L;

    Long getId();

    void setId(Long id);

    Date getCreated();

    void setCreated(Date createdTime);

    Date getUpdated();

    void setUpdated(Date updatedTime);

    boolean isDeleted();

    void setIsDelete(boolean delete);
}
