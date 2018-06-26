package com.til.prime.timesSubscription.service.impl;

import com.google.gson.Gson;
import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dto.external.EmailTask;
import com.til.prime.timesSubscription.dto.external.SMSTask;
import com.til.prime.timesSubscription.service.QueueService;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Properties;

@Service
public class QueueServiceImpl implements QueueService {
    private static final Logger LOG = Logger.getLogger(QueueServiceImpl.class);
    private static final Gson gson = new Gson();
    private static final MessageProperties messageProperties;
    static {
        messageProperties = new MessageProperties();
        messageProperties.setContentType(MessageProperties.CONTENT_TYPE_JSON);
    }
    @Resource(name = "config_properties")
    private Properties properties;
    @Autowired
    private AmqpTemplate amqpTemplateSMS;
    @Autowired
    private AmqpTemplate amqpTemplateEmail;

//    @PostConstruct
    public void test() throws Exception{
        while (true) {
//            SMSTask smsTask = new SMSTask();
//            smsTask.setMobileNumber("9880252944");
//            smsTask.setPartnerId("TimesSubscription");
//            smsTask.setTemplateKey("otpSmsKey");
//            Map<String, String> map = Maps.newHashMap();
//            map.put("otp", "9876");
//            map.put("appId", "TimesSubscription");
//            map.put("timeStamp", "Thu 12:30PM");
//            smsTask.setContext(map);
//            smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
//            pushToSMSQueue(smsTask);
//            EmailTask emailTask = new EmailTask();
//            emailTask.setTemplateKey("USER_MOBILE_INVALIDATION_EMAIL");
//            emailTask.setPartnerId("TimesSubscription");
//            emailTask.setGroup("test");
//            emailTask.setEmailId("roushan1988@gmail.com");
//            emailTask.setFromName("TimesPrime");
//            emailTask.setFromEmail("info@timesprime.com");
//            Map<String, String> map2 = Maps.newHashMap();
//            map2.put("otp", "9999");
//            map2.put("appId", "timesprime");
//            map2.put("timeStamp", "Thu 14:30PM");
//            map2.put("content", "testing");
//            emailTask.setContext(map2);
//            emailTask.setCtaKey("link1");
//            emailTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
//            pushToEmailQueue(emailTask);
            Thread.sleep(10000);
        }
    }

    @Override
    @Async
    public void pushToSMSQueue(SMSTask smsTask) {
        Message message = null;
        try {
            message = new Message(gson.toJson(smsTask).getBytes(GlobalConstants.UTF_8), messageProperties);
            amqpTemplateSMS.send(properties.getProperty(GlobalConstants.SMS_ROUTING_KEY), message);
            LOG.info("SMSTask pushed into queue: "+smsTask);
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
    }

    @Override
    @Async
    public void pushToEmailQueue(EmailTask emailTask) {
        Message message = null;
        try {
            message = new Message(gson.toJson(emailTask).getBytes(GlobalConstants.UTF_8), messageProperties);
            amqpTemplateEmail.send(properties.getProperty(GlobalConstants.EMAIL_ROUTING_KEY), message);
            LOG.info("EMailTask pushed into queue: "+emailTask);
        } catch (Exception e) {
            LOG.error("Exception", e);
        }
    }
}
