package web_servies;

import com.google.gson.Gson;
import dao.MovieDao;
import dao.ReservationDao;
import entities.Reservation;
import entities.Screening;
import entities.Seat;
import entities.User;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@WebService(serviceName = "reservationservice")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class ReservationService {

    private ReservationDao reservationDao;

    public ReservationService() {
        reservationDao = new ReservationDao();
    }

    @WebMethod
    public String getAllReservation(){
        return new Gson().toJson(reservationDao.findAll());
    }

    @WebMethod
    public String getReservationByUserId(Long idUser){
        return new Gson().toJson(reservationDao.findByUserId(idUser));
    }

    @WebMethod
    public void reserve(Screening screening, List<Seat> seats, User user) {
        Reservation reservation = new Reservation();
        reservation.setReserved(true);
        reservation.setScreening(screening);
        reservation.setSeats(new HashSet<>(seats));
        reservation.setUser(user);
        reservationDao.save(reservation);
    }
}
