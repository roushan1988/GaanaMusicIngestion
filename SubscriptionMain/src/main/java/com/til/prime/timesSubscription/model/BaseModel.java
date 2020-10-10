package com.til.prime.timesSubscription.model;

import javax.persistence.*;

@MappedSuperclass
public class BaseModel{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;
//    @Column(name="created")
//    private Date created;
//    @Column(name="updated", insertable=false, updatable = false)
//    private Date updated;
//    @Column(name="deleted")
//    private boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

//    public Date getCreated() {
//        return created;
//    }
//
//    public void setCreated(Date created) {
//        this.created = created;
//    }
//
//    public Date getUpdated() {
//        return updated;
//    }
//
//    public void setUpdated(Date updated) {
//        this.updated = updated;
//    }
//
//    public boolean isDeleted() {
//        return deleted;
//    }
//
//    public void setIsDelete(boolean delete) {
//        this.deleted = delete;
//    }
}
