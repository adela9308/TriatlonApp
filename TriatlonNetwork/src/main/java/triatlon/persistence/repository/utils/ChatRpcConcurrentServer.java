package triatlon.persistence.repository.utils;

import network.TriatlonClientRPCWorker;
import triatlon.service.IService;

import java.net.Socket;


public class ChatRpcConcurrentServer extends AbsConcurrentServer {
    private IService server;
    public ChatRpcConcurrentServer(int port, IService server) {
        super(port);
        this.server = server;
        System.out.println("Chat- ChatRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        TriatlonClientRPCWorker worker=new TriatlonClientRPCWorker(server, client);
        Thread tw=new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
