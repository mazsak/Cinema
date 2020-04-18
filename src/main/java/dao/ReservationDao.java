package dao;

import entities.Reservation;

import java.util.List;

public class ReservationDao extends CRUD<Reservation> {

    public List<Reservation> findByUserId(Long idUser) {
        em.getTransaction().begin();
        List<Reservation> results = em.createQuery("from Reservation where user_id=:id", Reservation.class).setParameter("id", idUser).getResultList();
        em.getTransaction().commit();
        return results;
    }
}
