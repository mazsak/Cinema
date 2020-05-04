package utils;

import dao.*;
import entities.*;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InitDatabase {

    public static void loadingData() {
        loadUsers();
        loadActors();
        loadDirectors();
        loadMovies();
        loadAuditoriumsAndSeats();
        loadScreenings();
        loadReservations();
    }

    private static void loadUsers() {
        System.out.println("-------------Init user-------------");
        UserDao userDao = new UserDao();
        userDao.save(User.builder()
                .username("user1")
                .password("dddd")
                .mail("mazsak97@gmail.com")
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

    private static void loadAuditoriumsAndSeats() {
        System.out.println("-------------Init auditorium-------------");
        AuditoriumDao auditoriumDao = new AuditoriumDao();
        auditoriumDao.save(Auditorium.builder()
                .name("mala")
                .row(8)
                .number(8)
                .seats(createSeats(8, 8)).build());
        auditoriumDao.save(Auditorium.builder()
                .name("duza")
                .row(10)
                .number(10)
                .seats(createSeats(10, 10)).build());
    }

    private static Set<Seat> createSeats(int rows, int numbers) {
        SeatDao seatDao = new SeatDao();
        Set<Seat> seats = new HashSet<>();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < numbers; j++) {
                Seat seat = Seat.builder()
                        .row(i)
                        .number(j).build();
                seats.add(seat);
            }
        }
        seats.forEach(seatDao::save);
        return seats;
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
                .duration(178)
                .imagePath("src/main/java/img/lotr.jpg").build());
        movieDao.save(Movie.builder()
                .title("Iron Man")
                .description("On a weapons demo in Afghanistan, Tony's convoy is attacked and he is taken hostage by militants. Because of shrapnel in his body, a fellow hostage operates on him and puts a reactor in his chest to keep him alive.")
                .actor(actors.get(1))
                .actor(actors.get(2))
                .director(directors.get(1))
                .director(directors.get(0))
                .duration(126)
                .imagePath("src/main/java/img/iron.jpg").build());
        movieDao.save(Movie.builder()
                .title("Joker")
                .description("In Gotham City, mentally troubled comedian Arthur Fleck is disregarded and mistreated by society...")
                .actor(actors.get(1))
                .actor(actors.get(2))
                .director(directors.get(1))
                .director(directors.get(0))
                .duration(122)
                .imagePath("src/main/java/img/joker.jpg").build());
        movieDao.save(Movie.builder()
                .title("The Wolf of Wall Street")
                .description("Jordan Belfort is a Long Island penny stockbroker who served 22 months in prison for defrauding investors in a massive 1990s securities scam that involved widespread corruption on Wall Street")
                .actor(actors.get(1))
                .actor(actors.get(2))
                .director(directors.get(1))
                .director(directors.get(0))
                .duration(180)
                .imagePath("src/main/java/img/wolf.jpg").build());
        movieDao.save(Movie.builder()
                .title("Forrest Gump")
                .description("Forrest Gump is a simple man with a low I.Q. but good intentions. He is running through childhood with his best and only friend Jenny...")
                .actor(actors.get(1))
                .actor(actors.get(2))
                .director(directors.get(1))
                .director(directors.get(0))
                .duration(141)
                .imagePath("src/main/java/img/run.jpg").build());
        movieDao.save(Movie.builder()
                .title("Toy Story")
                .description("Toy Story is about the 'secret life of toys' when people are not around. When Buzz Lightyear, a space-ranger, takes Woody's place as Andy's favorite toy...")
                .actor(actors.get(1))
                .actor(actors.get(2))
                .director(directors.get(1))
                .director(directors.get(0))
                .duration(76)
                .imagePath("src/main/java/img/toy.jpg").build());
    }

    private static void loadScreenings() {
        System.out.println("-------------Init screening-------------");
        ScreeningDao screeningDao = new ScreeningDao();
        List<Auditorium> auditoriums = new AuditoriumDao().findAll();
        List<Movie> movies = new MovieDao().findAll();
        screeningDao.save(Screening.builder()
                .auditorium(auditoriums.get(0))
                .movie(movies.get(0))
                .year(2020)
                .month(5)
                .day(4)
                .hour("19")
                .minutes("00").build());
        screeningDao.save(Screening.builder()
                .auditorium(auditoriums.get(0))
                .movie(movies.get(0))
                .year(2020)
                .month(5)
                .day(4)
                .hour("15")
                .minutes("00").build());
        screeningDao.save(Screening.builder()
                .auditorium(auditoriums.get(1))
                .movie(movies.get(1))
                .year(2020)
                .month(5)
                .day(4)
                .hour("19")
                .minutes("00").build());
        screeningDao.save(Screening.builder()
                .auditorium(auditoriums.get(1))
                .movie(movies.get(1))
                .year(2020)
                .month(5)
                .day(5)
                .hour("19")
                .minutes("00").build());
        screeningDao.save(Screening.builder()
                .auditorium(auditoriums.get(1))
                .movie(movies.get(2))
                .year(2020)
                .month(5)
                .day(6)
                .hour("16")
                .minutes("00").build());
    }

    private static void loadReservations() {
        System.out.println("-------------Init reservation-------------");
        ReservationDao reservationDao = new ReservationDao();
        List<User> users = new UserDao().findAll();
        List<Screening> screenings = new ScreeningDao().findAll();
        List<Seat> seats = new SeatDao().findAll();
        reservationDao.save(Reservation.builder()
                .reserved(true)
                .screening(screenings.get(0))
                .seat(seats.get(0))
                .seat(seats.get(1))
                .user(users.get(0)).build());
        reservationDao.save(Reservation.builder()
                .reserved(true)
                .screening(screenings.get(1))
                .seat(seats.get(5))
                .user(users.get(1)).build());
    }

}
