package dao;

import entities.Movie;

public class MovieDao extends CRUD<Movie> {

    public Movie findByIdMovieWithActorsAndDirectors(long id){
        em.getTransaction().begin();
        Movie result = em.createQuery("from Movie m, Actor a, Director d where m.id=:idMovie and m.actors", Movie.class).setParameter("idMovie",id).getSingleResult();
        em.getTransaction().commit();
        return result;
    }
}
