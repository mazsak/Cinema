package dao;

import entities.User;

public class UserDao extends CRUD<User> {
    public User findUserByUsernameAndPassword(String username, String password){
        em.getTransaction().begin();
        User user = em.createQuery("from User where username=:usName and password=:pass", User.class).setParameter("usName", username).setParameter("pass", password).getSingleResult();;
        em.getTransaction().commit();
        return user;
    }
}
