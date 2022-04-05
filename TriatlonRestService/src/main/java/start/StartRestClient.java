package start;

import domain.Arbitru;
import domain.Proba;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import rest.client.ProbeClient;
import triatlon.service.rest.ServiceException;

public class StartRestClient {
    private final static ProbeClient probeClient=new ProbeClient();
    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        try {
            Arbitru arb=new Arbitru("jessie_glaser@yahoo.com","passjessie","Jessi Glaser");
            arb.setId(1L);
            Proba p=new Proba("probaADDTest",arb);
            show(()->System.out.println("\n--TEST-- Added proba: "+probeClient.create(p)));

            show(()->
                System.out.println("\n--TEST-- ProbaByID: "+probeClient.getById("3")));

            show(()->
                System.out.println("\n--TEST-- GetAll dupa ADD: "));
                for(Proba proba: probeClient.getAll())
                    System.out.println(proba);

            show(()-> {
                String id="3";
                System.out.println("\n--TEST-- Updated proba with id "+id);
                Proba proba=probeClient.getById(id);
                System.out.println("\n--TEST-- Before: "+proba);
                proba.setDiscipline("probaUPDATETest");
                Arbitru a=new Arbitru("devin_leseven@yahoo.com","passdevin","Devin LeSeven");
                a.setId(2L);
                proba.setArbitru(a);
                probeClient.update(proba);
                System.out.println("\n--TEST--After: "+proba);
            });

            show(()->
                    System.out.println("\n--TEST-- GetAll dupa UPDATE: "));
            for(Proba proba: probeClient.getAll())
                System.out.println(proba);

            show(()-> {
                String id="3";
                probeClient.delete(id);
                System.out.println("\n--TEST-- Deleted proba with id "+id);
            });

            show(()->
                    System.out.println("\n--TEST-- GetAll dupa DELETE: "));
            for(Proba proba: probeClient.getAll())
                System.out.println(proba);

            show(()->{
                System.out.println("\n--TEST-- Get Proba By Arbitru with id=6 "+probeClient.getProbaByArbitru("6"));
            });
        } catch (RestClientException ex) {
            System.out.println("Exception ... " + ex.getMessage());
        }
    }



    private static void show(Runnable task) {
        try {
            task.run();
        } catch (ServiceException e) {
            //  LOG.error("Service exception", e);
            System.out.println("Service exception"+ e);
        }
    }
}
