package com.til.prime.timesSubscription.service;

import com.til.prime.timesSubscription.dto.external.EmailTask;
import com.til.prime.timesSubscription.dto.external.SMSTask;

public interface QueueService {
    void pushToSMSQueue(SMSTask smsTask);
    void pushToEmailQueue(EmailTask emailTask);
}
