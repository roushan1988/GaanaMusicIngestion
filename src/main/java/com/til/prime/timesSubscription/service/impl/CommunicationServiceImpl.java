package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.dto.external.EmailTask;
import com.til.prime.timesSubscription.dto.external.SMSTask;
import com.til.prime.timesSubscription.model.BackendSubscriptionUserModel;
import com.til.prime.timesSubscription.model.UserModel;
import com.til.prime.timesSubscription.model.UserSubscriptionModel;
import com.til.prime.timesSubscription.service.CommunicationService;
import com.til.prime.timesSubscription.service.CommunicationServiceHelper;
import com.til.prime.timesSubscription.service.QueueService;
import org.apache.commons.lang3.StringUtils;
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
        if(StringUtils.isNotEmpty(user.getEmail())){
            EmailTask emailTask = helper.getUserMobileUpdateEmailTask(user, relevantSubscriptions);
            queueService.pushToEmailQueue(emailTask);
        }
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

    @Override
    public void sendBackendActivationPendingCommunication(BackendSubscriptionUserModel model) {
        SMSTask smsTask = helper.getBackendActivationSMSTask(model);
        queueService.pushToSMSQueue(smsTask);
        if(StringUtils.isNotEmpty(model.getEmail())){
            EmailTask emailTask = helper.getBackendActivationEmailTask(model);
            queueService.pushToEmailQueue(emailTask);
        }
    }
}
