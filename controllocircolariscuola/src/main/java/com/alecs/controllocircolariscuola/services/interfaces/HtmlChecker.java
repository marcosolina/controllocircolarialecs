package com.alecs.controllocircolariscuola.services.interfaces;

import reactor.core.publisher.Mono;

public interface HtmlChecker {

    public Mono<Boolean> checkForHtmlChanges();
    
    public Mono<Boolean> clearOldHtml();
    
    public Mono<Boolean> sendNotification();
}
