package triatlon.persistence.repository;

import domain.*;

import java.util.List;

public interface IRepositoryRezultat extends IRepository<Long, Rezultat> {

    //public Float pointsParticipant(Participant Participant);
    public List<ParticipantPointsDTO> pointsParticipant();
    public List<Participant> participantsNotFromProba(Proba Proba);
    public List<ParticipantPointsDTO> participantsFromProba(Proba Proba);
}
