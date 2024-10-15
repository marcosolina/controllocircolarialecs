package com.alecs.controllocircolariscuola.services.implementations;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alecs.controllocircolariscuola.repository.interfaces.HtmlRepo;
import com.alecs.controllocircolariscuola.services.interfaces.HtmlChecker;
import com.alecs.controllocircolariscuola.services.interfaces.RestClientUtils;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

@Service
public class HtmlCheckerIstitutoComprensivoDiCastelMella implements HtmlChecker{
    private static final Logger _LOGGER = LoggerFactory.getLogger(HtmlCheckerIstitutoComprensivoDiCastelMella.class);
    private HtmlRepo repo;
    private RestClientUtils rest;
    private static URI uri = URI.create("https://iccastelmella.edu.it/le-circolari");
    
    public HtmlCheckerIstitutoComprensivoDiCastelMella(HtmlRepo repo, RestClientUtils rest) {
        this.repo = repo;
        this.rest = rest;
    }

    @PostConstruct
    public void init() {
        this.checkForHtmlChanges();
    }
    
    @Override
    public Mono<Boolean> checkForHtmlChanges() {
        _LOGGER.debug("Checking html changes");
        
        // @formatter:off
        try {
            rest.performGetRequestNoExceptions(String.class, uri.toURL(), Optional.empty(), Optional.empty())
                .subscribe(html -> {
                    _LOGGER.debug(html.getBody());
                });
            var monoOldHtml = repo.findAllByOrderByHtmlTsDesc()
                    .next()
                    .map(entity -> entity.getHtml())
                    ;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        // @formatter:on
        
        return Mono.just(true);
    }

    @Override
    public Mono<Boolean> clearOldHtml() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<Boolean> sendNotification() {
        // TODO Auto-generated method stub
        return null;
    }
}
