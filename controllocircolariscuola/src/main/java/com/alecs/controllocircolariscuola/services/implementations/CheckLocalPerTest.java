package com.alecs.controllocircolariscuola.services.implementations;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import com.alecs.controllocircolariscuola.models.svc.Circolare;
import com.alecs.controllocircolariscuola.services.interfaces.HtmlChecker;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;

import reactor.core.publisher.Mono;
import reactor.util.function.Tuple4;
import reactor.util.function.Tuples;

public class CheckLocalPerTest implements HtmlChecker {
    private static final Logger _LOGGER = LoggerFactory.getLogger(CheckLocalPerTest.class);
    private static final Map<LocalDate, Circolare> _CIRCOLARI_MEMORIZZATE = new HashMap<LocalDate, Circolare>();
    private static DateTimeFormatter _DATE_FORMATTER = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.ITALIAN);
    private static boolean _UI_CHANGED_NOTIFICATION_SENT = false;
    private static String _PREFIX = "Pubblicato:";
    private static URI _URI_TO_CHECK = URI.create("https://marco.selfip.net/circolarialetest");
    
    private static LocalDate dataDiTest = LocalDate.of(2024, 12, 4);


    @Scheduled(cron = "0 * * * * *") // every minute
    //@PostConstruct
    public void init() throws IOException {
        checkForNewNotifications().subscribe(b -> {
            var keys = _CIRCOLARI_MEMORIZZATE.keySet();
            keys.forEach(k -> {
                if (k.isBefore(dataDiTest)) {
                    _LOGGER.debug("Removing notifications with key: " + k);
                    _CIRCOLARI_MEMORIZZATE.remove(k);
                }
            });
        });
    }

    @Override
    public Mono<Boolean> checkForNewNotifications() {
        // @formatter:off
        var tuple = this.getTodaysCountAndScreenshot();
        int count = tuple.getT1();
        var screenshot = tuple.getT2();
        var uiChanged = tuple.getT3();
        var urlUltimaCircolare = tuple.getT4();
        if(count == 0) {
            if(uiChanged && !_UI_CHANGED_NOTIFICATION_SENT) {
                return this.sendNotification("Hanno cambiato la schermata", Optional.ofNullable(screenshot))
                        .map(ok -> {
                            _UI_CHANGED_NOTIFICATION_SENT = ok;
                            return ok;
                        });
            }
            return Mono.just(true);
        }
        
        var circolareMemorizzata = _CIRCOLARI_MEMORIZZATE.compute(dataDiTest, (k,v) -> {
            if(v != null) {
                return v;
            }
            var nuovaCircolare = new Circolare();
            nuovaCircolare.setDate(k);
            nuovaCircolare.setNumeroCircolari(count);
            return nuovaCircolare;
        });
        
        if(circolareMemorizzata.getNumeroCircolari() != count) {
            circolareMemorizzata.setNotificaInviata(false);
        }
        
        if(!circolareMemorizzata.isNotificaInviata()) {
            return this.sendNotification("Nuova circolare disponibile", Optional.ofNullable(screenshot))
                    .map(result -> {
                        _UI_CHANGED_NOTIFICATION_SENT = false;
                        circolareMemorizzata.setNotificaInviata(result);
                        return result;
                    })
                    .flatMap(result -> sendUrl(urlUltimaCircolare));
        }
        
        return Mono.just(true);
        // @formatter:on
    }
    
    

    @Override
    public Mono<Boolean> sendNotification(String message, Optional<Path> screenshot) {
        _LOGGER.info(message);
        return Mono.just(true);
    }
    
    private Mono<Boolean> sendUrl(String url){
        if(url != null) {
            return this.sendNotification(String.format("[Qui il link](%s)", url), Optional.empty());
        }
        
        return Mono.just(true);
    }

    private Tuple4<Integer, Path, Boolean, String> getTodaysCountAndScreenshot() {
        var screenshotPath = Paths.get("PAGINA_WEB.png");
        try (Playwright playwright = Playwright.create()) {
            Browser browser = playwright.chromium().launch(new
            BrowserType.LaunchOptions().setHeadless(false).setSlowMo(50));
            //Browser browser = playwright.chromium().launch();

            Page page = browser.newPage();
            openThePageAndAcceptCookies(page, screenshotPath);

            
            String formattedDate = dataDiTest.format(_DATE_FORMATTER);

            int count = page.getByText(String.format("%s %s", _PREFIX, formattedDate)).count();
            boolean uiChanged = page.getByText(_PREFIX).count() == 0;
            String link = this.estraiLink(page);

            return Tuples.of(count, screenshotPath, uiChanged, link);

        } catch (Exception e) {
            _LOGGER.error(e.getMessage());
            e.printStackTrace();
        }

        return Tuples.of(0, null, false, null);
    }

    private void openThePageAndAcceptCookies(Page page, Path screenshotPath) {
        page.navigate(_URI_TO_CHECK.toString());
        //page.getByRole(AriaRole.BUTTON, new Page.GetByRoleOptions().setName("Accetta Tutto")).click();
        page.screenshot(new Page.ScreenshotOptions().setPath(screenshotPath));
    }
    
    private String estraiLink(Page page) {
        var locator = page.getByText("Leggi");
        if(locator.count() == 0) {
            return null;
        }
        
        return locator.first().getAttribute("href");
    }
}
