//package com.application.config;
//
//import co.elastic.clients.elasticsearch.ElasticsearchClient;
//import co.elastic.clients.json.jackson.JacksonJsonpMapper;
//import co.elastic.clients.transport.ElasticsearchTransport;
//import co.elastic.clients.transport.rest_client.RestClientTransport;
//import jakarta.annotation.PostConstruct;
//import org.apache.http.HttpHost;
//import org.elasticsearch.client.RestClient;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//@Service
//public class Elasticsearch {
//
//    private ElasticsearchClient esClient;
//
//    @Value("${spring.elasticsearch.rest.uris}")
//    private String serverUrl;
//    private RestClient restClient;
//    private ElasticsearchTransport transport;
//
//    @PostConstruct
//    public void init() {
//        if (serverUrl == null || serverUrl.isEmpty()) {
//            throw new IllegalArgumentException("HTTP Host may not be null or empty");
//        }
//
//        // Create the low-level client
//        restClient = RestClient.builder(HttpHost.create(serverUrl)).build();
//
//        // Create the transport with a Jackson mapper
//        transport = new RestClientTransport(restClient, new JacksonJsonpMapper());
//
//        // Create the API client
//        esClient = new ElasticsearchClient(transport);
//    }
//
//    // Provide a method to access the ElasticsearchClient
//    public ElasticsearchClient getEsClient() {
//        return esClient;
//    }
//}
