package com.til.prime.timesSubscription.convertor;

import com.til.prime.timesSubscription.dto.external.SubscriptionPlanDTO;
import com.til.prime.timesSubscription.dto.external.SubscriptionVariantDTO;
import com.til.prime.timesSubscription.dto.external.UserSubscriptionDTO;
import com.til.prime.timesSubscription.enums.PlanTypeEnum;
import com.til.prime.timesSubscription.model.SubscriptionPlanModel;
import com.til.prime.timesSubscription.model.SubscriptionVariantModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;

import java.util.*;

public class ModelToDTOConvertorUtil {

    public static SubscriptionVariantDTO getSubscriptionVariantDTO(SubscriptionVariantModel variantModel) {
        SubscriptionVariantDTO subscriptionVariantDTO = new SubscriptionVariantDTO(variantModel.getId(), variantModel.getName(), variantModel.getPlanType(),
                variantModel.getPrice(), variantModel.getDurationDays(), variantModel.isRecurring());
        return subscriptionVariantDTO;
    }

    public static final SubscriptionPlanDTO getSubscriptionPlanDTO(SubscriptionPlanModel model, UserSubscriptionModel lastUserSubscription){
        Set<PlanTypeEnum> restrictedPlanTypes = new HashSet<>();
        if(lastUserSubscription!=null){
            restrictedPlanTypes.add(PlanTypeEnum.TRIAL);
            PlanTypeEnum planType = lastUserSubscription.getSubscriptionVariant().getPlanType();
            if(planType==PlanTypeEnum.TRIAL_WITH_PAYMENT || planType==PlanTypeEnum.PAYMENT){
                restrictedPlanTypes.add(PlanTypeEnum.TRIAL_WITH_PAYMENT);
            }
        }
        SubscriptionPlanDTO dto = new SubscriptionPlanDTO();
        dto.setPlanId(model.getId());
        dto.setName(model.getName());
        dto.setBusiness(model.getBusiness());
        dto.setCurrency(model.getCurrency());
        dto.setCountry(model.getCountry());
        if(model.getVariants()!=null){
            dto.setVariants(new ArrayList<>());
            for(SubscriptionVariantModel variantModel: model.getVariants()){
                if(!restrictedPlanTypes.contains(variantModel.getPlanType())){
                    dto.getVariants().add(new SubscriptionVariantDTO(variantModel.getId(), variantModel.getName(), variantModel.getPlanType(), variantModel.getPrice(), variantModel.getDurationDays(), variantModel.isRecurring()));
                }
            }
            Collections.sort(dto.getVariants());
            dto.setVariants(Arrays.asList(dto.getVariants().get(0)));
        }
        return dto;
    }

    public static final UserSubscriptionDTO getUserSubscriptionDTO(UserSubscriptionModel model){
        UserSubscriptionDTO dto = new UserSubscriptionDTO();
        SubscriptionVariantModel variantModel = model.getSubscriptionVariant();
        dto.setOrderId(model.getOrderId());
        dto.setUserSubscriptionId(model.getId());
        dto.setSubscriptionVariantId(model.getSubscriptionVariant().getId());
        dto.setName(variantModel.getName());
        dto.setPlanType(variantModel.getPlanType().name());
        dto.setDurationDays(variantModel.getDurationDays());
        dto.setPrice(model.getPrice());
        dto.setStartDate(model.getStartDate());
        dto.setEndDate(model.getEndDate());
        dto.setPlanStatus(model.getPlanStatus().name());
        dto.setTransactionStatus(model.getTransactionStatus().name());
        dto.setBusiness(model.getBusiness().name());
        dto.setCountry(variantModel.getSubscriptionPlan().getCountry().name());
        dto.setCurrency(variantModel.getSubscriptionPlan().getCurrency().name());
        dto.setStatus(model.getStatus().name());
        return dto;
    }
}