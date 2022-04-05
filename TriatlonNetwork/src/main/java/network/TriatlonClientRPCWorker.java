package network;

import domain.*;
import dto.DTOArbitru;
import dto.DTORezultatProba;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import triatlon.service.IService;
import triatlon.service.Observer;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class TriatlonClientRPCWorker implements Runnable, Observer {
    private IService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public TriatlonClientRPCWorker(IService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    private Response handleRequest(Request request){
        Response response=null;
        if (request.type()== RequestType.LOGIN){
            System.out.println("Login request ..."+request.type());
            DTOArbitru arbitruDTO=(DTOArbitru) request.data();
            try {
                Arbitru voluntar=server.getArbitruByUsernamePassword(arbitruDTO.getUsername(), arbitruDTO.getPassword(),this);
                response = new Response.Builder().type(ResponseType.OK).data(voluntar).build();
            } catch (Exception e) {
                connected=false;
                response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.GET_ALL_PARTICIPANTS){
            System.out.println("GetAllParticipantsPoints request..."+request.type());
            try{
                List<ParticipantPointsDTO> participants=server.participantPoints();
                response=new Response.Builder().type(ResponseType.OK).data(participants).build();
            } catch (Exception e) {
                response=new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.PROBA_BY_ARBITRU){
            System.out.println("ProbaByIdArbitru request..."+request.type());
            Arbitru arbitru=(Arbitru) request.data();
            try{
                Proba proba=server.getProbaByIdArbitru(arbitru);
                response=new Response.Builder().type(ResponseType.OK).data(proba).build();

            } catch (Exception e) {
                response=new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.PARTICIPANTS_NOT_FROM_PROBA){
            System.out.println("ParticipantsNotFromProba request..."+request.type());
            Proba proba= (Proba) request.data();
            try{
                List<Participant> participants= server.participantsNotFromProba(proba);
                response=new Response.Builder().type(ResponseType.OK).data(participants).build();

            } catch (Exception e) {
                response=new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.PARTICIPANTS_FROM_PROBA){
            System.out.println("ParticipantsFromProba request..."+request.type());
            Proba proba= (Proba) request.data();
            try{
                List<ParticipantPointsDTO> participants= server.participantsFromProba(proba);
                response=new Response.Builder().type(ResponseType.OK).data(participants).build();

            } catch (Exception e) {
                response=new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type()==RequestType.ADD_POINTS){
            System.out.println("AddPoints request..."+request.type());
            DTORezultatProba dto= (DTORezultatProba) request.data();
            try{
                server.addRezultat(dto.getRezultat(),dto.getProba());
                response=new Response.Builder().type(ResponseType.OK).build();

            } catch (Exception e) {
                response=new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT){
            System.out.println("Log_Out Request ..."+request.type());
            Long id=(Long) request.data();
            try {
                server.logout(id);
                connected=false;
                response = new Response.Builder().type(ResponseType.OK).build();
            } catch (Exception e) {
                response = new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        output.writeObject(response);
        output.flush();
    }

    @Override
    public void notifyAddedPoints(List<ParticipantPointsDTO> participants) {
        try{
            Response response = new Response.Builder().type(ResponseType.ADDED_POINTS).data(participants).build();
            sendResponse(response);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
//    public void notifyRaportAddedPoints(List<ParticipantPointsDTO> participants) {
//        try{
//            Response response = new Response.Builder().type(ResponseType.RAPORT_ADDED_POINTS).data(participants).build();
//            sendResponse(response);
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//        }
//    }
//    public void notifyParticipantsRefresh(List<Participant> participants) {
//        try{
//            Response response = new Response.Builder().type(ResponseType.PARTICIPANTS_REFRESH).data(participants).build();
//            sendResponse(response);
//        }
//        catch (Exception e)
//        {
//            System.out.println(e.getMessage());
//        }
//    }
}
