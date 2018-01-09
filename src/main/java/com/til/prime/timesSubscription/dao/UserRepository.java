package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.UserModel;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends GenericJpaRepository<UserModel, Long> {
    UserModel findByMobileAndDeletedFalse(String mobile);
    UserModel findBySsoIdAndDeletedFalse(String ssoId);
    UserModel findByEmailAndDeletedFalse(String email);
    List<UserModel> findByNameAndDeletedFalse(String name);
}
