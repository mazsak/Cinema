package web_servies;

import dao.ScreeningDao;
import entities.Screening;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@WebService(serviceName = "screeningservice")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class ScreeningService {

    private ScreeningDao screeningDao;

    public ScreeningService() {
        screeningDao = new ScreeningDao();
    }

    @WebMethod
    public List<Screening> getAllScreenings() {
        return screeningDao.findAll();
    }

    @WebMethod
    public List<Screening> getScreeningsByDate(int year, int month, int day) {
        return screeningDao.findByDate(year, month, day);
    }
}