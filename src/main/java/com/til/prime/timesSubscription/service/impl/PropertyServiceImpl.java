package com.til.prime.timesSubscription.service.impl;

import com.google.common.cache.CacheBuilder;
import com.til.prime.timesSubscription.dao.SubscriptionPropertyRepository;
import com.til.prime.timesSubscription.enums.PropertyEnum;
import com.til.prime.timesSubscription.model.SubscriptionPropertyModel;
import com.til.prime.timesSubscription.service.PropertyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;

@Service
public class PropertyServiceImpl implements PropertyService {
    private static final Logger LOG = Logger.getLogger(PropertyServiceImpl.class);
    private static final ConcurrentMap<Object, Object> propertyMap = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).build().asMap();

    @Autowired
    private SubscriptionPropertyRepository subscriptionPropertyRepository;

    @Override
//    @Scheduled(cron = "0 */30 * * * ?")
    public void reload(){
        LOG.info("Property reload start");
        for(PropertyEnum propertyEnum: PropertyEnum.values()){
            SubscriptionPropertyModel subscriptionProperty = subscriptionPropertyRepository.findByKey(propertyEnum.name());
            if(subscriptionProperty==null){
                continue;
            }
            propertyMap.put(subscriptionProperty.getKey(), parseValue(subscriptionProperty.getValue(), propertyEnum.getType()));
        }
        LOG.info("Properties reloaded");
    }

    private Object parseValue(String value, String type){
        switch(type){
            case "Integer": return Integer.parseInt(value);
            case "Long": return Long.parseLong(value);
            case "Float": return Float.parseFloat(value);
            case "Double": return Double.parseDouble(value);
            case "String": return value;
            default: return value;
        }
    }
}
