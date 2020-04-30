package web_servies;

import com.google.gson.Gson;
import dao.*;
import entities.Reservation;
import entities.Screening;
import entities.Seat;
import entities.User;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@WebService(serviceName = "reservationservice")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class ReservationService {

    private ReservationDao reservationDao;
    private ScreeningDao screeningDao;
    private SeatDao seatDao;
    private UserDao userDao;

    public ReservationService() {
        reservationDao = new ReservationDao();
        screeningDao = new ScreeningDao();
        seatDao = new SeatDao();
        userDao = new UserDao();
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
    public void reserve(Long screeningId, List<Long> seatIds, Long userId) {
        Reservation reservation = new Reservation();
        reservation.setReserved(true);
        screeningDao.findById(screeningId).ifPresent(reservation::setScreening);
        Set<Seat> seats = seatIds.stream().map(seatId -> seatDao.findById(seatId).orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet());
        reservation.setSeats(seats);
        userDao.findById(userId).ifPresent(reservation::setUser);
        reservationDao.save(reservation);
    }

    @WebMethod
    public void cancelReservation(Long reservationId) {
        reservationDao.findById(reservationId).ifPresent(reservation -> reservationDao.delete(reservation));
    }
}
