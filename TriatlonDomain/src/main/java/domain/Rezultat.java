package domain;

public class Rezultat extends Entity<Long>{
    private Proba proba;
    private Participant participant;
    private Float points;

    public Rezultat(Proba proba, Participant participant, Float points) {
        this.proba = proba;
        this.participant = participant;
        this.points = points;
    }

    public Proba getProba() {
        return proba;
    }

    public void setProba(Proba proba) {
        this.proba = proba;
    }

    public Participant getParticipant() {
        return participant;
    }

    public void setParticipant(Participant participant) {
        this.participant = participant;
    }

    public Float getPoints() {
        return points;
    }

    public void setPoints(Float points) {
        this.points = points;
    }

    @Override
    public String toString() {
        return "Proba: "+proba.getDiscipline()+" "+"Participant: "+participant.getFirstName()+" "+participant.getLastName()
                +" Points: "+points;
    }
}
