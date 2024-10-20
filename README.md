# Controllo circolari Alecs

Piccola applicazione per controllare se ci sono nuove circolari sul sito del [Istituto Comprensivo di Castel Mella](https://iccastelmella.edu.it/le-circolari).

Questo progetto usa [Playwight](https://playwright.dev/java/docs/intro) per fare scraping del sito web.

## Come funziona

Il programma controlla se ci sono nuove circolari sul sito del [Istituto Comprensivo di Castel Mella](https://iccastelmella.edu.it/le-circolari) e, se ci sono, invia una notifica tramite Telegram.
Un cron job esegue lo script ogni cinque minuti, e controlla se ci sono nuove circolari con la data di pubblicazione odierna.

## Comandi utili

- Create test tramite [Codegen](https://playwright.dev/java/docs/codegen) `mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="codegen iccastelmella.edu.it/le-circolari"`
