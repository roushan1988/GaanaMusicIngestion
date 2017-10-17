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
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
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
//            smsTask.setPartnerId("timesprime");
//            smsTask.setTemplateKey("testmessage");
//            Map<String, String> map = Maps.newHashMap();
//            map.put("otp", "9876");
//            map.put("appId", "timesprime");
//            map.put("timeStamp", "Thu 12:30PM");
//            smsTask.setContext(map);
//            smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
//            pushToSMSQueue(smsTask);
//            EmailTask emailTask = new EmailTask();
//            emailTask.setTemplateKey("testemail3");
//            emailTask.setPartnerId("timesprime");
//            emailTask.setGroup("test");
//            emailTask.setEmailId("roushan1988@gmail.com");
//            emailTask.setFromName("TimesPrime");
//            emailTask.setFromEmail("roushan.singh1@timesinternet.in");
//            Map<String, String> map2 = Maps.newHashMap();
//            map2.put("otp", "9999");
//            map2.put("appId", "timesprime");
//            map2.put("timeStamp", "Thu 14:30PM");
//            emailTask.setContext(map2);
//            emailTask.setCtaKey("link1");
//            emailTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
//            pushToEmailQueue(emailTask);
//            Thread.sleep(30000);
        }
    }

    @Override
    public void pushToSMSQueue(SMSTask smsTask) {
        Message message = null;
        try {
            message = new Message(gson.toJson(smsTask).getBytes(GlobalConstants.UTF_8), messageProperties);
            amqpTemplateSMS.send(properties.getProperty(GlobalConstants.SMS_ROUTING_KEY), message);
            LOG.info("SMSTask pushed into queue: "+smsTask);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void pushToEmailQueue(EmailTask emailTask) {
        Message message = null;
        try {
            message = new Message(gson.toJson(emailTask).getBytes(GlobalConstants.UTF_8), messageProperties);
            amqpTemplateEmail.send(properties.getProperty(GlobalConstants.EMAIL_ROUTING_KEY), message);
            LOG.info("EMailTask pushed into queue: "+emailTask);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
