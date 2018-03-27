package com.til.prime.timesSubscription.dao;

import com.til.prime.timesSubscription.model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends GenericJpaRepository<UserModel, Long> {
    UserModel findByMobileAndDeletedFalse(String mobile);
    List<UserModel> findByMobile(String mobile);
    UserModel findFirstByMobileAndPrimeIdNotNull(String mobile);
    UserModel findFirstByPrimeId(String primeId);
    UserModel findBySsoIdAndDeletedFalse(String ssoId);
    UserModel findByEmailAndDeletedFalse(String email);
    Page<UserModel> findByFirstNameAndLastNameAndDeletedFalse(String firstName, String lastName, Pageable pageable);
}
