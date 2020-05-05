package web_servies;

import dao.UserDao;
import entities.User;

import javax.jws.HandlerChain;
import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.soap.MTOM;
import javax.xml.ws.soap.SOAPBinding;
import java.util.Base64;

@MTOM
@WebService(serviceName = "service")
@HandlerChain(file = "handler-chain.xml")
@BindingType(value = SOAPBinding.SOAP11HTTP_MTOM_BINDING)
public class UserService {

    final UserDao userDao;

    public UserService(){
        userDao = new UserDao();
    }

    @WebMethod
    public User createUser(String username, String password, long phoneNumber, String mail) {
        password = Base64.getEncoder().encodeToString(password.getBytes());
        userDao.save(
                User.builder()
                        .username(username)
                        .password(password)
                        .phoneNumber(phoneNumber)
                        .mail(mail).build());
        return userDao.findUserByUsernameAndPassword(username, password);
    }

    @WebMethod
    public User findUserByUsernameAndPassword(String username, String password){
        password = Base64.getEncoder().encodeToString(password.getBytes());
        return userDao.findUserByUsernameAndPassword(username, password);
    }

    @WebMethod
    public User updateUser(User user) {
        userDao.update(user);
        return user;
    }
}
