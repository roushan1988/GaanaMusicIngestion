package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.constants.RedisConstants;
import com.til.prime.timesSubscription.dto.internal.SSOAuthDTO;
import com.til.prime.timesSubscription.service.CacheService;
import org.apache.log4j.Logger;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class CacheServiceImpl implements CacheService {
    private static final Logger LOG = Logger.getLogger(CacheServiceImpl.class);

    @Resource
    private CacheManager cacheManager;

    @Override
    public SSOAuthDTO getAuthCache(String mobile){
        Cache.ValueWrapper vw = cacheManager.getCache(RedisConstants.SSO_AUTH_CACHE_KEY).get(mobile);
        if(vw!=null){
            return (SSOAuthDTO) vw.get();
        }
        return null;
    }

    @Override
    public void updateAuthCache(String mobile, SSOAuthDTO authDTO){
        cacheManager.getCache(RedisConstants.SSO_AUTH_CACHE_KEY).put(mobile, authDTO);
    }

    @Override
    public void deleteAuthCache(String mobile){
        cacheManager.getCache(RedisConstants.SSO_AUTH_CACHE_KEY).evict(mobile);
    }
}
