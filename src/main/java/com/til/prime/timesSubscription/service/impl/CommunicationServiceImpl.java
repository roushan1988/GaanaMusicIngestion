package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.dto.external.EmailTask;
import com.til.prime.timesSubscription.dto.external.SMSTask;
import com.til.prime.timesSubscription.model.UserModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import com.til.prime.timesSubscription.service.CommunicationService;
import com.til.prime.timesSubscription.service.CommunicationServiceHelper;
import com.til.prime.timesSubscription.service.QueueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommunicationServiceImpl implements CommunicationService {
    @Autowired
    private QueueService queueService;
    @Autowired
    private CommunicationServiceHelper helper;

    @Override
    public void sendUserSuspensionCommunication(UserModel user, List<UserSubscriptionModel> relevantSubscriptions) {
        EmailTask emailTask = helper.getUserMobileUpdateEmailTask(user, relevantSubscriptions);
        queueService.pushToEmailQueue(emailTask);
    }

    @Override
    public void sendUserCreationWithExistingMobileCommunication(UserModel user, List<UserSubscriptionModel> relevantSubscriptions) {

    }

    @Override
    public void sendSubscriptionExpiryCommunication(UserSubscriptionModel userSubscription) {

    }

    @Override
    public void sendSubscriptionExpiryExtensionCommunication(UserSubscriptionModel userSubscription) {

    }

    @Override
    public void sendSubscriptionCancellationCommunication(UserSubscriptionModel userSubscription) {

    }

    @Override
    public void sendSubscriptionActiveCommunication(UserSubscriptionModel userSubscription) {

    }

    @Override
    public void sendSubscriptionSuccessCommunication(UserSubscriptionModel userSubscription) {
        SMSTask smsTask = helper.getSubscriptionSuccessSMSTask(userSubscription);
        queueService.pushToSMSQueue(smsTask);
        EmailTask emailTask = helper.getSubscriptionSuccessEmailTask(userSubscription);
        queueService.pushToEmailQueue(emailTask);
    }

//    @Autowired
//    private UserSubscriptionRepository repository;
//    @PostConstruct
//    public void test(){
//        UserSubscriptionModel userSubscription = repository.findById(23l);
//        sendSubscriptionSuccessCommunication(userSubscription);
//    }
}
