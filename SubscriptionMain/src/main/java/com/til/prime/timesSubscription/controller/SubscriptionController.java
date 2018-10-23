package com.til.prime.timesSubscription.controller;

import com.til.prime.timesSubscription.aspect.Loggable;
import com.til.prime.timesSubscription.dto.external.*;
import com.til.prime.timesSubscription.enums.PropertyEnum;
import com.til.prime.timesSubscription.service.PropertyService;
import com.til.prime.timesSubscription.service.SubscriptionService;
import com.til.prime.timesSubscription.util.RequestUpdateUtil;
import com.til.prime.timesSubscription.util.ResponseUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletContext;
import java.nio.file.Files;

@Controller
@RequestMapping("/til/subscription")
public class SubscriptionController {

    private static final Logger LOG = Logger.getLogger(SubscriptionController.class);
    private static final String HEALTH_CHECK_FILE_PATH = "/WEB-INF/healthCheck.txt";
    @Autowired
    private SubscriptionService subscriptionService;
    @Autowired
    PropertyService propertyService;
    @Autowired
    ServletContext servletContext;

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
    @RequestMapping(path="/internal/getPlanDetailsByVariant", method = RequestMethod.POST)
    @ResponseBody
    public SubscriptionPlanVariantResponse getPlanDetailsByVariant(@RequestBody PlanDetailsRequest request){
        try {
            return subscriptionService.getPlanDetailsByVariant(request, true);
        }catch (Exception e){
            LOG.error("Exception in getPlanDetailsByVariant: ", e);
            SubscriptionPlanVariantResponse response = new SubscriptionPlanVariantResponse();
            return (SubscriptionPlanVariantResponse) ResponseUtil.createExceptionResponse(response, 10);
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
    public PurchaseHistoryResponse getPurchaseHistory(@CookieValue(value = "ssoId") String ssoId,
                                                      @CookieValue(value = "ticketId") String ticketId,
                                                      @CookieValue(value = "mobile") String mobile,
                                                      @RequestParam(value = "business") String business,
                                                      @RequestParam(value = "currentSubscription", required = false) boolean currentSubscription,
                                                      @RequestParam(value = "includeDeleted", required = false) boolean includeDeleted){
        PurchaseHistoryRequest request = new PurchaseHistoryRequest();
        RequestUpdateUtil.updateRequest(request, ssoId, ticketId, mobile);
        if(StringUtils.isNotEmpty(business)){
            request.setBusiness(business);
        }
        request.setCurrentSubscription(currentSubscription);
        request.setIncludeDeleted(includeDeleted);
        try {
            return subscriptionService.getPurchaseHistory(request);
        }catch (Exception e){
            LOG.error("Exception in getPurchaseHistory: ", e);
            PurchaseHistoryResponse response = new PurchaseHistoryResponse();
            return (PurchaseHistoryResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/middle/getPurchaseHistory", method = RequestMethod.GET)
    @ResponseBody
    public PurchaseHistoryResponse getPurchaseHistoryMiddleware(@RequestParam(value = "ssoId") String ssoId,
                                                                @RequestParam(value = "ticketId") String ticketId,
                                                                @RequestParam(value = "mobile") String mobile,
                                                                @RequestParam(value = "business") String business,
                                                                @RequestParam(value = "currentSubscription", required = false) boolean currentSubscription,
                                                                @RequestParam(value = "includeDeleted", required = false) boolean includeDeleted){
        PurchaseHistoryRequest request = new PurchaseHistoryRequest();
        RequestUpdateUtil.updateRequest(request, ssoId, ticketId, mobile);
        if(StringUtils.isNotEmpty(business)){
            request.setBusiness(business);
        }
        request.setCurrentSubscription(currentSubscription);
        request.setIncludeDeleted(includeDeleted);
        try {
            return subscriptionService.getPurchaseHistory(request);
        }catch (Exception e){
            LOG.error("Exception in getPurchaseHistory: ", e);
            PurchaseHistoryResponse response = new PurchaseHistoryResponse();
            return (PurchaseHistoryResponse) ResponseUtil.createExceptionResponse(response, 10);
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
    @RequestMapping(path="/checkEligibility", method = {RequestMethod.GET, RequestMethod.POST})
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

    @Loggable
    @RequestMapping(path="/server/backendSubscription", method = RequestMethod.POST)
    @ResponseBody
    public BackendSubscriptionResponse backendSubscriptionViaServer(@RequestBody BackendSubscriptionRequest request){
        try {
            return subscriptionService.backendSubscriptionViaServer(request);
        }catch (Exception e){
            LOG.error("Exception in backendSubscriptionViaServer: ", e);
            BackendSubscriptionResponse response = new BackendSubscriptionResponse();
            return (BackendSubscriptionResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/backendSubscriptionActivation", method = {RequestMethod.GET, RequestMethod.POST})
    @ResponseBody
    public BackendSubscriptionActivationResponse backendSubscriptionActivation(@CookieValue(value = "ssoId", required = false) String ssoId,
                                                                               @CookieValue(value = "ticketId", required = false) String ticketId,
                                                                               @CookieValue(value = "mobile", required = false) String mobile,
                                                                               @RequestBody BackendSubscriptionActivationRequest request){
        try {
            RequestUpdateUtil.updateRequest(request, ssoId, ticketId, mobile);
            return subscriptionService.backendSubscriptionActivation(request);
        }catch (Exception e){
            LOG.error("Exception in backendSubscriptionActivationViaServer: ", e);
            BackendSubscriptionActivationResponse response = new BackendSubscriptionActivationResponse();
            return (BackendSubscriptionActivationResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @RequestMapping(value = "/getServerStatus", method = RequestMethod.GET)
    @ResponseBody
    public String getServerStatus() throws Exception{
        String property = new String(Files.readAllBytes((ResourceUtils.getFile(servletContext.getRealPath(HEALTH_CHECK_FILE_PATH)).toPath())));
        return (String) propertyService.getProperty(PropertyEnum.valueOf(property));
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
    @RequestMapping(path="/sso/sendOtpSms", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse sendOtpSmsForSSO(@RequestBody SsoOtpRequest request){
        try {
            return subscriptionService.sendOtpSmsForSSO(request);
        }catch (Exception e){
            LOG.error("Exception in sendOtpSmsForSSO: ", e);
            GenericResponse response = new GenericResponse();
            return ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/otp/send", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse sendOtp(@RequestBody OtpRequest request){
        try {
            return subscriptionService.sendOtp(request);
        }catch (Exception e){
            LOG.error("Exception in sendOtp: ", e);
            GenericResponse response = new GenericResponse();
            return ResponseUtil.createExceptionResponse(response, 10);
        }
    }

    @Loggable
    @RequestMapping(path="/otp/verify", method = RequestMethod.POST)
    @ResponseBody
    public OtpVerificationResponse verifyOtp(@RequestBody OtpVerificationRequest request){
        try {
            return subscriptionService.verifyOtp(request);
        }catch (Exception e){
            LOG.error("Exception in verifyOtp: ", e);
            OtpVerificationResponse response = new OtpVerificationResponse();
            return (OtpVerificationResponse) ResponseUtil.createExceptionResponse(response, 10);
        }
    }
}
