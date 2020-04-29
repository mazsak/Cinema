package dao;

import entities.Screening;

import java.util.List;

public class ScreeningDao extends CRUD<Screening> {
    public List<Screening> findByDate(int year, int month, int day) {
        em.getTransaction().begin();
        List<Screening> results = em.createQuery("from Screening where year=:y and month=:m and day=:d", Screening.class)
                .setParameter("d", day)
                .setParameter("m", month)
                .setParameter("y", year).getResultList();
        em.getTransaction().commit();
        return results;
    }
}
