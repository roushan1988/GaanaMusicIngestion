package com.til.prime.timesSubscription.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.til.prime.timesSubscription.aspect.Loggable;
import com.til.prime.timesSubscription.dao.UserSubscriptionRepository;
import com.til.prime.timesSubscription.dto.external.BlockUnblockRequest;
import com.til.prime.timesSubscription.dto.external.CRMInitPurchaseRequest;
import com.til.prime.timesSubscription.dto.external.CancelSubscriptionResponse;
import com.til.prime.timesSubscription.dto.external.CancelSubscriptionServerRequest;
import com.til.prime.timesSubscription.dto.external.CustomerCRM;
import com.til.prime.timesSubscription.dto.external.CustomerSearchDTOs;
import com.til.prime.timesSubscription.dto.external.CustomerSearchRequest;
import com.til.prime.timesSubscription.dto.external.ExtendExpiryRequest;
import com.til.prime.timesSubscription.dto.external.ExtendExpiryResponse;
import com.til.prime.timesSubscription.dto.external.GenericResponse;
import com.til.prime.timesSubscription.dto.external.InitPurchaseResponse;
import com.til.prime.timesSubscription.dto.external.OrderDetailsRequest;
import com.til.prime.timesSubscription.dto.external.OrderSearchRequest;
import com.til.prime.timesSubscription.dto.external.PurchaseHistoryRequest;
import com.til.prime.timesSubscription.dto.external.PurchaseHistoryResponse;
import com.til.prime.timesSubscription.dto.external.UpdateCacheForMobileRequest;
import com.til.prime.timesSubscription.service.SubscriptionService;
import com.til.prime.timesSubscription.util.ResponseUtil;

@Controller
@RequestMapping("/crm")
public class CRMController {

    private static final Logger LOG = Logger.getLogger(CRMController.class);
    @Autowired
    private SubscriptionService subscriptionService;


    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

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
    public InitPurchaseResponse offerSubscriptionViaServer(@RequestBody CRMInitPurchaseRequest request){
        try {
            return subscriptionService.initPurchasePlan(request, true, request.isFree());
        }catch (Exception e){
            LOG.error("Exception in offerSubscriptionViaServer: ", e);
            InitPurchaseResponse response = new InitPurchaseResponse();
            return (InitPurchaseResponse) ResponseUtil.createExceptionResponse(response, 10);
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
    	ExtendExpiryResponse extendExpiryResponse = null;
        try {
            extendExpiryResponse = subscriptionService.extendExpiry(request);
            return extendExpiryResponse;
        }catch (Exception e){
            LOG.error("Exception in extendExpiry: ", e);
            ExtendExpiryResponse response = new ExtendExpiryResponse();
            return (ExtendExpiryResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/server/updateCacheForMobile", method = RequestMethod.POST)
    @ResponseBody
    public String updateCacheForMobile(@RequestBody UpdateCacheForMobileRequest request){
        try {
        	return subscriptionService.updateCacheForMobile(request);
        }catch (Exception e){
            LOG.error("Exception in updateCacheForMobile: ", e);
            return "FAILED";
        }
    }

    
    @Loggable
    @RequestMapping(path="/server/customerSearchCRM", method = RequestMethod.POST)
    @ResponseBody
    public String customerSearchCRM(@RequestBody CustomerSearchRequest request){
        try {
        	CustomerSearchDTOs customerSearchDTOs = subscriptionService.customerSearchCRM(request);
        	return new Gson().toJson(customerSearchDTOs);
        }catch (Exception e){
            LOG.error("Exception in customerSearchCRM: ", e);
            return null;
        }
    }
    
    
    @Loggable
    @RequestMapping(path="/server/customerDetailsCRM", method = RequestMethod.POST)
    @ResponseBody
    public String customerDetailsCRM(@RequestBody CustomerSearchRequest request){
        try {
        	CustomerCRM customerCRM = subscriptionService.customerDetailsCRM(request);
        	return new Gson().toJson(customerCRM);
        }catch (Exception e){
            LOG.error("Exception in customerDetailsCRM: ", e);
            return null;
        }
    }
 
    
    @Loggable
    @RequestMapping(path="/server/orderDetailsCRM", method = RequestMethod.POST)
    @ResponseBody
    public String orderDetailsCRM(@RequestBody OrderDetailsRequest request){
        try {

        	return new Gson().toJson(subscriptionService.getOrderDetailsCRM(request));
		} catch (Exception e) {
            LOG.error("Exception in customerDetailsCRM: ", e);
            return null;
		}
    }
    

    @Loggable
    @RequestMapping(path="/server/orderSearchCRM", method = RequestMethod.POST)
    @ResponseBody
    public String orderSearchCRM(@RequestBody OrderSearchRequest request){
        try {

        	return new Gson().toJson(subscriptionService.orderSearchCRM(request));
		} catch (Exception e) {
            LOG.error("Exception in customerDetailsCRM: ", e);
            return null;
		}
    }
    
    @Loggable
    @RequestMapping(path="/server/renewSubscription", method = RequestMethod.POST)
    @ResponseBody
    public String renewSubscription(@RequestBody CRMInitPurchaseRequest request){
        try {

        	return new Gson().toJson(subscriptionService.initPurchasePlan(request, true, request.isFree()));
        } catch (Exception e) {
            LOG.error("Exception in customerDetailsCRM: ", e);
            return null;
		}
    }
}
