package web_servies;

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
    public List<Movie> getAllMovie() {
        List<Movie> movies = movieDao.findAll();
        for (Movie movie : movies) {
            movie.setScreenings(null);
            movie.setDirectors(null);
            movie.setActors(null);
        }
        return movies;
    }

    @WebMethod
    public Movie getMovieById(long id) {
        Movie movie = movieDao.findById(id).get();
        movie.setScreenings(null);
        movie.setDirectors(null);
        movie.setActors(null);
        return movie;
    }
}
