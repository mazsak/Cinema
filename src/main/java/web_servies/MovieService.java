package web_servies;

import dao.ActorDao;
import dao.DirectorDao;
import dao.MovieDao;
import entities.Actor;
import entities.Director;
import entities.Movie;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@WebService(serviceName = "movieservice")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class MovieService {

    private MovieDao movieDao;
    private ActorDao actorDao;
    private DirectorDao directorDao;

    public MovieService() {
        movieDao = new MovieDao();
        actorDao = new ActorDao();
        directorDao = new DirectorDao();
    }

    @WebMethod
    public List<Movie> getAllMovie() {
        List<Movie> movies = movieDao.findAll();
        return movies;
    }

    @WebMethod
    public Movie getMovieById(long id) {
        Movie movie = movieDao.findById(id).get();
        return movie;
    }
}
