package web_servies;

import com.google.gson.Gson;
import dao.MovieDao;
import dao.ReservationDao;
import entities.Reservation;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

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
    public String getReservationByUserId(){
        return new Gson().toJson(reservationDao.findAll());
    }

//    @WebMethod
//    public
}
