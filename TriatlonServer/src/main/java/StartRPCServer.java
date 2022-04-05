import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import service.Service;
import triatlon.persistence.repository.utils.AbsConcurrentServer;
import triatlon.persistence.repository.utils.ChatRpcConcurrentServer;

public class StartRPCServer {
    private static int defaultPort=55555;
    public static void main(String[] args) {
       ApplicationContext context = new ClassPathXmlApplicationContext("ServerConfig.xml");
        Service service = context.getBean(Service.class);
        AbsConcurrentServer server = new ChatRpcConcurrentServer(defaultPort, service);
        try {
            server.start();
        } catch (Exception e) {
            System.err.println("Error starting the server" + e.getMessage());
        }finally {
            try {
                server.stop();
            }catch(Exception e){
                System.err.println("Error stopping server "+e.getMessage());
            }
        }
        //ApplicationContext factory=new ClassPathXmlApplicationContext("classpath:ServerConfig.xml");
    }
}
