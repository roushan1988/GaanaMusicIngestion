package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.dto.external.EmailTask;
import com.til.prime.timesSubscription.dto.external.SMSTask;
import com.til.prime.timesSubscription.service.QueueService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Properties;

@Service
public class QueueServiceImpl implements QueueService {
    private static final Logger LOG = Logger.getLogger(QueueServiceImpl.class);
    @Resource(name = "config_properties")
    private Properties properties;
    @Value("${kafka.sms.topic}")
    private String kafkaSMSTopic;
    @Value("${kafka.email.topic}")
    private String kafkaEmailTopic;

    @Autowired
    private KafkaTemplate<String, SMSTask> kafkaCommSMSTemplate;
    @Autowired
    private KafkaTemplate<String, EmailTask> kafkaCommEmailTemplate;

//    @PostConstruct
//    public void test() throws Exception{
//        while (true) {
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
//            Thread.sleep(10000);
//        }
//    }

    @Override
    @Async
    public void pushToSMSQueue(SMSTask smsTask) {
        try{
            kafkaCommSMSTemplate.send(kafkaSMSTopic, smsTask).get();
            LOG.info("Message published, topic: "+kafkaSMSTopic+", message: "+smsTask);
        }catch (Exception e){
            LOG.error("Exception while publishing message: "+smsTask, e);
        }
    }

    @Override
    @Async
    public void pushToEmailQueue(EmailTask emailTask) {
        try{
            kafkaCommEmailTemplate.send(kafkaEmailTopic, emailTask).get();
            LOG.info("Message published, topic: "+ kafkaEmailTopic +", message: "+emailTask);
        }catch (Exception e){
            LOG.error("Exception while publishing message: "+emailTask, e);
        }
    }
}
