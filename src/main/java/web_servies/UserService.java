package web_servies;

import dao.UserDao;
import entities.User;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.SOAPBinding;

@MTOM
@WebService(serviceName = "userservice")
@BindingType(value = SOAPBinding.SOAP11HTTP_MTOM_BINDING)
public class UserService {

    UserDao userDao;

    public UserService(){
        userDao = new UserDao();
    }

    @WebMethod
    public void createUser(String username, String password, long phoneNumber, String mail){
        userDao.save(
                User.builder()
                        .username(username)
                        .password(password)
                        .phoneNumber(phoneNumber)
                        .mail(mail).build());
    }

    @WebMethod
    public User findUserByUsernameAndPassword(String username, String password){
        return userDao.findUserByUsernameAndPassword(username, password);
    }
}
