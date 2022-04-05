package triatlon.service;

import domain.Participant;
import domain.ParticipantPointsDTO;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface Observer extends Remote {
     void notifyAddedPoints(List<ParticipantPointsDTO> participants) throws Exception ;
//     void notifyRaportAddedPoints(List<ParticipantPointsDTO> participants) throws Exception;
//     void notifyParticipantsRefresh(List<Participant> participants) throws Exception;
}
