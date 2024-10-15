package com.alecs.controllocircolariscuola.services.implementations;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClient.RequestBodySpec;
import org.springframework.web.util.UriBuilder;

import com.alecs.controllocircolariscuola.services.interfaces.RestClientUtils;

import reactor.core.publisher.Mono;

public class RestClientImpl implements RestClientUtils {

    private static final Logger _LOGGER = LoggerFactory.getLogger(RestClientImpl.class);
    private WebClient.Builder builder;
    private static final String VALUE_NONE = "\n\t\t\n\t\t\n\uE000\uE001\uE002\n\t\t\t\t\n";
    private static final int size = 16 * 1024 * 1024;

    public RestClientImpl(WebClient.Builder builder) {
        this.builder = builder.codecs(codecs -> codecs.defaultCodecs().maxInMemorySize(size));
    }

    @Override
    public Builder getWebBuilder() {
        return this.builder;
    }

    @Override
    public <T> Mono<ResponseEntity<T>> performGetRequestNoExceptions(Class<T> responseBodyClass, URL url,
            Optional<Map<String, String>> headers, Optional<Map<String, String>> queryParameters) {
        return performRequestNoExceptions(responseBodyClass, HttpMethod.GET, url, headers, queryParameters, Optional.of(MediaType.APPLICATION_JSON), Optional.empty());
    }

    @Override
    public <T> Mono<ResponseEntity<T>> performRequestNoExceptions(Class<T> responseBodyClass, HttpMethod method,
            URL url, Optional<Map<String, String>> headers, Optional<Map<String, String>> queryParameters,
            Optional<MediaType> contentType, Optional<? extends Serializable> body) {
        /*
         * Create the request and adds query parameters if provided
         */
        RequestBodySpec rbs = getRequestBodySpec(method, url, headers, queryParameters, contentType);

        /*
         * Perform the call
         */
        if (body.isPresent()) {
            rbs.bodyValue(body.get());
        }
        
        return rbs.exchangeToMono(response -> {
            
            if (response.statusCode().is4xxClientError() || response.statusCode().is5xxServerError()) {
                return Mono.just(new ResponseEntity<T>(response.statusCode()));
            }

            Mono<T> monoResp = response.bodyToMono(responseBodyClass);
            return mapToEntity(response, monoResp);
        });
    }
    
    @SuppressWarnings("unchecked")
    private <T> Mono<ResponseEntity<T>> mapToEntity(ClientResponse response, Mono<T> bodyMono) {
        // @formatter:off
        return ((Mono<Object>) bodyMono).defaultIfEmpty(VALUE_NONE).map(body ->
                new ResponseEntity<>(
                        body != VALUE_NONE ? (T) body : null,
                        response.headers().asHttpHeaders(),
                        response.statusCode()));
        // @formatter:on
    }
    
 // @formatter:off
    private RequestBodySpec getRequestBodySpec(
            HttpMethod method,
            URL url,
            Optional<Map<String, String>> headers,
            Optional<Map<String, String>> queryParameters,
            Optional<MediaType> contentType) {
    // @formatter:on
        /*
         * Create the request and adds query parameters if provided
         */
        RequestBodySpec rbs = this.builder.build().method(method).uri(uriBuilder -> {
            UriBuilder ub = uriBuilder.scheme(url.getProtocol()).host(url.getHost()).port(url.getPort()).path(url.getPath());
            if (queryParameters.isPresent()) {
                for (Map.Entry<String, String> entry : queryParameters.get().entrySet()) {
                    ub = ub.queryParam(entry.getKey(), entry.getValue());
                }
            }
            URI uri = ub.build();
            _LOGGER.debug(String.format("Http request to: %s", uri.toString()));
            return uri;

        }).contentType(contentType.orElse(MediaType.APPLICATION_JSON));

        /*
         * Add HTTP headers if provided
         */
        if (headers.isPresent()) {
            for (Map.Entry<String, String> entry : headers.get().entrySet()) {
                rbs = rbs.header(entry.getKey(), entry.getValue());
            }
        }

        return rbs;
    }

}
