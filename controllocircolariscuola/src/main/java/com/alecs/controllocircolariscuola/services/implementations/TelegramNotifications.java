package com.alecs.controllocircolariscuola.services.implementations;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.alecs.controllocircolariscuola.config.TelegramProperties;
import com.alecs.controllocircolariscuola.models.rest.TelegramMessage;
import com.alecs.controllocircolariscuola.models.rest.TelegramResponse;
import com.alecs.controllocircolariscuola.services.interfaces.RestClientUtils;
import com.alecs.controllocircolariscuola.services.interfaces.SendNotification;

import reactor.core.publisher.Mono;

@Service
public class TelegramNotifications implements SendNotification {
    private static final Logger _LOGGER = LoggerFactory.getLogger(TelegramNotifications.class);
    private RestClientUtils rest;
    private TelegramProperties props;

    public TelegramNotifications(RestClientUtils rest, TelegramProperties props) {
        this.rest = rest;
        this.props = props;
    }

    @Override
    public Mono<Boolean> sendNotification(String message) {
        _LOGGER.debug("Sending telegram text: " + message);

        URI uri = URI.create(String.format("https://api.telegram.org/bot%s/sendMessage", props.getToken()));

        var msg = new TelegramMessage();
        msg.setChatId(props.getChatId());
        msg.setText(message);

        // @formatter:off
        try {
            return this.rest.performRequestNoExceptions(
                    TelegramResponse.class,
                    HttpMethod.POST,
                    uri.toURL(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.empty(),
                    Optional.of(msg))
                .map(resp -> {
                    return resp.getStatusCode() == HttpStatus.OK && resp.getBody().isOk();
                });
            
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // @formatter:on
        return Mono.just(false);
    }

}
