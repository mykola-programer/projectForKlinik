//package ua.nike.project.spring.model;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.stereotype.Component;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//
//@Component
//public class EntityManagerConfig {
//
//    @Autowired
//    private EntityManagerFactory entityManagerFactory;
//
//    @Bean(name = "entityManager", destroyMethod = "close")
//    public EntityManager getEntityManager() {
//        return this.entityManagerFactory.createEntityManager();
//    }
//
//}
