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
        if (StringUtils.isNotEmpty(user.getEmail())) {
            EmailTask emailTask = helper.getUserMobileUpdateEmailTask(user, relevantSubscriptions);
            queueService.pushToEmailQueue(emailTask);
        }
    }

    @Override
    public void sendUserCreationWithExistingMobileCommunication(UserModel user, List<UserSubscriptionModel> relevantSubscriptions) {

    }

    @Override
    public void sendSubscriptionExpiredCommunication(UserSubscriptionModel userSubscription) {
        //TODO Paid Subscription Expired When Auto-debit off IDENTIFIED
        SMSTask smsTask = helper.getSubscriptionExpiredSMSTask(userSubscription);
        queueService.pushToSMSQueue(smsTask);

        if (StringUtils.isNotEmpty(userSubscription.getUser().getEmail())) {
            EmailTask emailTask = helper.getSubscriptionExpiredEmailTask(userSubscription);
            queueService.pushToEmailQueue(emailTask);
        }
    }

    @Override
    public void sendSubscriptionExpiryExtensionCommunication(UserSubscriptionModel userSubscription) {
        //TODO Subsciption Extended
        SMSTask smsTask = helper.getSubscriptionExtensionSMSTask(userSubscription);
        queueService.pushToSMSQueue(smsTask);

        if (StringUtils.isNotEmpty(userSubscription.getUser().getEmail())) {
            EmailTask emailTask = helper.getSubscriptionExtensionEmailTask(userSubscription);
            queueService.pushToEmailQueue(emailTask);

        }
    }

    @Override
    public void sendSubscriptionCancellationCommunication(UserSubscriptionModel userSubscription) {

        SMSTask smsTask = helper.getCancelSubscriptionSMSTask(userSubscription);
        queueService.pushToSMSQueue(smsTask);

        if (StringUtils.isNotEmpty(userSubscription.getUser().getEmail())) {
            EmailTask emailTask = helper.getCancelSubscriptionEmailTask(userSubscription);
            queueService.pushToEmailQueue(emailTask);
        }
    }

    @Override
    public void sendSubscriptionActiveCommunication(UserSubscriptionModel userSubscription) {

    }

    @Override
    public void sendFreeTrialSubscriptionSuccessCommunication(UserSubscriptionModel userSubscription) {
        SMSTask smsTask = helper.getFreeTrialSubscriptionSMSTask(userSubscription);
        queueService.pushToSMSQueue(smsTask);

        if (StringUtils.isNotEmpty(userSubscription.getUser().getEmail())) {
            EmailTask emailTask = helper.getFreeTrialSubscriptionEmailTask(userSubscription);
            queueService.pushToEmailQueue(emailTask);
        }

    }

    @Override
    public void sendPaidSubscriptionSuccessCommunication(UserSubscriptionModel userSubscription) {
        SMSTask smsTask = helper.getFirstTimePurchaseSMSTask(userSubscription);
        queueService.pushToSMSQueue(smsTask);

        if (StringUtils.isNotEmpty(userSubscription.getUser().getEmail())) {
            EmailTask emailTask = helper.getFirstTimePurchaseEmailTask(userSubscription);
            queueService.pushToEmailQueue(emailTask);
        }

    }

    @Override
    public void sendExistingSubscriptionActivationCommunication(UserSubscriptionModel userSubscription) {
//        SMSTask smsTask = helper.getExistingSubsActivationSMSTask(userSubscription);
//        queueService.pushToSMSQueue(smsTask);
//
//        if (StringUtils.isNotEmpty(userSubscription.getUser().getEmail())) {
//            EmailTask emailTask = helper.getExistingSubsActivationEmailTask(userSubscription);
//            queueService.pushToEmailQueue(emailTask);
//        }

    }

    @Override
    public void sendBackendActivationPendingCommunication(BackendSubscriptionUserModel model) {
        SMSTask smsTask = helper.getBackendActivationSMSTask(model);
        queueService.pushToSMSQueue(smsTask);
        if (StringUtils.isNotEmpty(model.getEmail())) {
            EmailTask emailTask = helper.getBackendActivationEmailTask(model);
            queueService.pushToEmailQueue(emailTask);
        }
    }

    @Override
    public void sendSubscriptionRenewedCommunication(UserSubscriptionModel userSubscription) {
        SMSTask smsTask = helper.getSubscriptionRenewedSMSTask(userSubscription);
        queueService.pushToSMSQueue(smsTask);

        if (StringUtils.isNotEmpty(userSubscription.getUser().getEmail())) {
            EmailTask emailTask = helper.getSubscriptionRenewedEmailTask(userSubscription);
            queueService.pushToEmailQueue(emailTask);
        }
    }

    @Override
    public void sendFreeTrailExpiryReminderCommunication(UserSubscriptionModel userSubscription, Long days) {
        SMSTask smsTask = helper.getFreeTrialExpiryReminderSMSTask(userSubscription, days);
        queueService.pushToSMSQueue(smsTask);

        if (StringUtils.isNotEmpty(userSubscription.getUser().getEmail())) {
            EmailTask emailTask = helper.getFreeTrialExpiryReminderEmailTask(userSubscription, days);
            queueService.pushToEmailQueue(emailTask);
        }
    }

    @Override
    public void sendFreeTrailExpiredCommunication(UserSubscriptionModel userSubscription) {
        SMSTask smsTask = helper.getFreeTrialExpiredSMSTask(userSubscription);
        queueService.pushToSMSQueue(smsTask);

        if (StringUtils.isNotEmpty(userSubscription.getUser().getEmail())) {
            EmailTask emailTask = helper.getFreeTrialExpiredEmailTask(userSubscription);
            queueService.pushToEmailQueue(emailTask);
        }
    }


    @Override
    public void sendSubscriptionRenewalReminderAutoDebitOnCommunication(UserSubscriptionModel userSubscription) {
        SMSTask smsTask = helper.getSubscriptionRenewalReminderSMSTask(userSubscription, true);
        queueService.pushToSMSQueue(smsTask);

        if (StringUtils.isNotEmpty(userSubscription.getUser().getEmail())) {
            EmailTask emailTask = helper.getSubscriptionRenewalReminderEmailTask(userSubscription, true);
            queueService.pushToEmailQueue(emailTask);
        }
    }

    @Override
    public void sendSubscriptionRenewalReminderAutoDebitOffCommunication(UserSubscriptionModel userSubscription) {
        SMSTask smsTask = helper.getSubscriptionRenewalReminderSMSTask(userSubscription, false);
        queueService.pushToSMSQueue(smsTask);

        if (StringUtils.isNotEmpty(userSubscription.getUser().getEmail())) {
            EmailTask emailTask = helper.getSubscriptionRenewalReminderEmailTask(userSubscription, false);
            queueService.pushToEmailQueue(emailTask);
        }
    }
}
