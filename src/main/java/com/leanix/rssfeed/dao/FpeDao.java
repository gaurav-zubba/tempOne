package com.leanix.rssfeed.dao;

import com.leanix.rssfeed.core.MasterRssEntity;

import org.hibernate.SessionFactory;
import io.dropwizard.hibernate.AbstractDAO;


public class FpeDao extends AbstractDAO<MasterRssEntity> {

    public FpeDao(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public MasterRssEntity create(MasterRssEntity task) {
        return persist(task);
    }

/*
    public Optional<MasterRssEntity> findById(Long id) {
        return Optional.ofNullable(get(id));
    }

    public List<MasterRssEntity> getAll() {
        CriteriaBuilder builder = currentSession().getCriteriaBuilder();
        CriteriaQuery<MasterRssEntity> criteria = builder.createQuery(MasterRssEntity.class);
        criteria.from(MasterRssEntity.class);
        List<MasterRssEntity> tasks = currentSession().createQuery(criteria).getResultList();
        return tasks;
    }*/
/*
    public void update(MasterRssEntity task) {
        this.currentSession().update(MasterRssEntity.class.getName(), task);
    }

    public void delete(MasterRssEntity task) {
        this.currentSession().delete(MasterRssEntity.class.getName(), task);
    }
    */
}
