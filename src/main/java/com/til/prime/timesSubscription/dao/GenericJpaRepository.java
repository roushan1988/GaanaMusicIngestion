package com.til.prime.timesSubscription.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created by roushan on 17/7/17.
 */
@NoRepositoryBean
public interface GenericJpaRepository<T, ID extends Serializable> extends JpaRepository<T, ID> {
    T findById(ID id);
    Long countByCreatedAfter(Date created);
    List<T> findByCreatedAfter(Date created);
    Page<T> findByCreatedAfter(Pageable pageable, Date created);
    Long countByUpdatedAfter(Date updated);
    List<T> findByUpdatedAfter(Date updated);
    Page<T> findByUpdatedAfter(Pageable pageable, Date updated);
    List<T> findByDeletedFalse();
}