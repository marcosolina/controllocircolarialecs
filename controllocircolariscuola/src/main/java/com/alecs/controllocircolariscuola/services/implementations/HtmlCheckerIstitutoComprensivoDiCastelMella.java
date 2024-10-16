package com.alecs.controllocircolariscuola.services.implementations;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alecs.controllocircolariscuola.models.svc.Circolare;
import com.alecs.controllocircolariscuola.services.interfaces.HtmlChecker;
import com.alecs.controllocircolariscuola.services.interfaces.RestClientUtils;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

import jakarta.annotation.PostConstruct;
import reactor.core.publisher.Mono;

@Service
public class HtmlCheckerIstitutoComprensivoDiCastelMella implements HtmlChecker {
    private static final Logger _LOGGER = LoggerFactory.getLogger(HtmlCheckerIstitutoComprensivoDiCastelMella.class);
    private static final Map<LocalDate, Circolare> circolariMemorizzate = new HashMap<LocalDate, Circolare>();
    private RestClientUtils rest;
    private static URI uri = URI.create("https://iccastelmella.edu.it/le-circolari");

    public HtmlCheckerIstitutoComprensivoDiCastelMella(RestClientUtils rest) {
        this.rest = rest;
    }

    @PostConstruct
    public void init() throws IOException {
        // this.checkForHtmlChanges().subscribe(b -> _LOGGER.debug(b.toString()));
        test();
    }

    private void test() throws IOException {
        try (Playwright playwright = Playwright.create()) {
            //Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.navigate(uri.toString());
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accetta Tutto")).click();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ITALIAN);
            LocalDate date = LocalDate.now();
            String formattedDate = date.format(formatter);
            
            Locator circolari = page.getByText(String.format("Pubblicato: %s", formattedDate));
            int countCircolariOdierne = circolari.count();

            if(countCircolariOdierne > 0) {
                var circolareMemorizzata = circolariMemorizzate.get(date);
                if(circolareMemorizzata == null) {
                    var nuovaCircolare = new Circolare();
                    nuovaCircolare.setDate(date);
                    nuovaCircolare.setNumeroCircolari(countCircolariOdierne);
                    circolariMemorizzate.put(date, nuovaCircolare);
                    circolareMemorizzata = nuovaCircolare;
                }
                
                if(!circolareMemorizzata.isNotificaInviata()) {
                    System.out.println("Invia notifica");
                }
                
                if(circolareMemorizzata.getNumeroCircolari() != countCircolariOdierne) {
                    System.out.println("Invia notifica");
                }
                
                circolareMemorizzata.setNotificaInviata(true);
            }
            page.screenshot(new Page.ScreenshotOptions().setPath(Paths.get("example.png")));
        }
    }

    @Override
    public Mono<Boolean> checkForHtmlChanges() {
        _LOGGER.debug("Checking html changes");

        // @formatter:off

        // @formatter:on

        return Mono.just(false);
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
