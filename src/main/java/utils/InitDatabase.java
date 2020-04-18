package utils;

import dao.ActorDao;
import dao.DirectorDao;
import dao.MovieDao;
import dao.UserDao;
import entities.Actor;
import entities.Director;
import entities.Movie;
import entities.User;

import java.sql.Blob;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class InitDatabase {

    public static void loadingData(){
        loadUsers();
        loadActors();
        loadDirectors();
        loadMovies();
    }

    private static void loadUsers(){
        UserDao userDao = new UserDao();
        userDao.save(User.builder()
                .username("user1")
                .password("dddd")
                .mail("ktus@c.cc")
                .phoneNumber(123123123).build());
        userDao.save(User.builder()
                .username("user2")
                .password("aaaa")
                .mail("ktus1@c.cc")
                .phoneNumber(123123123).build());
        userDao.save(User.builder()
                .username("user3")
                .password("qqqq")
                .mail("ktus2@c.cc")
                .phoneNumber(123123123).build());
    }

    private static void loadActors(){
        ActorDao actorDao = new ActorDao();
        actorDao.save(Actor.builder()
                .firstName("Cezary")
                .secondName("Pazura")
                .birthDate(LocalDate.of(1970,12,12))
                .birthPlace("Warszawa")
                .height(180).build());
        actorDao.save(Actor.builder()
                .firstName("Ferdymant")
                .secondName("Kiebski")
                .birthDate(LocalDate.of(1960,8,20))
                .birthPlace("Ludz").height(176).build());
        actorDao.save(Actor.builder()
                .firstName("Karol")
                .secondName("Oh")
                .birthDate(LocalDate.of(1980,3,3))
                .birthPlace("Krakow").height(174).build());
    }

    private static void loadDirectors(){
        DirectorDao directorDao = new DirectorDao();
        directorDao.save(Director.builder()
                .firstName("CezaryD")
                .secondName("PazuraD")
                .birthDate(LocalDate.of(1970,12,12))
                .birthPlace("Warszawa")
                .height(180).build());
        directorDao.save(Director.builder()
                .firstName("FerdymantD")
                .secondName("KiebskiD")
                .birthDate(LocalDate.of(1960,8,20))
                .birthPlace("Ludz").height(176).build());
        directorDao.save(Director.builder()
                .firstName("KarolD")
                .secondName("OhD")
                .birthDate(LocalDate.of(1980,3,3))
                .birthPlace("Krakow").height(174).build());
    }

    private static void loadMovies(){
        ActorDao actorDao = new ActorDao();
        DirectorDao directorDao = new DirectorDao();
         movie("The Lord Of The Rings: The Fellowship of the ring",
                 "The Dark Lord Sauron forges the One Ring in Mount Doom, installing into it a great part of his power to dominate the other Rings, so he might conquer Middle-earth.",
                 actorDao.findAll(), directorDao.findAll().stream().limit(2).collect(Collectors.toList()),
                 178);
         movie("Iron Man", "...", actorDao.findAll().stream().limit(1).collect(Collectors.toList()),
                 directorDao.findAll().stream().limit(1).collect(Collectors.toList()), 126);
    }

    private static void movie( String title, String description, List<Actor> actors, List<Director> directors, int duration){
        MovieDao movieDao = new MovieDao();
        movieDao.save(Movie.builder()
                .title(title)
                .description(description)
                .actors(actors)
                .directors(directors)
                .duration(360).build());
    }
}
