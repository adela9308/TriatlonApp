package domain;

public class ParticipantPointsDTO extends Entity<Long>{
    private Participant participant;
    private Float points;

    public ParticipantPointsDTO(Participant participant, Float points) {
        this.participant = participant;
        this.points = points;
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
        return  participant.getFirstName() + " "+participant.getLastName()+ " has "+points+" points.";

    }

}
