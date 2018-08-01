package com.til.prime.timesSubscription.controller;

import com.til.prime.timesSubscription.aspect.Loggable;
import com.til.prime.timesSubscription.dao.UserSubscriptionRepository;
import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.service.PropertyService;
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
@RequestMapping("/crm")
public class CRMController {

    private static final Logger LOG = Logger.getLogger(CRMController.class);
    @Autowired
    private SubscriptionService subscriptionService;


    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @Autowired
    PropertyService propertyService;
    
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
            LOG.error("Exception in blockUnblockUser: ", e);
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
    public GenericResponse updateCacheForMobile(@RequestBody GenericRequest request){
        try {
        	return subscriptionService.updateCacheForMobile(request);
        }catch (Exception e){
            LOG.error("Exception in updateCacheForMobile: ", e);
            GenericResponse response = new GenericResponse();
            return ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/server/customerSearchCRM", method = RequestMethod.POST)
    @ResponseBody
    public CustomerSearchCRMResponse customerSearchCRM(@RequestBody CustomerSearchRequest request){
        try {
        	return subscriptionService.customerSearchCRM(request);
        }catch (Exception e){
            LOG.error("Exception in customerSearchCRM: ", e);
            return null;
        }
    }
    
    
    @Loggable
    @RequestMapping(path="/server/customerDetailsCRM", method = RequestMethod.POST)
    @ResponseBody
    public CustomerDetailsCRMResponse customerDetailsCRM(@RequestBody CustomerSearchRequest request){
        try {
        	return subscriptionService.customerDetailsCRM(request);
        }catch (Exception e){
            LOG.error("Exception in customerDetailsCRM: ", e);
            return null;
        }
    }
 
    
    @Loggable
    @RequestMapping(path="/server/orderDetailsCRM", method = RequestMethod.POST)
    @ResponseBody
    public OrderDetailsCRMResponse orderDetailsCRM(@RequestBody OrderDetailsRequest request){
        try {

        	return subscriptionService.getOrderDetailsCRM(request);
		} catch (Exception e) {
            LOG.error("Exception in orderDetailsCRM: ", e);
            OrderDetailsCRMResponse response = new OrderDetailsCRMResponse();
            return (OrderDetailsCRMResponse) ResponseUtil.createExceptionResponse(response, 10);
		}
    }
    	    
	@Loggable
	@RequestMapping(path="/server/orderSearchCRM", method = RequestMethod.POST)
	@ResponseBody
	public OrderSearchCRMResponse orderSearchCRM(@RequestBody OrderSearchRequest orderSearchRequest){
		OrderSearchCRMResponse orderSearchCRMResponse = null;
		
		try {
	    	orderSearchCRMResponse = subscriptionService.orderSearchCRM(orderSearchRequest);
	    	return orderSearchCRMResponse;
		} catch (Exception e) {
	        LOG.error("Exception in orderDetailsCRM: ", e);
	        OrderSearchCRMResponse response = new OrderSearchCRMResponse();
            return (OrderSearchCRMResponse) ResponseUtil.createExceptionResponse(response, 10);
		}
	}
	 

    @Loggable
    @RequestMapping(path="/server/renewSubscription", method = RequestMethod.POST)
    @ResponseBody
    public InitPurchaseResponse renewSubscription(@RequestBody CRMInitPurchaseRequest request){
        try {

        	return subscriptionService.initPurchasePlan(request, true, request.isFree());
        } catch (Exception e) {
            LOG.error("Exception in renewSubscription: ", e);
            InitPurchaseResponse response = new InitPurchaseResponse();
            return (InitPurchaseResponse) ResponseUtil.createExceptionResponse(response, 10);
		}
    }


    @Loggable
    @RequestMapping(path="/server/getPropertyTableData", method = RequestMethod.POST)
    @ResponseBody
    public PropertyDataGetResponseCRM getPropertyTableData(@RequestBody PropertyDataRequestCRM request){
        try {

        	return subscriptionService.getPropertyTableData(request);
		} catch (Exception e) {
            LOG.error("Exception in getPropertyTableData: ", e);
            PropertyDataGetResponseCRM response = new PropertyDataGetResponseCRM();
            return (PropertyDataGetResponseCRM) ResponseUtil.createExceptionResponse(response, 10);
		}
    }

    @Loggable
    @RequestMapping(path="/server/updatePropertyTableData", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse updatePropertyTableData(@RequestBody PropertyDataUpdateRequestCRM request){
        try {

        	return subscriptionService.updatePropertyTableData(request);
		} catch (Exception e) {
            LOG.error("Exception in updatePropertyTableData: ", e);
            GenericResponse response = new GenericResponse();
            return ResponseUtil.createExceptionResponse(response, 10);
		}
    }

    @Loggable
    @RequestMapping(path="/deleteUser", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse deleteUser(@RequestBody GenericRequest request){
//        if(GlobalConstants.USER_DELETION_ALLOWED_ENVIRONMENTS.contains(System.getProperty(GlobalConstants.ENVIRONMENT))){
        try {
            return subscriptionService.deleteUser(request);
        } catch (Exception e) {
            LOG.error("Exception in deleteUser: ", e);
            GenericResponse response = new GenericResponse();
            return ResponseUtil.createExceptionResponse(response, 10);
        }
//        }
//        return ResponseUtil.createExceptionResponse(new GenericResponse(), 10);
    }
    
}
