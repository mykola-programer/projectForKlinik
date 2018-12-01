package ua.nike.project.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.hibernate.entity.VisitDate;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.VisitDateVO;
import ua.nike.project.spring.vo.VisitVO;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitDateDAOImpl implements VisitDateDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private VisitDAO visitDAO;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public VisitDateVO addVisitDate(VisitDateVO visitDateVO) throws BusinessException {
        if (visitDateVO != null && visitDateVO.getDate() != null) {
            VisitDate visitDate;
            try {
                visitDate = this.entityManager.createQuery("FROM VisitDate WHERE date=? ", VisitDate.class)
                        .setParameter(0, visitDateVO.getDate())
                        .getSingleResult();
                visitDate.setInactive(false);

            } catch (NoResultException e) {
                visitDate = new VisitDate();
                this.copyToVisitDate(visitDateVO, visitDate);
                visitDate.setInactive(false);
                this.entityManager.persist(visitDate);
            }
            this.entityManager.flush();
            return transformToVisitDateVO(visitDate);
        }
        throw new BusinessException("Input visitDate is not correctly !");
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<VisitDateVO> addVisitDates(List<VisitDateVO> visitDateVOList) throws BusinessException {
        List<VisitDateVO> result = new ArrayList<>();
        for (VisitDateVO visitDateVO : visitDateVOList) {
            if (visitDateVO != null && visitDateVO.getDate() != null) {
                result.add(addVisitDate(visitDateVO));
            }
        }
        return result;
    }

    /**
     * @param visitDateID
     * @return true - if visitDate was locked
     * false - if visitDate was not found in database
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean lockVisitDate(int visitDateID) {
        VisitDate visitDate = this.entityManager.find(VisitDate.class, visitDateID);
        if (visitDate == null) return false;
        visitDate.setInactive(true);
        this.entityManager.flush();
        return visitDate.isInactive();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean lockAllVisitDates() {
        for (VisitDate visitDate : this.entityManager.createNamedQuery("VisitDate.getAllUnlock", VisitDate.class).getResultList()) {
            visitDate.setInactive(true);
        }
        this.entityManager.flush();
        return true;
    }

    /**
     * @param visitDateID
     * @return true - if visitDate was locked
     * false - if visitDate was not found in database
     */
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean unlockVisitDate(int visitDateID) {
        VisitDate visitDate = this.entityManager.find(VisitDate.class, visitDateID);
        if (visitDate == null) return false;
        visitDate.setInactive(false);
        this.entityManager.flush();
        return !visitDate.isInactive();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public VisitDateVO findVisitDate(int visitDateID) throws BusinessException {
        VisitDate visitDate = this.entityManager.find(VisitDate.class, visitDateID);
        if (visitDate == null) throw new BusinessException("This visitDate is not find in database !");
        return transformToVisitDateVO(visitDate);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitDateVO> getListUnlockedVisitDates() {
        List<VisitDate> visitDates = this.entityManager.createNamedQuery("VisitDate.getAllUnlock", VisitDate.class).getResultList();
        List<VisitDateVO> result = new ArrayList<>();
        for (VisitDate visitDate : visitDates) {
            result.add(transformToVisitDateVO(visitDate));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitDateVO> getVisitDates() {
        List<VisitDate> visitDates = this.entityManager.createNamedQuery("VisitDate.findAll", VisitDate.class).getResultList();
        List<VisitDateVO> result = new ArrayList<>();
        for (VisitDate visitDate : visitDates) {
            result.add(transformToVisitDateVO(visitDate));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitVO> getListVisitsOfVisitDate(int visitDateID) throws BusinessException {
        VisitDate visitDate = this.entityManager.find(VisitDate.class, visitDateID);
        if (visitDate == null) throw new BusinessException("This visitDate is not find in database !");
        List<VisitVO> result = new ArrayList<>();
        for (Visit visit : visitDate.getVisits()) {
            result.add(this.visitDAO.transformToVisitVO(visit));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public VisitDateVO transformToVisitDateVO(VisitDate visitDate) {
        if (visitDate == null) return null;
        VisitDateVO result = new VisitDateVO();
        result.setVisitDateId(visitDate.getVisitDateId());
        result.setDate(visitDate.getDate());
        result.setInactive(visitDate.isInactive());

        return result;
    }

    private void copyToVisitDate(VisitDateVO original, VisitDate result) {
        if (original != null) {
            result.setDate(original.getDate());
            result.setInactive(original.isInactive());
        }
    }

}
