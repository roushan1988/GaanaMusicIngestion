package com.til.prime.timesSubscription.config;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dto.external.PublishedUserStatusDTO;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.plain.PlainSaslServer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConsumerAwareErrorHandler;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConsumerConfig {
    private static final Logger LOG = Logger.getLogger(KafkaConsumerConfig.class);


    @Value("${kafka.brokers}")
    private String kafkaBrokers;

    @Value("${kafka.jaas.config}")
    private String jaasConfig;

    @Bean
    public ConsumerFactory<String, PublishedUserStatusDTO> consumerFactory() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaBrokers);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "test_consumer5");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, GlobalConstants.SASL_PLAINTEXT);
        props.put(SaslConfigs.SASL_MECHANISM, PlainSaslServer.PLAIN_MECHANISM);
        props.put(SaslConfigs.SASL_JAAS_CONFIG, jaasConfig);
        return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), new JsonDeserializer<>(PublishedUserStatusDTO.class));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PublishedUserStatusDTO> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, PublishedUserStatusDTO> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setPollTimeout(30000);
        factory.getContainerProperties().setErrorHandler(errorHandlerConsumer());
//        factory.setRetryTemplate(getRetryTemplate());
        return factory;
    }

//    @Bean
//    public RetryPolicy getRetryPolicy(){
//        SimpleRetryPolicy simpleRetryPolicy = new SimpleRetryPolicy();
//        simpleRetryPolicy.setMaxAttempts(10);
//        return simpleRetryPolicy;
//    }
//
//    @Bean
//    public FixedBackOffPolicy getBackOffPolicy() {
//        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
//        backOffPolicy.setBackOffPeriod(1000);
//        return backOffPolicy;
//    }
//
//    @Bean
//    public RetryTemplate getRetryTemplate(){
//        RetryTemplate retryTemplate = new RetryTemplate();
//        retryTemplate.setRetryPolicy(getRetryPolicy());
//        retryTemplate.setBackOffPolicy(getBackOffPolicy());
//        return retryTemplate;
//    }

    @Bean
    ConsumerAwareErrorHandler errorHandlerConsumer(){
        return new ConsumerAwareErrorHandler() {
            @Override
            public void handle(Exception e, ConsumerRecord<?, ?> consumerRecord, Consumer<?, ?> consumer) {
                LOG.error("", e);
            }
        };
    }
}

