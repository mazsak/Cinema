package dao;

import database.FactoryHibernate;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;
import java.util.List;

public class CRUD<ENTITIES> {

    protected EntityManager em = FactoryHibernate.getEntityManager();
    private Class<ENTITIES> type;

    public CRUD() {
        this.type = (Class<ENTITIES>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void save(ENTITIES object) {
        em.getTransaction().begin();
        em.persist(object);
        em.getTransaction().commit();
    }

    public void update(ENTITIES object) {
        em.getTransaction().begin();
        em.merge(object);
        em.getTransaction().commit();
    }

    public List<ENTITIES> findAll() {
        em.getTransaction().begin();
        List<ENTITIES> result = em.createQuery("from " + type.getSimpleName(), type).getResultList();
        em.getTransaction().commit();
        return result;
    }

    public ENTITIES findById(Long id){
        em.getTransaction().begin();
        ENTITIES result = em.createQuery("from " + type.getSimpleName() + " where id=:id", type).setParameter("id",id).getSingleResult();
        em.getTransaction().commit();
        return result;
    }

    public void delete(ENTITIES object){
        em.getTransaction().begin();
        em.remove(object);
        em.getTransaction().commit();
    }

    public void deleteById(Long id){
        em.getTransaction().begin();
        em.createQuery("delete from " + type.getSimpleName() + " where id in (:id) ").setParameter("id",id).executeUpdate();
        em.getTransaction().commit();
    }

}
