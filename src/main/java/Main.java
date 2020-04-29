import utils.InitDatabase;
import web_servies.ImageService;
import web_servies.MovieService;
import web_servies.ScreeningService;

import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        InitDatabase.loadingData();

        Endpoint.publish("http://localhost:9999/cinema", new MovieService());
        Endpoint.publish("http://localhost:9999/cinema2", new ImageService());
        Endpoint.publish("http://localhost:9999/cinema3", new ScreeningService());
        System.out.println("Run and wait...");
    }
}
