package dao;

import entities.User;

import java.util.List;

public class UserDao extends CRUD<User> {
    public User findUserByUsernameAndPassword(String username, String password) {
        em.getTransaction().begin();
        List<User> users = em.createQuery("from User where username=:usName and password=:pass", User.class).setParameter("usName", username).setParameter("pass", password).getResultList();
        em.getTransaction().commit();
        return users.size() != 0 ? users.get(0) : null;
    }
}
