package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.UserModel;

public interface UserRepository extends GenericJpaRepository<UserModel, Long> {
    UserModel findBySsoId(String ssoId);
    UserModel findByMobile(String mobile);
    UserModel findByIdAndSsoId(Long id, String ssoId);
}
