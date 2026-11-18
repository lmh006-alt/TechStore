package vn.iotstar.dao.impl;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.VideoDao;
import vn.iotstar.entity.Video;

public class VideoDaoImpl implements VideoDao {

    @Override
    public void insert(Video video) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            if (video.getCreatedDate() == null) {
                video.setCreatedDate(LocalDateTime.now());
            }

            em.persist(video);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void update(Video video) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();
            em.merge(video);
            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void delete(int id) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            Video video = em.find(Video.class, id);

            if (video != null) {
                em.remove(video);
            }

            trans.commit();
        } catch (Exception e) {
            if (trans.isActive()) trans.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Video findById(int id) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.find(Video.class, id);
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findAll() {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.createQuery(
                "SELECT v FROM Video v ORDER BY v.createdDate DESC, v.id DESC",
                Video.class
            ).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findLatest(int limit) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.createQuery(
                "SELECT v FROM Video v ORDER BY v.createdDate DESC, v.id DESC",
                Video.class
            ).setMaxResults(limit).getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Video> findByUserId(
            int userId, int page, int pageSize) {

        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.createQuery(
                "SELECT v FROM Video v WHERE v.userId = :userId "
                + "ORDER BY v.createdDate DESC, v.id DESC",
                Video.class
            )
            .setParameter("userId", userId)
            .setFirstResult((page - 1) * pageSize)
            .setMaxResults(pageSize)
            .getResultList();
        } finally {
            em.close();
        }
    }

    @Override
    public long countByUserId(int userId) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.createQuery(
                "SELECT COUNT(v) FROM Video v WHERE v.userId = :userId",
                Long.class
            )
            .setParameter("userId", userId)
            .getSingleResult();
        } finally {
            em.close();
        }
    }
}