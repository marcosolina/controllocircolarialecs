package com.alecs.controllocircolariscuola.repository.interfaces;

import java.time.LocalDateTime;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.alecs.controllocircolariscuola.models.repo.HtmlEntity;

import reactor.core.publisher.Flux;

@Repository
public interface HtmlRepo extends ReactiveCrudRepository<HtmlEntity,LocalDateTime> {
    Flux<HtmlEntity> findAllByOrderByHtmlTsDesc();
}
