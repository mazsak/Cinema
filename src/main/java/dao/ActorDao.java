package dao;

import entities.Actor;

import java.util.List;

public class ActorDao extends CRUD<Actor> {

    public List<Actor> findByIdFilm(long idMovie){
        em.getTransaction().begin();
        List<Actor> result = em.createQuery("from Actor a where :idMovie in a.movies", Actor.class).setParameter("idMovie",idMovie).getResultList();
        em.getTransaction().commit();
        return result;
    }
}
