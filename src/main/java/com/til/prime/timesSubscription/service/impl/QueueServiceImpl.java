package com.til.prime.timesSubscription.service.impl;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dto.external.EmailTask;
import com.til.prime.timesSubscription.dto.external.SMSTask;
import com.til.prime.timesSubscription.enums.TaskPriorityEnum;
import com.til.prime.timesSubscription.service.QueueService;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.Map;
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
        SMSTask smsTask = new SMSTask();
        smsTask.setMobileNumber("9880252944");
        smsTask.setPartnerId("smartauth");
        smsTask.setTemplateKey("otpSmsKey");
        Map<String, String> map = Maps.newHashMap();
        map.put("otp", "1234");
        map.put("appId", "TimesPrime");
        map.put("timeStamp", "Thu 12:30PM");
        smsTask.setContext(map);
        smsTask.setTaskPriority(TaskPriorityEnum.HIGH_PRIORITY);
        pushToSMSQueue(smsTask);
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
