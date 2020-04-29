import dao.MovieDao;
import entities.Movie;
import utils.InitDatabase;
import web_servies.MovieService;

import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        InitDatabase.loadingData();

        Endpoint.publish("http://localhost:9999/cinema", new MovieService());
        System.out.println("Run and wait...");
    }
}