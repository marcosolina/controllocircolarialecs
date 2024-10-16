package com.alecs.controllocircolariscuola.models.svc;

import java.time.LocalDate;

public class Circolare {
    private LocalDate date;
    private int numeroCircolari;
    private boolean notificaInviata;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public int getNumeroCircolari() {
        return numeroCircolari;
    }

    public void setNumeroCircolari(int numeroCircolari) {
        this.numeroCircolari = numeroCircolari;
    }

    public boolean isNotificaInviata() {
        return notificaInviata;
    }

    public void setNotificaInviata(boolean notificaInviata) {
        this.notificaInviata = notificaInviata;
    }

}
