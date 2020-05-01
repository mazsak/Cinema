import entities.User;
import utils.InitDatabase;
import web_servies.*;

import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        InitDatabase.loadingData();

        Endpoint.publish("http://localhost:9999/cinema", new MovieService());
        Endpoint.publish("http://localhost:9999/cinema2", new ImageService());
        Endpoint.publish("http://localhost:9999/cinema3", new ScreeningService());
        Endpoint.publish("http://localhost:9999/cinema4", new ReservationService());
        Endpoint.publish("http://localhost:9999/cinema5", new UserService());
        System.out.println("Run and wait...");
    }
}
