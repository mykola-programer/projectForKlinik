package ua.nike.project.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.model.HospitalizationBean;
import ua.nike.project.spring.vo.HospitalizationBeanVO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HospitalizationBeanDAOImpl implements HospitalizationBeanDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<HospitalizationBeanVO> getListHospitalizations(LocalDate selectedDate) {
        Query query = this.entityManager.createNamedQuery("Hospitalization.findAllHospitalizationBeanByOperationDate", HospitalizationBean.class);
        query.setParameter("operationDate", selectedDate);
        List<HospitalizationBean> hospitalizationBeans = query.getResultList();

        if (hospitalizationBeans.isEmpty()) {
            return new ArrayList<>();
//            throw new NoResultException("В базі даних, жодного запису по вашому запиту не знайдено!");
        }
        List<HospitalizationBeanVO> result = new ArrayList<>();
        for (HospitalizationBean hospitalizationBean : hospitalizationBeans) {
            result.add(transformToHospitalizationVO(hospitalizationBean));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<HospitalizationBeanVO> getListNoHospitalizations(LocalDate selectedDate) {
        Query query = this.entityManager.createNamedQuery("Hospitalization.findAllNotHospitalizationBeanByOperationDate", HospitalizationBean.class);
        query.setParameter("operationDate", selectedDate);
        List<HospitalizationBean> hospitalizationBeans = query.getResultList();

        if (hospitalizationBeans.isEmpty()) {
            return new ArrayList<>();
//            throw new NoResultException("В базі даних, жодного запису по вашому запиту не знайдено!");
        }
        List<HospitalizationBeanVO> result = new ArrayList<>();
        for (HospitalizationBean hospitalizationBean : hospitalizationBeans) {
            result.add(transformToHospitalizationVO(hospitalizationBean));
        }
        return result;
    }

    private HospitalizationBeanVO transformToHospitalizationVO(HospitalizationBean hospitalizationBean) {
        if (hospitalizationBean == null) return null;
        HospitalizationBeanVO result = new HospitalizationBeanVO();

        result.setDateID(hospitalizationBean.getDateID());

        result.setHospitalizationID(hospitalizationBean.getHospitalizationID());
        result.setNumberOfPlace(hospitalizationBean.getNumberOfPlace());

        result.setPatientID(hospitalizationBean.getPatientID());
        result.setSurname(hospitalizationBean.getSurname());
        result.setFirstName(hospitalizationBean.getFirstName());
        result.setSecondName(hospitalizationBean.getSecondName());
        result.setSex(hospitalizationBean.getSex());
        result.setStatus(hospitalizationBean.getStatus());

        result.setOperationID(hospitalizationBean.getOperationID());
        result.setNumberOfOrder(hospitalizationBean.getNumberOfOrder());

        if (hospitalizationBean.getOperationTypeID() != null) {
            result.setOperationTypeID(hospitalizationBean.getOperationTypeID());
        }else result.setOperationTypeID(0);

        result.setOperationType(hospitalizationBean.getOperationType());
        result.setEye(hospitalizationBean.getEye());

        if (hospitalizationBean.getTimeForCome() != null) {
            result.setTimeForCome(hospitalizationBean.getTimeForCome().toString());
        }else result.setTimeForCome(null);

        result.setSurgeon(hospitalizationBean.getSurgeon());
        result.setManager(hospitalizationBean.getManager());
        result.setNote(hospitalizationBean.getNote());

        return result;
    }
}
