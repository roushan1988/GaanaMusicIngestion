package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.service.QueueService;
import org.apache.log4j.Logger;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
public class QueueServiceImpl implements QueueService {
    private static final Logger LOG = Logger.getLogger(QueueServiceImpl.class);
    @Autowired
    private AmqpTemplate template;

    @PostConstruct
    public void test(){
        pushToQueue(null);
    }

    @Override
    public void pushToQueue(Object object) {
        int messagCount = 0;
        while (messagCount < 10){
            template.convertAndSend("tp.routingkey.1", "Message # " + messagCount++);
        }
        System.out.println( messagCount + " message(s) sent successfully.");
    }
}
