package ua.nike.project.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.*;
import ua.nike.project.spring.vo.OperationVO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Repository
public class OperationDAOImpl implements OperationDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Integer saveOperation(OperationVO operationVO) {
        Operation operation = new Operation();
        copyToOperation(operationVO, operation);
        this.entityManager.persist(operation);
        this.entityManager.flush();
        return operation.getOperationId();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OperationVO findOperation(int operationID) {
        return transformToOperationVO(this.entityManager.find(Operation.class, operationID));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<OperationVO> listOperations() {
        List<Operation> operations = this.entityManager.createNamedQuery("Operation.findAll", Operation.class).getResultList();
        List<OperationVO> result = new ArrayList<>();
        for (Operation operation : operations) {
            result.add(transformToOperationVO(operation));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Integer editOperation(OperationVO operationVO) {
        Operation operation = this.entityManager.find(Operation.class, operationVO.getOperationId());
        copyToOperation(operationVO, operation);
        return operation.getOperationId();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<String> getSurgeons() {
        Set<String> surgeons =  new TreeSet<>();
        surgeons.add("Совва");
        surgeons.add("Болган");
        surgeons.add("Дзер");
        surgeons.addAll(this.entityManager.createNamedQuery("Operation.findAllSurgeons", String.class).getResultList());
        return new ArrayList<>(surgeons);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<String> getManagers() {
        Set<String> managers =  new TreeSet<>();
        managers.add("Флора");
        managers.add("Биндю");
        managers.add("Кіріченко");
        managers.addAll(this.entityManager.createNamedQuery("Operation.findAllManagers", String.class).getResultList());
        return new ArrayList<>(managers);
    }

    private OperationVO transformToOperationVO(Operation operation) {
        if (operation == null) return null;

        OperationVO result = new OperationVO();
        result.setOperationId(operation.getOperationId());

        if (operation.getOperationDate() != null) {
            result.setOperationDateID(operation.getOperationDate().getOperationDateId());
        } else result.setOperationDateID(0);

        if (operation.getTimeForCome() != null) {
            result.setTimeForCome(operation.getTimeForCome().toString());
        } else result.setTimeForCome(null);

        result.setNumberOfOrder(operation.getNumberOfOrder());

        if (operation.getPatient() != null) {
            result.setPatientID(operation.getPatient().getPatientId());
        } else result.setPatientID(0);

        if (operation.getProcedure() != null) {
            result.setProcedureID(operation.getProcedure().getProcedureId());
            result.setProcedureName(operation.getProcedure().getName());
        } else {
            result.setProcedureID(0);
            result.setProcedureName(null);
        }

        result.setEye(operation.getEye());
        result.setSurgeon(operation.getSurgeon());
        result.setManager(operation.getManager());
        result.setNote(operation.getNote());
        return result;
    }

    private void copyToOperation(OperationVO original, Operation result) {
        if (original != null) {
            if (original.getOperationDateID() > 0) {
                result.setOperationDate(this.entityManager.find(OperationDate.class, original.getOperationDateID()));
            } else result.setOperationDate(null);

            if (original.getTimeForCome() != null) {
                LocalTime time = LocalTime.parse(original.getTimeForCome(), DateTimeFormatter.ofPattern("HH:mm"));
                result.setTimeForCome(time);
            } else result.setTimeForCome(null);

            result.setNumberOfOrder(original.getNumberOfOrder());

            if (original.getPatientID() > 0) {
                result.setPatient(this.entityManager.find(Patient.class, original.getPatientID()));
            } else result.setPatient(null);

            if (original.getProcedureID() > 0) {
                result.setProcedure(this.entityManager.find(Procedure.class, original.getProcedureID()));
            } else result.setProcedure(null);

            result.setEye(original.getEye());
            result.setSurgeon(original.getSurgeon());
            result.setManager(original.getManager());

            if (original.getHospitalizationID() > 0) {
                Hospitalization hospitalization = this.entityManager.find(Hospitalization.class, original.getHospitalizationID());
                if (hospitalization.getOperationDate().getOperationDateId().equals(original.getOperationDateID())) {
                    result.setHospitalization(hospitalization);
                } else result.setHospitalization(null);
            } else result.setHospitalization(null);

            result.setNote(original.getNote());

        }
    }


}
