package com.alecs.controllocircolariscuola.services.interfaces;

import java.nio.file.Path;
import java.util.Optional;

import reactor.core.publisher.Mono;

public interface SendNotification {

    public Mono<Boolean> sendNotification(String message, Optional<Path> screenshot);
}
