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
    @RequestMapping(path="/server/cancelSubscription", method = RequestMethod.POST)
    @ResponseBody
    public CancelSubscriptionResponse cancelSubscriptionViaServer(@RequestBody CancelSubscriptionServerRequest request){
        try {
            return subscriptionService.cancelSubscription(request, true);
        }catch (Exception e){
            LOG.error("Exception in cancelSubscription: ", e);
            CancelSubscriptionResponse response = new CancelSubscriptionResponse();
            return (CancelSubscriptionResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/server/offerSubscription", method = RequestMethod.POST)
    @ResponseBody
    public InitPurchaseResponse offerSubscriptionViaServer(@RequestBody InitPurchaseRequest request){
        try {
            return subscriptionService.initPurchasePlan(request);
        }catch (Exception e){
            LOG.error("Exception in offerSubscriptionViaServer: ", e);
            InitPurchaseResponse response = new InitPurchaseResponse();
            return (InitPurchaseResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/app/cancelSubscription", method = RequestMethod.POST)
    @ResponseBody
    public CancelSubscriptionResponse cancelSubscriptionViaApp(@RequestBody CancelSubscriptionRequest request){
        try {
            return subscriptionService.cancelSubscription(request, false);
        }catch (Exception e){
            LOG.error("Exception in cancelSubscription: ", e);
            CancelSubscriptionResponse response = new CancelSubscriptionResponse();
            return (CancelSubscriptionResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/server/turnOffAutoDebit", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse turnOffAutoDebitViaServer(@RequestBody TurnOffAutoDebitRequest request){
        try {
            return subscriptionService.turnOffAutoDebit(request);
        }catch (Exception e){
            LOG.error("Exception in turnOffAutoDebitViaServer: ", e);
            GenericResponse response = new GenericResponse();
            return ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/server/blockUnblockUser", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse blockUnblockUser(@RequestBody BlockUnblockRequest request){
        try {
            return subscriptionService.blockUnblockUser(request);
        }catch (Exception e){
            LOG.error("Exception in blockUser: ", e);
            GenericResponse response = new GenericResponse();
            return ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/server/extendExpiry", method = RequestMethod.POST)
    @ResponseBody
    public ExtendExpiryResponse extendExpiry(@RequestBody ExtendExpiryRequest request){
        try {
            return subscriptionService.extendExpiry(request);
        }catch (Exception e){
            LOG.error("Exception in extendExpiry: ", e);
            ExtendExpiryResponse response = new ExtendExpiryResponse();
            return (ExtendExpiryResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/checkEligibility", method = RequestMethod.GET)
    @ResponseBody
    public GenericValidationResponse checkEligibility(@RequestBody CheckEligibilityRequest request){
        try {
            return subscriptionService.checkEligibility(request);
        }catch (Exception e){
            LOG.error("Exception in checkEligibility: ", e);
            GenericValidationResponse response = new GenericValidationResponse();
            return (GenericValidationResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/server/checkValidVariant", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public GenericValidationResponse checkValidVariant(@RequestBody CheckValidVariantRequest request){
        try {
            return subscriptionService.checkValidVariant(request);
        }catch (Exception e){
            LOG.error("Exception in checkValidVariant: ", e);
            GenericValidationResponse response = new GenericValidationResponse();
            return (GenericValidationResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/app/checkStatus", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public CheckStatusResponse checkStatusViaApp(@RequestBody CheckStatusRequest request){
        try {
            return subscriptionService.checkStatusViaApp(request);
        }catch (Exception e){
            LOG.error("Exception in checkStatus: ", e);
            CheckStatusResponse response = new CheckStatusResponse();
            return (CheckStatusResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/server/checkStatus", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public CheckStatusResponse checkStatusViaServer(@RequestBody CheckStatusRequest request){
        try {
            return subscriptionService.checkStatusViaServer(request, false);
        }catch (Exception e){
            LOG.error("Exception in checkStatus: ", e);
            CheckStatusResponse response = new CheckStatusResponse();
            return (CheckStatusResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/external/checkStatus", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public CheckStatusResponse checkExternalStatusViaServer(@RequestBody CheckStatusRequest request){
        try {
            return subscriptionService.checkStatusViaServer(request, true);
        }catch (Exception e){
            LOG.error("Exception in checkStatus: ", e);
            CheckStatusResponse response = new CheckStatusResponse();
            return (CheckStatusResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @RequestMapping(value = "/getServerStatus", method = RequestMethod.GET)
    @ResponseBody
    public String getServerStatus(){
        return "OK";
    }
}
