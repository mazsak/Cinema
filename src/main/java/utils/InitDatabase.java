package utils;

import dao.*;
import entities.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class InitDatabase {

    public static void loadingData() {
        loadUsers();
        loadActors();
        loadAuditoriums();
        loadDirectors();
        loadMovies();
        loadScreenings();
        loadSeats();
        loadReservations();
    }

    private static void loadUsers() {
        System.out.println("-------------Init user-------------");
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

    private static void loadActors() {
        System.out.println("-------------Init actor-------------");
        ActorDao actorDao = new ActorDao();
        actorDao.save(Actor.builder()
                .firstName("Cezary")
                .secondName("Pazura")
                .birthDate(LocalDate.of(1970, 12, 12))
                .birthPlace("Warszawa")
                .height(180).build());
        actorDao.save(Actor.builder()
                .firstName("Ferdymant")
                .secondName("Kiebski")
                .birthDate(LocalDate.of(1960, 8, 20))
                .birthPlace("Ludz").height(176).build());
        actorDao.save(Actor.builder()
                .firstName("Karol")
                .secondName("Oh")
                .birthDate(LocalDate.of(1980, 3, 3))
                .birthPlace("Krakow").height(174).build());
    }

    private static void loadDirectors() {
        System.out.println("-------------Init director-------------");
        DirectorDao directorDao = new DirectorDao();
        directorDao.save(Director.builder()
                .firstName("CezaryD")
                .secondName("PazuraD")
                .birthDate(LocalDate.of(1970, 12, 12))
                .birthPlace("Warszawa")
                .height(180).build());
        directorDao.save(Director.builder()
                .firstName("FerdymantD")
                .secondName("KiebskiD")
                .birthDate(LocalDate.of(1960, 8, 20))
                .birthPlace("Ludz").height(176).build());
        directorDao.save(Director.builder()
                .firstName("KarolD")
                .secondName("OhD")
                .birthDate(LocalDate.of(1980, 3, 3))
                .birthPlace("Krakow").height(174).build());
    }

    private static void loadAuditoriums() {
        System.out.println("-------------Init auditorium-------------");
        AuditoriumDao auditoriumDao = new AuditoriumDao();
        auditoriumDao.save(Auditorium.builder()
                .name("mala").build());
        auditoriumDao.save(Auditorium.builder()
                .name("duza").build());
    }

    private static void loadMovies() {
        System.out.println("-------------Init movie-------------");
        MovieDao movieDao = new MovieDao();
        List<Director> directors = new DirectorDao().findAll();
        List<Actor> actors = new ActorDao().findAll();
        movieDao.save(Movie.builder()
                .title("The Lord Of The Rings: The Fellowship of the ring")
                .description("The Dark Lord Sauron forges the One Ring in Mount Doom, installing into it a great part of his power to dominate the other Rings, so he might conquer Middle-earth.")
                .actors(actors)
                .director(directors.get(2))
                .director(directors.get(0))
                .duration(178).build());
        movieDao.save(Movie.builder()
                .title("Iron Man")
                .description("...")
                .actor(actors.get(1))
                .actor(actors.get(2))
                .director(directors.get(1))
                .director(directors.get(0))
                .duration(126).build());
    }

    private static void loadSeats() {
        System.out.println("-------------Init seat-------------");
        SeatDao seatDao = new SeatDao();
        List<Auditorium> auditoriums = new AuditoriumDao().findAll();
        for (Auditorium auditorium : auditoriums)
            for (int i = 1; i < 11; i++)
                for (int y = 1; y < 11; y++)
                    seatDao.save(Seat.builder()
                            .number(i)
                            .row(y)
                            .auditorium(auditorium).build());


    }

    private static void loadScreenings() {
        System.out.println("-------------Init screening-------------");
        ScreeningDao screeningDao = new ScreeningDao();
        List<Auditorium> auditoriums= new AuditoriumDao().findAll();
        List<Movie> movies= new MovieDao().findAll();
        screeningDao.save(Screening.builder()
                .auditorium(auditoriums.get(0))
                .movie(movies.get(0))
                .date(LocalDateTime.of(2020,4,20,19,0)).build());
        screeningDao.save(Screening.builder()
                .auditorium(auditoriums.get(1))
                .movie(movies.get(1))
                .date(LocalDateTime.of(2020,4,20,19,0)).build());
    }

    private static void loadReservations() {
        System.out.println("-------------Init reservation-------------");
        ReservationDao reservationDao = new ReservationDao();
        List<User> users= new UserDao().findAll();
        List<Screening> screenings = new ScreeningDao().findAll();
        List<Seat> seats = new SeatDao().findAll();
        reservationDao.save(Reservation.builder()
                .reserved(true)
                .screening(screenings.get(0))
                .seat(seats.get(0))
                .user(users.get(0)).build());
        reservationDao.save(Reservation.builder()
                .reserved(true)
                .screening(screenings.get(1))
                .seat(seats.get(5))
                .user(users.get(1)).build());
    }

}
