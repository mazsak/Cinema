import utils.InitDatabase;
import web_servies.*;

import javax.xml.ws.Endpoint;

public class Main {
    public static void main(String[] args) {
        InitDatabase.loadingData();

        Endpoint.publish("http://localhost:8080/movie", new MovieService());
        Endpoint.publish("http://localhost:8080/image", new ImageService());
        Endpoint.publish("http://localhost:8080/screening", new ScreeningService());
        Endpoint.publish("http://localhost:8080/reservation", new ReservationService());
        Endpoint.publish("http://localhost:8080/user", new UserService());
        System.out.println("Run and wait...");
    }
}
