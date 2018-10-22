package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.internal.SSOAuthDTO;

public interface CacheService {
    SSOAuthDTO getAuthCache(String mobile);
    void updateAuthCache(String mobile, SSOAuthDTO authDTO);
    void deleteAuthCache(String mobile);
}
