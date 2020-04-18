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

public class InitDatabase {

    public static void loadingData(){
        user();
        actor();
        director();
        movie();
    }

    private static void user(){
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

    private static void actor(){
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

    private static void director(){
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

    private static void movie(){
        MovieDao movieDao = new MovieDao();
        ActorDao actorDao = new ActorDao();
        DirectorDao directorDao = new DirectorDao();
        movieDao.save(Movie.builder()
                .title("Władca pierścieni")
                .description("Fajne")
                .actors(actorDao.findAll())
                .director(directorDao.findById(2L))
                .director(directorDao.findById(1L))
                .duration(360).build());
        movieDao.save(Movie.builder()
                .title("Iron Man")
                .description("No no")
                .actor(actorDao.findById(1L))
                .actor(actorDao.findById(3L))
                .director(directorDao.findById(2L))
                .director(directorDao.findById(1L))
                .duration(360).build());
    }
}
