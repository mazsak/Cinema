package dao;

import entities.Reservation;
import entities.Seat;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class SeatDao extends CRUD<Seat> {

    public List<Set<Seat>> findReservedSeats(Long screeningId){
        em.getTransaction().begin();
        List<Reservation> reservations = em.createQuery("from Reservation r where r.screening.id=:scId", Reservation.class).setParameter("scId", screeningId).getResultList();
        List<Set<Seat>> results = reservations.stream().map(Reservation::getSeats).collect(Collectors.toList());
        em.getTransaction().commit();
        return results;
    }
}
