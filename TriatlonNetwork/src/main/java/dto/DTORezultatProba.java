package dto;

import domain.Proba;
import domain.Rezultat;

import java.io.Serializable;

public class DTORezultatProba implements Serializable {
    Rezultat rezultat;
    Proba proba;

    public DTORezultatProba(Rezultat rezultat, Proba proba) {
        this.rezultat = rezultat;
        this.proba = proba;
    }

    public Rezultat getRezultat() {
        return rezultat;
    }

    public Proba getProba() {
        return proba;
    }
}
