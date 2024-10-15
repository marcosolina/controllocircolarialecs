package com.alecs.controllocircolariscuola.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import com.alecs.controllocircolariscuola.services.implementations.RestClientImpl;
import com.alecs.controllocircolariscuola.services.interfaces.RestClientUtils;

import reactor.netty.http.client.HttpClient;

@Configuration
public class Beans {

    @Bean
    public RestClientUtils getBuilder(){
        WebClient.Builder builder = WebClient.builder()
                .clientConnector(new ReactorClientHttpConnector(HttpClient.create().followRedirect(true)));
        
        return new RestClientImpl(builder);
    }
}
