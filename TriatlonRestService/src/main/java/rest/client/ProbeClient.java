package rest.client;

import domain.Proba;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import triatlon.service.rest.ServiceException;

import java.util.concurrent.Callable;

public class ProbeClient {
    public static final String URL = "http://localhost:8080/triatlon/probe";

    private RestTemplate restTemplate = new RestTemplate();

    private <T> T execute(Callable<T> callable) {
        try {
            return callable.call();
        } catch (ResourceAccessException | HttpClientErrorException e) { // server down, resource exception
            throw new ServiceException(e);
        } catch (Exception e) {
            throw new ServiceException(e);
        }
    }

    public Proba[] getAll() {
        return execute(() -> restTemplate.getForObject(URL, Proba[].class));
    }

    public Proba getById(String id) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL, id), Proba.class));
    }

    public Proba create(Proba proba) {
        return execute(() -> restTemplate.postForObject(URL, proba, Proba.class));
    }

    public void update(Proba proba) {
        execute(() -> {
            restTemplate.put(String.format("%s/%s", URL, proba.getId()), proba);
            return null;
        });
    }

    public void delete(String id) {
        execute(() -> {
            restTemplate.delete(String.format("%s/%s", URL, id));
            return null;
        });
    }
    public Proba getProbaByArbitru(String arbitru) {
        return execute(() -> restTemplate.getForObject(String.format("%s/%s", URL+"/proba_arb", arbitru), Proba.class));
    }

}
