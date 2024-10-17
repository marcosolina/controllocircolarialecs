package com.alecs.controllocircolariscuola.services.interfaces;

import reactor.core.publisher.Mono;

public interface HtmlChecker {

    public Mono<Boolean> checkForNewNotifications();
    
    
    public Mono<Boolean> sendNotification(String message);
}
