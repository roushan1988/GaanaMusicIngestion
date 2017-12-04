package com.til.prime.timesSubscription.service.impl;

import com.google.common.cache.CacheBuilder;
import com.til.prime.timesSubscription.dao.ExternalClientRepository;
import com.til.prime.timesSubscription.dao.SubscriptionPropertyRepository;
import com.til.prime.timesSubscription.enums.PropertyEnum;
import com.til.prime.timesSubscription.model.ExternalClientModel;
import com.til.prime.timesSubscription.model.SubscriptionPropertyModel;
import com.til.prime.timesSubscription.service.PropertyService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class PropertyServiceImpl implements PropertyService {
    private static final Logger LOG = Logger.getLogger(PropertyServiceImpl.class);
    private static final ConcurrentMap<PropertyEnum, Object> propertyMap = CacheBuilder.newBuilder().expireAfterWrite(60, TimeUnit.MINUTES).<PropertyEnum, Object>build().asMap();

    @Autowired
    private SubscriptionPropertyRepository subscriptionPropertyRepository;
    @Autowired
    private ExternalClientRepository clientRepository;

    @Override
    @PostConstruct
    @Scheduled(cron = "0 */30 * * * ?")
    public void reload(){
        LOG.info("Property reload start");
        for(PropertyEnum propertyEnum: PropertyEnum.RELOAD_PROPERTIES){
            SubscriptionPropertyModel subscriptionProperty = subscriptionPropertyRepository.findByKey(propertyEnum);
            if(subscriptionProperty==null){
                continue;
            }
            propertyMap.put(subscriptionProperty.getKey(), parseValue(subscriptionProperty.getValue(), propertyEnum.getType()));
        }
        List<ExternalClientModel> clients = clientRepository.findAll();
        propertyMap.putIfAbsent(PropertyEnum.EXTERNAL_CLIENTS, new ConcurrentHashMap<String, ExternalClientModel>());
        ConcurrentMap<String, ExternalClientModel> clientMap = (ConcurrentMap<String, ExternalClientModel>) propertyMap.get(PropertyEnum.EXTERNAL_CLIENTS);
        for(ExternalClientModel client: clients){
            clientMap.put(client.getClientId(), client);
        }
        LOG.info("Properties reloaded");
    }

    @Override
    public Object getProperty(PropertyEnum propertyEnum){
        Object value = propertyMap.get(propertyEnum);
        if(value==null){
            LOG.info("Find property - " + propertyEnum.name());
            SubscriptionPropertyModel subscriptionProperty = subscriptionPropertyRepository.findByKey(propertyEnum);
            if(subscriptionProperty==null){
                return null;
            }
            propertyMap.put(subscriptionProperty.getKey(), parseValue(subscriptionProperty.getValue(), propertyEnum.getType()));
        }
        return propertyMap.get(propertyEnum);
    }

    @Override
    public List<Long> getSubscriptionRenewalReminderDays() {
        return Arrays.stream(((String) getProperty(PropertyEnum.SUBSCRIPTION_RENEWAL_REMINDER_DAYS)).split(",")).map(i -> Long.parseLong(i)).collect(Collectors.toList());
    }

    @Override
    public List<Long> getSubscriptionExpiryReminderDays() {
        return Arrays.stream(((String) getProperty(PropertyEnum.SUBSCRIPTION_EXPIRY_REMINDER_DAYS)).split(",")).map(i -> Long.parseLong(i)).collect(Collectors.toList());
    }

    @Override
    public List<Long> getExpiredSubscriptionRenewalReminderDays() {
        return Arrays.stream(((String) getProperty(PropertyEnum.EXPIRED_SUBSCRIPTION_RENEWAL_REMINDER_DAYS)).split(",")).map(i -> Long.parseLong(i)).collect(Collectors.toList());
    }

    @Override
    public ExternalClientModel getExternalClient(String clientId) {
        if(getProperty(PropertyEnum.EXTERNAL_CLIENTS)==null){
            List<ExternalClientModel> clients = clientRepository.findAll();
            propertyMap.putIfAbsent(PropertyEnum.EXTERNAL_CLIENTS, new ConcurrentHashMap<String, ExternalClientModel>());
            ConcurrentMap<String, ExternalClientModel> clientMap = (ConcurrentMap<String, ExternalClientModel>) propertyMap.get(PropertyEnum.EXTERNAL_CLIENTS);
            for(ExternalClientModel client: clients){
                clientMap.put(client.getClientId(), client);
            }
        }
        return ((Map<String, ExternalClientModel>)getProperty(PropertyEnum.EXTERNAL_CLIENTS)).get(clientId);
    }

    private Object parseValue(String value, String type){
        switch(type){
            case "Integer": return Integer.parseInt(value);
            case "Long": return Long.parseLong(value);
            case "Float": return Float.parseFloat(value);
            case "Double": return Double.parseDouble(value);
            case "String": return value;
            case "List<Long>": return Arrays.stream(value.split(",")).map(Long::parseLong).collect(Collectors.toList());
            default: return value;
        }
    }
}
