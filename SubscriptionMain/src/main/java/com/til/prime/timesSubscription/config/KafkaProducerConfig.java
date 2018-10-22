package com.til.prime.timesSubscription.config;

import com.til.prime.timesSubscription.constants.GlobalConstants;
import com.til.prime.timesSubscription.dto.external.EmailTask;
import com.til.prime.timesSubscription.dto.external.PublishedUserStatusDTO;
import com.til.prime.timesSubscription.dto.external.SMSTask;
import org.apache.kafka.clients.CommonClientConfigs;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.config.SaslConfigs;
import org.apache.kafka.common.security.plain.PlainSaslServer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaProducerConfig {

    @Value("${kafka.pubsub.brokers}")
    private String kafkaPubSubBrokers;

    @Value("${kafka.pubsub.jaas.config}")
    private String jaasPubSubConfig;

    @Value("${kafka.communication.brokers}")
    private String kafkaCommunicationBrokers;

    @Value("${kafka.communication.jaas.config}")
    private String jaasCommunicationConfig;

    @Bean(name="producerFactoryPubSub")
    public ProducerFactory<String, PublishedUserStatusDTO> producerFactoryPubSub() {
        Map<String, Object> configPubSubProps = new HashMap<>();
        configPubSubProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaPubSubBrokers);
        configPubSubProps.put(ProducerConfig.ACKS_CONFIG, GlobalConstants.ACK_ALL);
        configPubSubProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configPubSubProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configPubSubProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, GlobalConstants.SASL_PLAINTEXT);
        configPubSubProps.put(SaslConfigs.SASL_MECHANISM, PlainSaslServer.PLAIN_MECHANISM);
        configPubSubProps.put(SaslConfigs.SASL_JAAS_CONFIG, jaasPubSubConfig);
        return new DefaultKafkaProducerFactory<>(configPubSubProps);
    }

    private Map<String, Object> getCommunicationConfigMap(){
        Map<String, Object> configCommProps = new HashMap<>();
        configCommProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCommunicationBrokers);
        configCommProps.put(ProducerConfig.ACKS_CONFIG, GlobalConstants.ACK_ALL);
        configCommProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configCommProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        configCommProps.put(CommonClientConfigs.SECURITY_PROTOCOL_CONFIG, GlobalConstants.SASL_PLAINTEXT);
        configCommProps.put(SaslConfigs.SASL_MECHANISM, PlainSaslServer.PLAIN_MECHANISM);
        configCommProps.put(SaslConfigs.SASL_JAAS_CONFIG, jaasCommunicationConfig);
        return configCommProps;
    }

    @Bean(name="producerFactorySMSCommunication")
    public ProducerFactory<String, SMSTask> producerFactorySMSCommunication() {
        return new DefaultKafkaProducerFactory<>(getCommunicationConfigMap());
    }

    @Bean(name="producerFactoryEmailCommunication")
    public ProducerFactory<String, EmailTask> producerFactoryEmailCommunication() {
        return new DefaultKafkaProducerFactory<>(getCommunicationConfigMap());
    }

    @Bean(name="kafkaTemplatePubSub")
    public KafkaTemplate<String, PublishedUserStatusDTO> kafkaTemplatePubSub() {
        return new KafkaTemplate<>(producerFactoryPubSub());
    }

    @Bean(name="kafkaCommSMSTemplate")
    public KafkaTemplate<String, SMSTask> kafkaTemplateSMSCommunication() {
        return new KafkaTemplate<>(producerFactorySMSCommunication());
    }

    @Bean(name="kafkaCommEmailTemplate")
    public KafkaTemplate<String, EmailTask> kafkaTemplateEmailCommunication() {
        return new KafkaTemplate<>(producerFactoryEmailCommunication());
    }
}