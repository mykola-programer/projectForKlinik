package ua.nike.project.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.model.HospitalizationBean;
import ua.nike.project.spring.vo.HospitalizationBeanVO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@Repository
public class HospitalizationBeanDAOImpl implements HospitalizationBeanDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<HospitalizationBeanVO> listHospitalizations(Date selectedDate) {
        Query query = this.entityManager.createNamedQuery("Hospitalization.findAllHospitalizationBeanByOperationDate", HospitalizationBean.class);
        query.setParameter("operationDate", selectedDate);
        List<HospitalizationBean> hospitalizationBeans = query.getResultList();

        if (hospitalizationBeans.isEmpty()) {
            throw new NoResultException("В базі даних, жодного запису по вашому запиту не знайдено!");
        }
        List<HospitalizationBeanVO> result = new ArrayList<>();
        for (HospitalizationBean hospitalizationBean : hospitalizationBeans) {
            result.add(transformToHospitalizationVO(hospitalizationBean));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<HospitalizationBeanVO> listNoHospitalizations(Date selectedDate) {
        Query query = this.entityManager.createNamedQuery("Hospitalization.findAllNotHospitalizationBeanByOperationDate", HospitalizationBean.class);
        query.setParameter("operationDate", selectedDate);
        List<HospitalizationBean> hospitalizationBeans = query.getResultList();

        if (hospitalizationBeans.isEmpty()) {
            throw new NoResultException("В базі даних, жодного запису по вашому запиту не знайдено!");
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

        result.setPatientID(hospitalizationBean.getPatientID());
        result.setProcedureID(hospitalizationBean.getProcedureID());
        result.setOperationID(hospitalizationBean.getOperationID());
        result.setNumberOfPlace(hospitalizationBean.getNumberOfPlace());
        result.setNumberOfOrder(hospitalizationBean.getNumberOfOrder());
        result.setSurname(hospitalizationBean.getSurname());
        result.setFirstName(hospitalizationBean.getFirstName());
        result.setSecondName(hospitalizationBean.getSecondName());
        result.setSex(hospitalizationBean.getSex());
        result.setStatus(hospitalizationBean.getStatus());
        result.setProcedure(hospitalizationBean.getProcedure());
        result.setEye(hospitalizationBean.getEye());
        result.setTimeForCome(hospitalizationBean.getTimeForCome().toString());
        result.setSurgeon(hospitalizationBean.getSurgeon());
        result.setManager(hospitalizationBean.getManager());
        result.setNote(hospitalizationBean.getNote());

        return result;

    }
}
