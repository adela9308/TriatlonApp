package controller;

import domain.*;
import javafx.application.Platform;
import javafx.beans.Observable;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import triatlon.service.IService;
import triatlon.service.Observer;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.RMIClientSocketFactory;
import java.rmi.server.RMIServerSocketFactory;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class UserController extends UnicastRemoteObject implements Observer, Serializable {
    IService service;
    Arbitru arbitru;
    Stage stage;

    public UserController() throws RemoteException {
    }

    public void setStage(Stage newStage){
        this.stage=newStage;
    }

    public void setService(IService service, Arbitru arbitru) throws Exception {

        this.service=service;
        this.arbitru=arbitru;
        labelName.setText("Hello, "+arbitru.getName());

        loadParticipantsListView();
        loadParticipantFromProba();
        generateRaport();
    }
    ObservableList<ParticipantPointsDTO> modelParticipantsPoints= FXCollections.observableArrayList();
    ObservableList<Participant> modelProbaParticipant= FXCollections.observableArrayList();
    ObservableList<ParticipantPointsDTO> modelParticipantFromProba= FXCollections.observableArrayList();
    @FXML
    private Button logoutButton;
    @FXML
    private Label labelName;
    @FXML
    private ListView<ParticipantPointsDTO> participantsListView;
    @FXML
    private TextField nopointsTextField;
    @FXML
    private ListView<Participant> participantsNotFromProbaListView;
    @FXML
    private ListView<ParticipantPointsDTO> participantsFromProbaListView;


    @FXML
    void handleLogout() throws Exception {
        Stage currentStage=(Stage)nopointsTextField.getScene().getWindow();//workaround pentru obitnerea scenei curente :P
        service.logout(arbitru.getId());
        currentStage.close();
        stage.show();
    }

    @FXML
    void addPointsHandle() throws Exception {
        String error="";
        Participant p= participantsNotFromProbaListView.getSelectionModel().getSelectedItem();
        if(p==null) error+="You must select a participant!\n";

        Float points=null;
        try{
            points=Float.parseFloat(nopointsTextField.getText());
        }catch (Exception e){error+="You must enter a float number!";}
        if(!error.isEmpty()) MessageAlert.showErrorMessage(null,error);
        else {
            Proba proba = service.getProbaByIdArbitru(arbitru);
            Rezultat rezultat = new Rezultat(proba, p, points);
            service.addRezultat(rezultat,proba);
            generateRaport();
            loadParticipantFromProba();
        }
        nopointsTextField.clear();
    }


    void generateRaport() throws Exception {
        modelParticipantFromProba.clear();
        Proba proba = service.getProbaByIdArbitru(arbitru);
        List<ParticipantPointsDTO> list=service.participantsFromProba(proba);
        modelParticipantFromProba.setAll(list);
        participantsFromProbaListView.setItems(modelParticipantFromProba);
    }

    void loadParticipantsListView() throws Exception {
        modelParticipantsPoints.clear();
        List<ParticipantPointsDTO> list=service.participantPoints();
        modelParticipantsPoints.setAll(list);
        participantsListView.setItems(modelParticipantsPoints);
    }

    void loadParticipantFromProba() throws Exception {
        modelProbaParticipant.clear();
        Proba p=service.getProbaByIdArbitru(arbitru);
        List<Participant> list=service.participantsNotFromProba(p);
        modelProbaParticipant.setAll(list);
        participantsNotFromProbaListView.setItems(modelProbaParticipant);

    }

    @Override
    public void notifyAddedPoints(List<ParticipantPointsDTO> participants) throws Exception {
      Platform.runLater( ()-> {
          modelParticipantsPoints.clear();
          modelParticipantsPoints.setAll(participants);
          participantsListView.setItems(modelParticipantsPoints);
      });
    }

//    @Override
//    public void notifyRaportAddedPoints(List<ParticipantPointsDTO> participants) throws Exception {
//        modelParticipantFromProba.clear();
//        modelParticipantFromProba.setAll(participants);
//        participantsFromProbaListView.setItems(modelParticipantFromProba);
//    }
//
//    @Override
//    public void notifyParticipantsRefresh(List<Participant> participants) throws Exception {
//        //lista mica de participant not from proba
//        modelProbaParticipant.clear();
//        modelProbaParticipant.setAll(participants);
//        participantsNotFromProbaListView.setItems(modelProbaParticipant);
//    }

}
