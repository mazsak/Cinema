package web_servies;

import com.google.gson.Gson;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import dao.ReservationDao;
import dao.ScreeningDao;
import dao.SeatDao;
import dao.UserDao;
import entities.Auditorium;
import entities.Reservation;
import entities.Screening;
import entities.Seat;
import org.omg.CosNaming.NamingContextPackage.NotFound;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;

@WebService(serviceName = "reservationservice")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class ReservationService {

    private final ReservationDao reservationDao;
    private final ScreeningDao screeningDao;
    private final SeatDao seatDao;
    private final UserDao userDao;

    public ReservationService() {
        reservationDao = new ReservationDao();
        screeningDao = new ScreeningDao();
        seatDao = new SeatDao();
        userDao = new UserDao();
    }

    @WebMethod
    public String getAllReservation() {
        return new Gson().toJson(reservationDao.findAll());
    }

    @WebMethod
    public List<Reservation> getReservationByUserId(Long idUser) {
        return reservationDao.findByUserId(idUser);
    }

    @WebMethod
    public void reserve(Long screeningId, List<Long> seatIds, Long userId) {
        Reservation reservation = new Reservation();
        reservation.setReserved(true);
        screeningDao.findById(screeningId).ifPresent(reservation::setScreening);
        Set<Seat> seats = seatIds.stream().map(seatId -> seatDao.findById(seatId).orElse(null)).filter(Objects::nonNull).collect(Collectors.toSet());
        reservation.setSeats(seats);
        userDao.findById(userId).ifPresent(user -> {
            reservation.setUser(user);
            try {
                sendEmail(user.getMail(), reservation);
            } catch (MessagingException | IOException | DocumentException | NotFound e) {
                e.printStackTrace();
            }
        });
        reservationDao.save(reservation);
    }

    @WebMethod
    public Reservation findReservationById(Long reservationId){
        return reservationDao.findById(reservationId).orElse(null);
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
        userDao.findById(reservation.getUser().getId()).ifPresent(user -> {
            reservation.setUser(user);
            try {
                sendEmail(user.getMail(), reservation);
            } catch (MessagingException | IOException | DocumentException | NotFound e) {
                e.printStackTrace();
            }
        });
    }

    @WebMethod
    public void sendEmail(String mailTo, Reservation reservation) throws MessagingException, IOException, DocumentException, NotFound {
        Properties prop = getProperties();
        Session session = getSession(prop);
        Message message = createReservationMessage(session, mailTo, reservation);
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


    private Message createReservationMessage(Session session, String emailTo, Reservation reservation) throws MessagingException, IOException, DocumentException, NotFound {
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

        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream("reservation.pdf"));

        document.open();
        if (reservation==null){
            throw new NotFound();
        }
        Font font = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Screening screening = reservation.getScreening();
        Chunk header = new Chunk("Reservation details: ", font);
        Chunk filmDetails = new Chunk("Reservation for: " +
                screening.getMovie().getTitle() +
                " on: " + screening.getDay() +"." + screening.getMonth() + "." + screening.getYear() + ", at: " +
                screening.getHour() + ":" +screening.getMinutes(), font);
        Chunk auditorium = new Chunk("At auditorium: " + screening.getAuditorium().getName(), font);
        final String[] reservationDetails = {"Seats: \n"};
        reservation.getSeats()
                .forEach(seat -> reservationDetails[0] = reservationDetails[0].concat("row: " + (seat.getRow() + 1) + ", number: " + (seat.getNumber() + 1) + "\n"));
        Chunk details = new Chunk(reservationDetails[0], font);
        document.add(new Paragraph(header));
        document.add(new Paragraph(filmDetails));
        document.add(new Paragraph(auditorium));
        document.add(new Paragraph(details));

        document.close();

        MimeBodyPart attachmentBodyPart = new MimeBodyPart();
        attachmentBodyPart.attachFile(new File("reservation.pdf"));
        multipart.addBodyPart(attachmentBodyPart);
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
