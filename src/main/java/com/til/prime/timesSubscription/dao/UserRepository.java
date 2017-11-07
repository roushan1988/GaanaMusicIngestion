package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.UserModel;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends GenericJpaRepository<UserModel, Long> {
    UserModel findByMobile(String mobile);
}
