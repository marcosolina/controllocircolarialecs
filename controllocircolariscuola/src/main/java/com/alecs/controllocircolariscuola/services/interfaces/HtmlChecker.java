package com.alecs.controllocircolariscuola.services.interfaces;

import java.nio.file.Path;
import java.util.Optional;

import reactor.core.publisher.Mono;

public interface HtmlChecker {

    public Mono<Boolean> checkForNewNotifications();
    
    
    public Mono<Boolean> sendNotification(String message, Optional<Path> screenshot);
}
