package web_servies;

import dao.MovieDao;
import entities.Movie;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@WebService(serviceName = "service")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class MovieService {

    private final MovieDao movieDao;

    public MovieService() {
        movieDao = new MovieDao();
    }

    @WebMethod
    public List<Movie> getAllMovie() {
        return movieDao.findAll();
    }

    @WebMethod
    public Movie getMovieById(long id) {
        return movieDao.findById(id).get();
    }
}
