package web_servies;

import dao.ScreeningDao;
import entities.Screening;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import java.util.List;

@WebService(serviceName = "service")
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL)
public class ScreeningService {

    private final ScreeningDao screeningDao;

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

    @WebMethod
    public Screening getScreeningById(Long id) {
        return screeningDao.findById(id).get();
    }
}
