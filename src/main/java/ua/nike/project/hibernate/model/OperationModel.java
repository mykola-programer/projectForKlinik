package ua.nike.project.hibernate.model;

import ua.nike.project.hibernate.entity.Operation;
import ua.nike.project.hibernate.entity.OperationDay;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class OperationModel {

    public static List<OperationBean> getResultOperation(LocalDate selectedDate) throws NoResultException {
//        System.out.println(Thread.currentThread().getName());
        String jpql = "FROM OperationDay od where od.operationDate = :operationDate";
        EntityManager entityManager = EntityManagerFactorySingleton.getEntityManager();
        Query query = entityManager.createQuery(jpql);
        query.setParameter("operationDate", Date.valueOf(selectedDate));
        List<OperationDay> operationDays = new ArrayList<OperationDay>(query.getResultList());
        List<OperationBean> result = new ArrayList<>();
        for (OperationDay operationDay : operationDays) {
            System.out.println(operationDay);
            for (Operation operation : operationDay.getOperations()) {
                System.out.println(operation);
                OperationBean operationBean = new OperationBean();
                operationBean.setOperationDate(operationDay.getOperationDate());
                operationBean.setSurname(operation.getPatient().getSurname());
                operationBean.setFirstname(operation.getPatient().getFirstName());
                operationBean.setSecondname(operation.getPatient().getSecondName());
                operationBean.setOperation(operation.getOperationName());
                operationBean.setEye(operation.getEye());
                operationBean.setSurgeon(operationDay.getSurgeon());
                operationBean.setManager(operation.getManager());

                result.add(operationBean);
            }
        }
        if (result.isEmpty()) throw new NoResultException("В базі даних, жодного запису по вашому запиту не знайдено!");

        return result;
    }
}
