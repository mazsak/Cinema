package dao;

import database.FactoryHibernate;

import javax.persistence.EntityManager;
import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

public class CRUD<ENTITY> {

    protected final EntityManager em = FactoryHibernate.getEntityManager();
    private final Class<ENTITY> type;

    public CRUD() {
        this.type = (Class<ENTITY>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    }

    public void save(ENTITY object) {
        em.getTransaction().begin();
        em.persist(object);
        em.getTransaction().commit();
    }

    public void update(ENTITY object) {
        em.getTransaction().begin();
        em.merge(object);
        em.getTransaction().commit();
    }

    public List<ENTITY> findAll() {
        em.getTransaction().begin();
        List<ENTITY> result = em.createQuery("from " + type.getSimpleName(), type).getResultList();
        em.getTransaction().commit();
        return result;
    }

    public Optional<ENTITY> findById(Long id){
        ENTITY entity = em.find(type, id);
        return entity != null ? Optional.of(entity) : Optional.empty();
    }

    public void delete(ENTITY object){
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
