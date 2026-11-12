package vn.iotstar.dao.impl;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.iotstar.config.JpaConfig;
import vn.iotstar.dao.CategoryDao;
import vn.iotstar.model.Category;

public class CategoryDaoImpl implements CategoryDao {
    private Category toModel(vn.iotstar.entity.Category entity) {
        return new Category(entity.getCategoryId(), entity.getCategoryname(), entity.getImages());
    }

    private vn.iotstar.entity.Category toEntity(Category model) {
        vn.iotstar.entity.Category entity = new vn.iotstar.entity.Category();

        entity.setCategoryId(model.getId());
        entity.setCategoryname(model.getName());
        entity.setImages(model.getIcon());

        return entity;
    }

    @Override
    public void insert(Category category) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();
            em.persist(toEntity(category));
            trans.commit();
        } catch (RuntimeException e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public void edit(Category category) {
        EntityManager em = JpaConfig.getEntityManager();
        EntityTransaction trans = em.getTransaction();

        try {
            trans.begin();

            vn.iotstar.entity.Category entity = em.find(vn.iotstar.entity.Category.class, category.getId());

            if (entity == null) {
                throw new IllegalArgumentException("Category not found");
            }

            entity.setCategoryname(category.getName());
            entity.setImages(category.getIcon());

            trans.commit();
        } catch (RuntimeException e) {
            if (trans.isActive()) {
                trans.rollback();
            }
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

            vn.iotstar.entity.Category entity = em.find(vn.iotstar.entity.Category.class, id);

            if (entity != null) {
                em.remove(entity);
            }

            trans.commit();
        } catch (RuntimeException e) {
            if (trans.isActive()) {
                trans.rollback();
            }
            throw e;
        } finally {
            em.close();
        }
    }

    @Override
    public Category get(int id) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            vn.iotstar.entity.Category entity = em.find(vn.iotstar.entity.Category.class, id);

            return entity == null ? null : toModel(entity);
        } finally {
            em.close();
        }
    }

    @Override
    public Category get(String name) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            List<vn.iotstar.entity.Category> list = em.createQuery(
                    "SELECT c FROM Category c WHERE c.categoryname = :name", vn.iotstar.entity.Category.class)
                    .setParameter("name", name).setMaxResults(1).getResultList();

            return list.isEmpty() ? null : toModel(list.get(0));
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category> getAll() {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.createQuery("SELECT c FROM Category c ORDER BY c.categoryId", vn.iotstar.entity.Category.class)
                    .getResultList().stream().map(this::toModel).toList();
        } finally {
            em.close();
        }
    }

    @Override
    public List<Category> search(String keyword) {
        EntityManager em = JpaConfig.getEntityManager();

        try {
            return em.createQuery(
                    "SELECT c FROM Category c " + "WHERE LOWER(c.categoryname) LIKE :keyword",
                    vn.iotstar.entity.Category.class)
                    .setParameter("keyword", "%" + keyword.toLowerCase(java.util.Locale.ROOT) + "%")
                    .getResultList().stream().map(this::toModel).toList();
        } finally {
            em.close();
        }
    }
}
