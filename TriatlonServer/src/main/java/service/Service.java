package service;

import domain.*;
import triatlon.persistence.repository.IRepositoryArbitru;
import triatlon.persistence.repository.IRepositoryParticipant;
import triatlon.persistence.repository.IRepositoryProba;
import triatlon.persistence.repository.IRepositoryRezultat;
import triatlon.service.IService;
import triatlon.service.Observer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Service implements IService {
    private IRepositoryProba repoProba;
    private IRepositoryParticipant repoParticipant;
    private IRepositoryRezultat repoRezultat;
    private IRepositoryArbitru repoArbitru;

    private Map<Long, Observer> loggedClients;
    private final int defaultThreadsNumber = 5;


    public Service(IRepositoryProba repoProba, IRepositoryParticipant repoParticipant, IRepositoryRezultat repoRezultat, IRepositoryArbitru repoArbitru)  {
        this.repoProba = repoProba;
        this.repoParticipant = repoParticipant;
        this.repoRezultat = repoRezultat;
        this.repoArbitru = repoArbitru;

        this.loggedClients = new ConcurrentHashMap<>();

    }

    private void notifyAddedPoints(){
        ExecutorService executorService= Executors.newFixedThreadPool(this.defaultThreadsNumber);
        loggedClients.forEach((id,client)->{
            Observer obs=loggedClients.get(id);
            executorService.execute(()->{
                try{
                    System.out.println("Notifying ["+id+"]");
                    obs.notifyAddedPoints(this.participantPoints());
                }catch (Exception e){
                    System.out.println("Error notifying judge with ID: " + id + " Message: " + e.getMessage());
                }
            });
        });
        executorService.shutdown();
    }
//    private void notifyRaportAddedPoints(Proba proba){
//        ExecutorService executorService= Executors.newFixedThreadPool(this.defaultThreadsNumber);
//        loggedClients.forEach((id,client)->{
//            Observer obs=loggedClients.get(id);
//            executorService.execute(()->{
//                try{
//                    System.out.println("Notifying ["+id+"]");
//                    obs.notifyRaportAddedPoints(this.participantsFromProba(proba));
//                }catch (Exception e){
//                    System.out.println("Error notifying judge with ID: " + id + " Message: " + e.getMessage());
//
//                }
//            });
//        });
//    }
//    private void notifyParticipantsRefresh(Proba proba){
//        ExecutorService executorService= Executors.newFixedThreadPool(this.defaultThreadsNumber);
//        loggedClients.forEach((id,client)->{
//            Observer obs=loggedClients.get(id);
//            executorService.execute(()->{
//                try{
//                    System.out.println("Notifying ["+id+"]");
//                    obs.notifyParticipantsRefresh(this.participantsNotFromProba(proba));
//                }catch (Exception e){
//                    System.out.println("Error notifying judge with ID: " + id + " Message: " + e.getMessage());
//
//                }
//            });
//        });
//    }
    public synchronized Arbitru getArbitruByUsernamePassword(String username, String password,Observer client){
        Arbitru a=repoArbitru.getArbitruByUsernamePassword(username,password);
        if(a!=null)
            loggedClients.put(a.getId(), client);
        return a;
    }

    public synchronized List<ParticipantPointsDTO> participantPoints(){
        return repoRezultat.pointsParticipant();
    }

    public synchronized List<Participant> participantsNotFromProba(Proba p){ return repoRezultat.participantsNotFromProba(p); }

    public synchronized void addRezultat(Rezultat rezultat,Proba proba){
        repoRezultat.add(rezultat);
        notifyAddedPoints();
//        notifyParticipantsRefresh(proba);
//        notifyRaportAddedPoints(proba);

    }

    public synchronized Proba getProbaByIdArbitru(Arbitru arbitru){
        return repoProba.getProbaByIdArbitru(arbitru);
    }

    public synchronized List<ParticipantPointsDTO> participantsFromProba(Proba proba){
        return repoRezultat.participantsFromProba(proba);

    }

    public synchronized void logout(long id) throws Exception {
        loggedClients.remove(id);

    }

}
