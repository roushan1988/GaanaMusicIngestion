package com.til.prime.timesSubscription.service.impl;

import com.google.common.cache.CacheBuilder;
import com.til.prime.timesSubscription.convertor.ModelToDTOConvertorUtil;
import com.til.prime.timesSubscription.dao.ExternalClientRepository;
import com.til.prime.timesSubscription.dao.SubscriptionPlanRepository;
import com.til.prime.timesSubscription.dao.SubscriptionPropertyRepository;
import com.til.prime.timesSubscription.dto.external.GenericResponse;
import com.til.prime.timesSubscription.dto.external.PropertyDataUpdateRequestCRM;
import com.til.prime.timesSubscription.dto.external.SubscriptionPlanDTO;
import com.til.prime.timesSubscription.enums.BusinessEnum;
import com.til.prime.timesSubscription.enums.CountryEnum;
import com.til.prime.timesSubscription.enums.PlanTypeEnum;
import com.til.prime.timesSubscription.enums.PropertyEnum;
import com.til.prime.timesSubscription.model.ExternalClientModel;
import com.til.prime.timesSubscription.model.SubscriptionPlanModel;
import com.til.prime.timesSubscription.model.SubscriptionPropertyModel;
import com.til.prime.timesSubscription.model.SubscriptionVariantModel;
import com.til.prime.timesSubscription.service.PropertyService;
import com.til.prime.timesSubscription.util.ResponseUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.*;
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
    @Autowired
    private SubscriptionPlanRepository planRepository;

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
        reloadAllPlans();
        reloadExternalClients();
        LOG.info("Properties reloaded");
    }

    @Override
    public Object   getProperty(PropertyEnum propertyEnum){
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
    public ExternalClientModel getExternalClient(String clientId) {
        if(getProperty(PropertyEnum.EXTERNAL_CLIENTS)==null){
            reloadExternalClients();
        }
        return ((Map<String, ExternalClientModel>)getProperty(PropertyEnum.EXTERNAL_CLIENTS)).get(clientId);
    }

    @Override
    public Long getSubscriptionCTARenewalReminderDays() {
        return (Long) getProperty(PropertyEnum.SUBSCRIPTION_CTA_RENEWAL_REMINDER_DAYS);
    }

    private void reloadExternalClients(){
        List<ExternalClientModel> clients = clientRepository.findByDeletedFalse();
        propertyMap.putIfAbsent(PropertyEnum.EXTERNAL_CLIENTS, new ConcurrentHashMap<String, ExternalClientModel>());
        ConcurrentMap<String, ExternalClientModel> clientMap = (ConcurrentMap<String, ExternalClientModel>) propertyMap.get(PropertyEnum.EXTERNAL_CLIENTS);
        for(ExternalClientModel client: clients){
            clientMap.put(client.getClientId(), client);
        }
    }

    @Override
    public SubscriptionVariantModel getBackendFreeTrialVariant(BusinessEnum business, CountryEnum country) {
        SubscriptionVariantModel variantModel = (SubscriptionVariantModel) getProperty(PropertyEnum.BACKEND_FREE_TRIAL_PLAN);
        if(variantModel==null){
            List<SubscriptionPlanModel> plans = getAllPlanModels(business, country);
            LOOP:
            for(SubscriptionPlanModel plan: plans){
                if(plan.getBusiness()==business && plan.getCountry()==country){
                    for(SubscriptionVariantModel variant: plan.getVariants()){
                        if(variant.getPlanType()== PlanTypeEnum.TRIAL){
                            variantModel = variant;
                            break LOOP;
                        }
                    }
                    break LOOP;
                }
            }
            propertyMap.put(PropertyEnum.BACKEND_FREE_TRIAL_PLAN, variantModel);
        }
        return variantModel;
    }

    @Override
    public List<SubscriptionPlanDTO> getAllPlans(BusinessEnum business, CountryEnum country) {
        if(getProperty(PropertyEnum.SUBSCRIPTION_PLAN_DTOS)==null){
            reloadAllPlans();
        }
        return ((ConcurrentMap<BusinessCountry, List<SubscriptionPlanDTO>>) getProperty(PropertyEnum.SUBSCRIPTION_PLAN_DTOS)).get(new BusinessCountry(business, country));
    }

    @Override
    public Map<PropertyEnum, String> getPropertyTableData() {
    	Map<PropertyEnum, String> propertyMap = new HashMap<>();
        for(PropertyEnum propertyEnum: PropertyEnum.CRM_UPDATE_PROPERTIES){
            SubscriptionPropertyModel subscriptionProperty = subscriptionPropertyRepository.findByKey(propertyEnum);
            if(subscriptionProperty==null){
                continue;
            }
            propertyMap.put(subscriptionProperty.getKey(), subscriptionProperty.getValue());
        }
        return propertyMap;
    }

    
    @Override
    public GenericResponse updatePropertyTableData(PropertyDataUpdateRequestCRM request){
        GenericResponse response = new GenericResponse();
        SubscriptionPropertyModel subscriptionProperty = subscriptionPropertyRepository.findByKey(request.getKey());
        if(subscriptionProperty==null){
            //ResponseUtil.createFailureResponse(response, validationResponse, validationErrorCategory);
        	return null;
        }else{
        	subscriptionProperty.setValue(request.getValue());
            subscriptionPropertyRepository.save(subscriptionProperty);
            ResponseUtil.createSuccessResponse(response);
        }
        
        
        return response;
    }

    @Override
    public void reloadAllPlans(){
        List<SubscriptionPlanModel> plans = planRepository.findByDeletedFalse();
        propertyMap.putIfAbsent(PropertyEnum.SUBSCRIPTION_PLAN_DTOS, new ConcurrentHashMap<BusinessCountry, List<SubscriptionPlanDTO>>());
        propertyMap.putIfAbsent(PropertyEnum.SUBSCRIPTION_PLAN_MODELS, new ConcurrentHashMap<BusinessCountry, List<SubscriptionPlanModel>>());
        ConcurrentMap<BusinessCountry, List<SubscriptionPlanDTO>> planDTOMap = (ConcurrentMap<BusinessCountry, List<SubscriptionPlanDTO>>) propertyMap.get(PropertyEnum.SUBSCRIPTION_PLAN_DTOS);
        ConcurrentMap<BusinessCountry, List<SubscriptionPlanModel>> planModelMap = (ConcurrentMap<BusinessCountry, List<SubscriptionPlanModel>>) propertyMap.get(PropertyEnum.SUBSCRIPTION_PLAN_MODELS);
        Map<BusinessCountry, List<SubscriptionPlanDTO>> dtoMap = new HashMap<>();
        Map<BusinessCountry, List<SubscriptionPlanModel>> modelMap = new HashMap<>();
        for(SubscriptionPlanModel plan: plans){
            Collections.sort(plan.getVariants());
            BusinessCountry businessCountry = new BusinessCountry(plan.getBusiness(), plan.getCountry());
            modelMap.putIfAbsent(businessCountry, new ArrayList<>());
            dtoMap.putIfAbsent(businessCountry, new ArrayList<>());
            modelMap.get(businessCountry).add(plan);
            dtoMap.get(businessCountry).add(ModelToDTOConvertorUtil.getSubscriptionPlanDTO(plan, null));
        }
        planDTOMap.putAll(dtoMap);
        planModelMap.putAll(modelMap);
    }
    
    @Override
    public List<SubscriptionPlanModel> getAllPlanModels(BusinessEnum business, CountryEnum country) {
        if(getProperty(PropertyEnum.SUBSCRIPTION_PLAN_MODELS)==null){
            reloadAllPlans();
        }
        return ((ConcurrentMap<BusinessCountry, List<SubscriptionPlanModel>>) getProperty(PropertyEnum.SUBSCRIPTION_PLAN_MODELS)).get(new BusinessCountry(business, country));
    }

    private Object parseValue(String value, String type){
        switch(type){
            case "Integer": return Integer.parseInt(value);
            case "Long": return Long.parseLong(value);
            case "Float": return Float.parseFloat(value);
            case "Double": return Double.parseDouble(value);
            case "String": return value;
            case "List<Long>": return Arrays.stream(value.split(",")).map(Long::parseLong).collect(Collectors.toList());
            case "List<String>": return Arrays.stream(value.split(",")).collect(Collectors.toList());
            default: return value;
        }
    }

    private class BusinessCountry{
        BusinessEnum business;
        CountryEnum country;

        public BusinessCountry(BusinessEnum business, CountryEnum country) {
            this.business = business;
            this.country = country;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            BusinessCountry that = (BusinessCountry) o;

            if (business != that.business) return false;
            return country == that.country;
        }

        @Override
        public int hashCode() {
            int result = business.hashCode();
            result = 31 * result + country.hashCode();
            return result;
        }
    }
}
