package domain;

import javax.lang.model.type.ArrayType;

public class Proba extends Entity<Long>{
    private String discipline;
    private Arbitru arbitru;

    public Proba() { }

    public Proba(String discipline, Arbitru arbitru) {
        this.discipline = discipline;
        this.arbitru=arbitru;
    }
    public String getDiscipline() {
        return discipline;
    }

    public void setDiscipline(String discipline) {
        this.discipline = discipline;
    }

    public Arbitru getArbitru() {
        return arbitru;
    }

    public void setArbitru(Arbitru arbitru) {
        this.arbitru = arbitru;
    }

    @Override
    public String toString() {
        return "Proba: "+discipline+" Arbitru: "+arbitru.getName();
    }
}
