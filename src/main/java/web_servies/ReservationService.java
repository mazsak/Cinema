package web_servies;

import com.google.gson.Gson;
import dao.*;
import entities.*;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@WebService(serviceName = "reservationservice")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class ReservationService {

    private ReservationDao reservationDao;
    private ScreeningDao screeningDao;
    private SeatDao seatDao;
    private UserDao userDao;
    private AuditoriumDao auditoriumDao;

    public ReservationService() {
        reservationDao = new ReservationDao();
        screeningDao = new ScreeningDao();
        seatDao = new SeatDao();
        userDao = new UserDao();
        auditoriumDao = new AuditoriumDao();
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
        reservationDao.deleteById(reservationId);
    }

    @WebMethod
    public void updateSeats(Long reservationId, List<Long> seatIds) {
        Reservation reservation = reservationDao.findById(reservationId).orElseThrow(IllegalArgumentException::new);
        Set<Seat> seats = seatIds.stream().map(seatId -> seatDao.findById(seatId).orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet());
        reservation.setSeats(seats);
        reservationDao.update(reservation);
    }

    @WebMethod
    public void sendEmail(String mailTo) throws MessagingException, IOException {
        Properties prop = getProperties();
        Session session = getSession(prop);
        Message message = createReservationMessage(session, mailTo);
        Transport.send(message);
    }

    @WebMethod
    public boolean[][] findReservedSeatsByScreeningId(Long screeningId) throws NotFound {
        Screening screening = screeningDao.findById(screeningId).orElseThrow(NotFound::new);
        Auditorium auditorium = screening.getAuditorium();
        boolean[][] seats = new boolean[auditorium.getRow()][auditorium.getNumber()];
        List<Set<Seat>> reservedSeatsForScreening = seatDao.findReservedSeats(screeningId);
        reservedSeatsForScreening
                .forEach(seatSet -> seatSet
                        .forEach(seat -> seats[seat.getRow()][seat.getNumber()]=true));
        return seats;
    }


    private Message createReservationMessage(Session session, String emailTo) throws MessagingException, IOException {
        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress("cinema.rsi.project@gmail.com"));
        message.setRecipients(
                Message.RecipientType.TO, InternetAddress.parse(emailTo));
        message.setSubject("Cinema-reservation");

        String msg = "Thank you for your reservation.";

        MimeBodyPart mimeBodyPart = new MimeBodyPart();
        mimeBodyPart.setContent(msg, "text/html");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(mimeBodyPart);

        message.setContent(multipart);
        // todo: creating PDF
//        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
//        attachmentBodyPart.attachFile(file);
//        multipart.addBodyPart(attachmentBodyPart);
        return message;
    }

    private Session getSession(Properties prop) {
        return Session.getInstance(prop, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication("cinema.rsi.project", "mati1234!");
                }
            });
    }

    private Properties getProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", true);
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "25");
        return prop;
    }
}
