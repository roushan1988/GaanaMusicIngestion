package com.til.prime.timesSubscription.service.impl;

import com.google.common.cache.CacheBuilder;
import com.til.prime.timesSubscription.dao.SubscriptionPropertyRepository;
import com.til.prime.timesSubscription.enums.PropertyEnum;
import com.til.prime.timesSubscription.model.SubscriptionPropertyModel;
import com.til.prime.timesSubscription.service.PropertyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {
    private static final Logger LOG = Logger.getLogger(PropertyServiceImpl.class);
    private static final ConcurrentMap<PropertyEnum, Object> propertyMap = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).<PropertyEnum, Object>build().asMap();

    @Autowired
    private SubscriptionPropertyRepository subscriptionPropertyRepository;

    @Override
    @Scheduled(cron = "0 */30 * * * ?")
    public void reload(){
        LOG.info("Property reload start");
        for(PropertyEnum propertyEnum: PropertyEnum.values()){
            SubscriptionPropertyModel subscriptionProperty = subscriptionPropertyRepository.findByKey(propertyEnum);
            if(subscriptionProperty==null){
                continue;
            }
            propertyMap.put(subscriptionProperty.getKey(), parseValue(subscriptionProperty.getValue(), propertyEnum.getType()));
        }
        LOG.info("Properties reloaded");
    }

    private Object getProperty(PropertyEnum propertyEnum){
        Object value = propertyMap.get(propertyEnum);
        if(value==null){
            LOG.info("Find coupon property - " + propertyEnum.name());
            SubscriptionPropertyModel subscriptionProperty = subscriptionPropertyRepository.findByKey(propertyEnum);
            if(subscriptionProperty==null){
                return null;
            }
            propertyMap.put(subscriptionProperty.getKey(), parseValue(subscriptionProperty.getValue(), propertyEnum.getType()));
        }
        return propertyMap.get(propertyEnum.name());
    }

    @Override
    public List<Long> getSubscriptionRenewalReminderDays() {
        return Arrays.stream(((String) getProperty(PropertyEnum.SUBSCRIPTION_RENEWAL_REMINDER_DAYS)).split(",")).map(i -> Long.parseLong(i)).collect(Collectors.toList());
    }

    @Override
    public List<Long> getSubscriptionExpiryReminderDays() {
        return Arrays.stream(((String) getProperty(PropertyEnum.SUBSCRIPTION_EXPIRY_REMINDER_DAYS)).split(",")).map(i -> Long.parseLong(i)).collect(Collectors.toList());
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
