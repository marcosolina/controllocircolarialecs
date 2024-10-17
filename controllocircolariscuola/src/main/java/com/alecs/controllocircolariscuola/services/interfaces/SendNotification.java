package com.alecs.controllocircolariscuola.services.interfaces;

import reactor.core.publisher.Mono;

public interface SendNotification {

    public Mono<Boolean> sendNotification(String message);
}
