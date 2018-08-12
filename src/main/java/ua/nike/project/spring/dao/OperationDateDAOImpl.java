package ua.nike.project.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.OperationDate;
import ua.nike.project.spring.vo.OperationDateVO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Repository
public class OperationDateDAOImpl implements OperationDateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public OperationDateVO findOperationDate(int operationDateID) {
        return transformToOperationDateVO(this.entityManager.find(OperationDate.class, operationDateID));
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int saveOperationDate(OperationDate operationDate) {
        this.entityManager.persist(operationDate);
        this.entityManager.flush();
        return operationDate.getOperationDateId();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<OperationDateVO> getListOperationDates() {
        List<OperationDate> operationDates = this.entityManager.createNamedQuery("OperationDate.findAll", OperationDate.class).getResultList();
        List<OperationDateVO> result = new ArrayList<>();
        for (OperationDate operationDate : operationDates) {
            result.add(transformToOperationDateVO(operationDate));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<LocalDate> getDates() {
        return this.entityManager.createNamedQuery("OperationDate.getOperationDates").getResultList();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public OperationDateVO saveDate(LocalDate date) {
        OperationDate operationDate = new OperationDate();
        operationDate.setDate(date);
        this.entityManager.persist(operationDate);
        this.entityManager.flush();
        return transformToOperationDateVO(operationDate);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<OperationDateVO> saveDates(Set<LocalDate> dates) {
        for (LocalDate date : dates) {
            OperationDate operationDate = new OperationDate();
            operationDate.setDate(date);
            this.entityManager.persist(operationDate);
            System.out.println(operationDate.getOperationDateId());
        }
//        this.entityManager.flush();
//        this.entityManager.clear();
        return getListOperationDates();
    }

    private OperationDateVO transformToOperationDateVO(OperationDate operationDate) {
        if (operationDate == null) return null;
        OperationDateVO result = new OperationDateVO();
        result.setOperationDateId(operationDate.getOperationDateId());
        result.setDate(operationDate.getDate());

//        result.setHospitalizations(operationDate.getHospitalizations());
//        result.setOperations(operationDate.getOperations());

        return result;
    }

}
