package com.application.config;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import co.elastic.clients.util.ContentType;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.message.BasicHeader;
import org.elasticsearch.client.RestClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ElasticsearchConfig {

    @Value("${elasticsearch.host}")
    private String elasticsearchHost;
    @Value("${elasticsearch.port}")
    private String elasticsearchPort;

//    @Value("${elasticsearch.api.key:}")
//    private String elasticsearchApiKey;
//    @Bean
//    public RestClient getRestClient() {
//        return RestClient
//                .builder(new HttpHost(elasticsearchHost, Integer.parseInt(elasticsearchPort)))
//                .setDefaultHeaders(new Header[]{
//                        new BasicHeader("Content-type", "application/json")
//                })
//                .build();
//    }

    @Bean
    public RestClient getRestClient() {
        final CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials("", ""));
        return RestClient.builder(new HttpHost(elasticsearchHost, Integer.parseInt(elasticsearchPort)))
                .setHttpClientConfigCallback(httpClientBuilder -> {
                    httpClientBuilder.disableAuthCaching();
                    httpClientBuilder.setDefaultHeaders(List.of(
                            new BasicHeader(
                                    HttpHeaders.CONTENT_TYPE, ContentType.APPLICATION_JSON)));

                    httpClientBuilder
                            // * add product type
                            .addInterceptorLast((HttpResponseInterceptor)
                                    (response, context) ->
                                            response.addHeader("X-Elastic-Product", "Elasticsearch"))
                            // * add elasticsearch cloud api key
                            .addInterceptorLast((HttpResponseInterceptor)
                                    (response, context) ->
                                            response.addHeader("Authorization", "ApiKey azBxcDNKWUJNRDhhR1ZPWmU0T1Q6NDhrX29ZYndfVHRZaWJNQUpzZ0FpZw=="));

                    return httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
                }).build();
    }

    @Bean
    public ElasticsearchTransport getElasticsearchTransport() {
        return new RestClientTransport(getRestClient(), new JacksonJsonpMapper());
    }

    @Bean
    public ElasticsearchClient getElasticsearchClient() {
        return new ElasticsearchClient(getElasticsearchTransport());
    }
}