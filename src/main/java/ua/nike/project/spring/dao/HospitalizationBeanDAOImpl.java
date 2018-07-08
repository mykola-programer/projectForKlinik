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
        List<HospitalizationBeanVO> hospitalizationBeansVO = new ArrayList<>();
        for (HospitalizationBean hospitalizationBean : hospitalizationBeans) {
            hospitalizationBeansVO.add(transformToHospitalizationVO(hospitalizationBean));
        }
        return hospitalizationBeansVO;
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
        List<HospitalizationBeanVO> hospitalizationBeansVO = new ArrayList<>();
        for (HospitalizationBean hospitalizationBean : hospitalizationBeans) {
            hospitalizationBeansVO.add(transformToHospitalizationVO(hospitalizationBean));
        }
        return hospitalizationBeansVO;
    }

    private HospitalizationBeanVO transformToHospitalizationVO(HospitalizationBean hospitalizationBean) {
        if (hospitalizationBean == null) return null;
        HospitalizationBeanVO hospitalizationBeanVO = new HospitalizationBeanVO();

        hospitalizationBeanVO.setNumberOfPlace(hospitalizationBean.getNumberOfPlace());
        hospitalizationBeanVO.setNumberOfOrder(hospitalizationBean.getNumberOfOrder());
        hospitalizationBeanVO.setSurname(hospitalizationBean.getSurname());
        hospitalizationBeanVO.setFirstName(hospitalizationBean.getFirstName());
        hospitalizationBeanVO.setSecondName(hospitalizationBean.getSecondName());
        hospitalizationBeanVO.setSex(hospitalizationBean.getSex());
        hospitalizationBeanVO.setStatus(hospitalizationBean.getStatus());
        hospitalizationBeanVO.setProcedure(hospitalizationBean.getProcedure());
        hospitalizationBeanVO.setEye(hospitalizationBean.getEye());
        hospitalizationBeanVO.setTimeForCome(hospitalizationBean.getTimeForCome().toString());
        hospitalizationBeanVO.setSurgeon(hospitalizationBean.getSurgeon());
        hospitalizationBeanVO.setManager(hospitalizationBean.getManager());
        hospitalizationBeanVO.setNote(hospitalizationBean.getNote());

        return hospitalizationBeanVO;
    }
}
