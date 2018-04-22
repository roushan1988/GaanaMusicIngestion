package com.til.prime.timesSubscription.service.impl;

import com.til.prime.timesSubscription.constants.KafkaConstants;
import com.til.prime.timesSubscription.dto.external.PublishedUserStatusDTO;
import com.til.prime.timesSubscription.service.UserStatusPublisherService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class UserStatusPublisherServiceImpl implements UserStatusPublisherService {
    private static final Logger LOG = Logger.getLogger(UserStatusPublisherServiceImpl.class);

    @Autowired
    private KafkaTemplate<String, PublishedUserStatusDTO> kafkaTemplate;

    @Override
    public boolean publishUserStatus(PublishedUserStatusDTO msg) {
        try{
            kafkaTemplate.send(KafkaConstants.USER_STATUS_TOPIC, msg);
            return true;
        }catch (Exception e){
            LOG.error("Exception while publishing message: "+msg, e);
            return false;
        }
    }

    /*Below is only sample demo code, this system is not doing any consumption of kafka messages*/
//    @KafkaListener(topics = KafkaConstants.USER_STATUS_TOPIC)
    public void listenWithHeaders(@Payload PublishedUserStatusDTO message, @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                                  @Header(KafkaHeaders.OFFSET) int offset) {
        LOG.info("Received Message: " + message + "from partition: " + partition+", offset: "+offset);
    }
}
