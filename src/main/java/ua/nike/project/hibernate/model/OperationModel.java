package ua.nike.project.hibernate.model;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import java.sql.Date;
import java.time.LocalDate;
import java.util.List;


public class OperationModel {

    public static List<OperationBean> getResultOperation(LocalDate selectedDate, EntityManager entityManager) throws NoResultException {

        Query query = entityManager.createNamedQuery("Operation.selectOperationInDate", OperationBean.class);
        query.setParameter("operationDate", Date.valueOf(selectedDate));
        List<OperationBean> result = query.getResultList();


//        List<ResultSet> resultList = query.getResultList();
//        List<OperationBean> result = new ArrayList<OperationBean>(query.getResultList());
//        for (ResultSet resultSet : resultList){
//            System.out.println(resultSet);
//        }
//        List<OperationBean> result = new ArrayList<>();

//        for (OperationDay operationDay : operationDays) {
//            System.out.println(operationDay);
//            for (Operation operation : operationDay.getOperations()) {
//                System.out.println(operation);
//                OperationBean operationBean = new OperationBean();
//                operationBean.setOperationDate(operationDay.getOperationDate());
//                operationBean.setSurname(operation.getPatient().getSurname());
//                operationBean.setFirstname(operation.getPatient().getFirstName());
//                operationBean.setSecondname(operation.getPatient().getSecondName());
//                operationBean.setOperation(operation.getOperationName());
//                operationBean.setEye(operation.getEye());
//                operationBean.setSurgeon(operationDay.getSurgeon());
//                operationBean.setManager(operation.getManager());

//                result.add(operationBean);
//            }
//        }
        if (result.isEmpty()) {
            throw new NoResultException("В базі даних, жодного запису по вашому запиту не знайдено!");
        }
        return result;
    }
}
