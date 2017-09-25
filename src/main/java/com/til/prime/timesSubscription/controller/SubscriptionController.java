package com.til.prime.timesSubscription.controller;

import com.til.prime.timesSubscription.aspect.Loggable;
import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.service.SubscriptionService;
import com.til.prime.timesSubscription.util.ResponseUtil;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/til/subscription")
public class SubscriptionController {

    private static final Logger LOG = Logger.getLogger(SubscriptionController.class);
    @Autowired
    private SubscriptionService subscriptionService;

    @Loggable
    @RequestMapping(path="/getAllPlans", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public PlanListResponse getAllPlans(@RequestBody PlanListRequest request){
        try {
            return subscriptionService.getAllPlans(request);
        }catch (Exception e){
            LOG.error("Exception in getAllPlans: ", e);
            PlanListResponse response = new PlanListResponse();
            return (PlanListResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/initPurchase", method = RequestMethod.POST)
    @ResponseBody
    public InitPurchaseResponse initPurchase(@RequestBody InitPurchaseRequest request){
        try {
            return subscriptionService.initPurchasePlan(request);
        }catch (Exception e){
            LOG.error("Exception in initPurchase: ", e);
            InitPurchaseResponse response = new InitPurchaseResponse();
            return (InitPurchaseResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/generateOrder", method = RequestMethod.POST)
    @ResponseBody
    public GenerateOrderResponse generateOrder(@RequestBody GenerateOrderRequest request){
        try {
            return subscriptionService.generateOrder(request);
        }catch (Exception e){
            LOG.error("Exception in generateOrder: ", e);
            GenerateOrderResponse response = new GenerateOrderResponse();
            return (GenerateOrderResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/submitPurchase", method = RequestMethod.POST)
    @ResponseBody
    public SubmitPurchaseResponse submitPurchase(@RequestBody SubmitPurchaseRequest request){
        try {
            return subscriptionService.submitPurchasePlan(request);
        }catch (Exception e){
            LOG.error("Exception in submitPurchase: ", e);
            SubmitPurchaseResponse response = new SubmitPurchaseResponse();
            return (SubmitPurchaseResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/getPurchaseHistory", method = RequestMethod.GET)
    @ResponseBody
    public PurchaseHistoryResponse getPurchaseHistory(@RequestBody PurchaseHistoryRequest request){
        try {
            return subscriptionService.getPurchaseHistory(request);
        }catch (Exception e){
            LOG.error("Exception in getPurchaseHistory: ", e);
            PurchaseHistoryResponse response = new PurchaseHistoryResponse();
            return (PurchaseHistoryResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/cancelSubscription", method = RequestMethod.POST)
    @ResponseBody
    public CancelSubscriptionResponse cancelSubscription(@RequestBody CancelSubscriptionRequest request){
        try {
            return subscriptionService.cancelSubscription(request);
        }catch (Exception e){
            LOG.error("Exception in cancelSubscription: ", e);
            CancelSubscriptionResponse response = new CancelSubscriptionResponse();
            return (CancelSubscriptionResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/extendExpiry", method = RequestMethod.POST)
    @ResponseBody
    public ExtendExpiryResponse extendExpiry(@RequestBody ExtendExpiryRequest request){
        try {
            return subscriptionService.extendExpiry(request);
        }catch (Exception e){
            LOG.error("Exception in extendTrial: ", e);
            ExtendExpiryResponse response = new ExtendExpiryResponse();
            return (ExtendExpiryResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/checkEligibility", method = RequestMethod.GET)
    @ResponseBody
    public GenericResponse checkEligibility(@RequestBody CheckEligibilityRequest request){
        try {
            return subscriptionService.checkEligibility(request);
        }catch (Exception e){
            LOG.error("Exception in extendTrial: ", e);
            GenericResponse response = new GenericResponse();
            return ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/checkStatus", method = RequestMethod.GET)
    @ResponseBody
    public CheckStatusResponse checkStatus(@RequestBody CheckStatusRequest request){
        try {
            return subscriptionService.checkStatus(request);
        }catch (Exception e){
            LOG.error("Exception in extendTrial: ", e);
            CheckStatusResponse response = new CheckStatusResponse();
            return (CheckStatusResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }
}
