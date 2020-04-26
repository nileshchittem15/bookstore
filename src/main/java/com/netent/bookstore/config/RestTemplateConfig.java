package com.netent.bookstore.config;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Configuration
public class RestTemplateConfig {

    @Value("${httpClient.connection.pool.size}")
    private String poolMaxTotal;

    @Value("${httpClientFactory.connection.timeout}")
    private int connectionTimeOut;

    @Value("${httpClientFactory.read.timeout}")
    private int readTimeOut;


    @Bean
    public RestTemplate restTemplate() {
        RestTemplate template = new RestTemplate(httpRequestFactory());
        List<HttpMessageConverter<?>> messageConverters = template.getMessageConverters();
        messageConverters.add(new FormHttpMessageConverter());
        template.setMessageConverters(messageConverters);
        return template;
    }

    private ClientHttpRequestFactory httpRequestFactory() {
        HttpComponentsClientHttpRequestFactory factory =
            new HttpComponentsClientHttpRequestFactory(httpClient());
        factory.setConnectTimeout(connectionTimeOut);
        factory.setReadTimeout(readTimeOut);
        return factory;
    }

    private HttpClient httpClient() {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(Integer.parseInt(poolMaxTotal));
        return HttpClientBuilder.create().setConnectionManager(connectionManager).build();
    }
}
