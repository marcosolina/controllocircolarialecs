package com.alecs.controllocircolariscuola.services.implementations;

import java.io.IOException;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.alecs.controllocircolariscuola.models.svc.Circolare;
import com.alecs.controllocircolariscuola.services.interfaces.HtmlChecker;
import com.alecs.controllocircolariscuola.services.interfaces.SendNotification;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import com.microsoft.playwright.options.AriaRole;

import reactor.core.publisher.Mono;

@Service
public class HtmlCheckerIstitutoComprensivoDiCastelMella implements HtmlChecker {
    private static final Logger _LOGGER = LoggerFactory.getLogger(HtmlCheckerIstitutoComprensivoDiCastelMella.class);
    private static final Map<LocalDate, Circolare> circolariMemorizzate = new HashMap<LocalDate, Circolare>();
    private SendNotification notification;
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ITALIAN);

    private static URI uri = URI.create("https://iccastelmella.edu.it/le-circolari");

    public HtmlCheckerIstitutoComprensivoDiCastelMella(SendNotification notification) {
        this.notification = notification;
    }

    @Scheduled(cron = "0 * * * * *") // Every minute
    public void init() throws IOException {
        checkForNewNotifications().subscribe();
    }

    @Override
    public Mono<Boolean> checkForNewNotifications() {
        // @formatter:off
        
        int count = this.getTodaysCount();
        if(count == 0) {
            return Mono.just(true);
        }
        
        LocalDate today = LocalDate.now();
        var circolareMemorizzata = circolariMemorizzate.get(today);
        
        if(circolareMemorizzata == null) {
            var nuovaCircolare = new Circolare();
            nuovaCircolare.setDate(today);
            nuovaCircolare.setNumeroCircolari(count);
            circolariMemorizzate.put(today, nuovaCircolare);
            circolareMemorizzata = nuovaCircolare;
        }
        
        if(circolareMemorizzata.getNumeroCircolari() != count) {
            circolareMemorizzata.setNotificaInviata(false);
        }
        
        if(!circolareMemorizzata.isNotificaInviata()) {
            return this.sendNotification("Nuova circolare disponibile");
        }
        
        return Mono.just(true);
        // @formatter:on
    }

    @Override
    public Mono<Boolean> sendNotification(String message) {
        return this.notification.sendNotification(message);
    }

    private int getTodaysCount() {
        try (Playwright playwright = Playwright.create()) {
            // Browser browser = playwright.chromium().launch(new
            // BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            Browser browser = playwright.chromium().launch();
            Page page = browser.newPage();
            page.navigate(uri.toString());
            page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accetta Tutto")).click();
            LocalDate date = LocalDate.now();
            String formattedDate = date.format(dateFormatter);

            return page.getByText(String.format("Pubblicato: %s", formattedDate)).count();

        } catch (Exception e) {
            _LOGGER.error(e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}
