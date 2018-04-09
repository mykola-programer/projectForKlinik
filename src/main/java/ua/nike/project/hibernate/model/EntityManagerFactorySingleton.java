package ua.nike.project.hibernate.model;


import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class EntityManagerFactorySingleton {

    private static EntityManagerFactory entityManagerFactory;
    private static EntityManager entityManager;

    private EntityManagerFactorySingleton() {
    }

    public static EntityManagerFactory getEntityManagerFactory(){
        if(entityManagerFactory == null) {
            entityManagerFactory = Persistence.createEntityManagerFactory("Hibernate_JPA");
        }
        return entityManagerFactory;
    }

    public static EntityManager getEntityManager() {
        if(entityManager == null) {
            entityManager = getEntityManagerFactory().createEntityManager();
        }
        return entityManager;
    }
}
