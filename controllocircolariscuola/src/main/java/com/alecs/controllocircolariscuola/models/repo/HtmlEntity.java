package com.alecs.controllocircolariscuola.models.repo;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("html")
public class HtmlEntity {
    @Id
    @Column("htlm_ts")
    private LocalDateTime htmlTs;

    @Column("html")
    private String html;

    public HtmlEntity() {
    };

    public HtmlEntity(LocalDateTime htmlTs, String html) {
        super();
        this.htmlTs = htmlTs;
        this.html = html;
    }

    public LocalDateTime getHtmlTs() {
        return htmlTs;
    }

    public void setHtmlTs(LocalDateTime htmlTs) {
        this.htmlTs = htmlTs;
    }

    public String getHtml() {
        return html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

}
