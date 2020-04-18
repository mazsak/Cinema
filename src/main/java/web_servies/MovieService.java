package web_servies;

import com.google.gson.Gson;
import dao.MovieDao;
import entities.Movie;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@WebService(serviceName = "movieservice")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class MovieService {

    private MovieDao movieDao;

    public MovieService() {
        movieDao = new MovieDao();
    }

    @WebMethod
    public String getAllMovie(){
        return new Gson().toJson(movieDao.findAll());
    }
}
