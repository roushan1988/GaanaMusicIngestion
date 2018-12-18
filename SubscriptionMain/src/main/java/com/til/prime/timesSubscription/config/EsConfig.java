package com.til.prime.timesSubscription.config;

import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.net.InetAddress;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.til.prime.timesSubscription.es")
public class EsConfig {

    @Value("${es.connection.string}")
    private String EsConnectionString;

    @Value("${es.clustername}")
    private String EsClusterName;

    @Value("${es.indexname}")
    private String esIndexName;

    @Bean
    public Client client() throws Exception {
        Settings esSettings = Settings.builder()
                .put("cluster.name", EsClusterName)
                .put("client.transport.sniff", true)
                .put("client.transport.ignore_cluster_name", false)
                .build();
        TransportClient client = new PreBuiltTransportClient(esSettings);
        for(String hostPort: EsConnectionString.split(",")){
            String[] hostAndPort = hostPort.split(":");
            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostAndPort[0]), Integer.parseInt(hostAndPort[1])));
        }
        return client;
    }

    @Bean
    public ElasticsearchOperations elasticsearchTemplate() throws Exception {
        return new ElasticsearchTemplate(client());
    }

    @Bean
    public String esIndexName() {
        return esIndexName;
    }
}
