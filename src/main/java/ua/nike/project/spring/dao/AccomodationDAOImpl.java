package ua.nike.project.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Accomodation;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.AccomodationVO;
import ua.nike.project.spring.vo.VisitVO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class AccomodationDAOImpl implements AccomodationDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private VisitDAO visitDAO;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int addAccomodation(AccomodationVO accomodationVO) {
        Accomodation accomodation = new Accomodation();
        try {
            accomodation = this.entityManager.createQuery("FROM Accomodation acc WHERE acc.ward=? AND acc.wardPlace=? ", Accomodation.class)
                    .setParameter(0, accomodationVO.getWard()).setParameter(1, accomodationVO.getWardPlace())
                    .getSingleResult();
            accomodation.setPlaceLocked(false);

        } catch (PersistenceException e) {
            this.copyToAccomodation(accomodationVO, accomodation);
            accomodation.setPlaceLocked(false);
            this.entityManager.persist(accomodation);
            this.entityManager.flush();
        }
        return accomodation.getAccomodationId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean lockAccomodationPlace(int accomodationID) throws BusinessException {
        Accomodation accomodation = this.entityManager.find(Accomodation.class, accomodationID);
        if (accomodation == null) throw new BusinessException("This accomodation is not find in database !");
        accomodation.setPlaceLocked(true);
        this.entityManager.flush();
        return accomodation.isPlaceLocked();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean unlockAccomodationPlace(int accomodationID) throws BusinessException {
        Accomodation accomodation = this.entityManager.find(Accomodation.class, accomodationID);
        if (accomodation == null) throw new BusinessException("This accomodation is not find in database !");
        accomodation.setPlaceLocked(false);
        this.entityManager.flush();
        return accomodation.isPlaceLocked();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AccomodationVO findAccomodation(int accomodationID) throws BusinessException {
        Accomodation accomodation = this.entityManager.find(Accomodation.class, accomodationID);
        if (accomodation == null) throw new BusinessException("This accomodation is not find in database !");
        return transformToAccomodationVO(accomodation);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AccomodationVO> getListUnlockedAccomodations() {
        List<Accomodation> accomodations = this.entityManager.createNamedQuery("Accomodation.getAllUnlock", Accomodation.class).getResultList();
        List<AccomodationVO> result = new ArrayList<>();
        for (Accomodation accomodation : accomodations) {
            result.add(transformToAccomodationVO(accomodation));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AccomodationVO> getListAccomodations() {
        List<Accomodation> accomodations = this.entityManager.createNamedQuery("Accomodation.findAll", Accomodation.class).getResultList();
        List<AccomodationVO> result = new ArrayList<>();
        for (Accomodation accomodation : accomodations) {
            result.add(transformToAccomodationVO(accomodation));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitVO> getListVisitsOfAccomodation(int accomodationID) throws BusinessException {
        Accomodation accomodation = this.entityManager.find(Accomodation.class, accomodationID);
        if (accomodation == null) throw new BusinessException("This accomodation is not find in database !");
        List<VisitVO> result = new ArrayList<>();
        for (Visit visit : accomodation.getVisits()) {
            result.add(this.visitDAO.transformToVisitVO(visit));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AccomodationVO transformToAccomodationVO(Accomodation accomodation) {
        if (accomodation == null) return null;
        AccomodationVO result = new AccomodationVO();
        result.setAccomodationId(accomodation.getAccomodationId());
        result.setWard(accomodation.getWard());
        result.setWardPlace(accomodation.getWardPlace());
        result.setPlaceLocked(accomodation.isPlaceLocked());
        return result;
    }

    private void copyToAccomodation(AccomodationVO original, Accomodation result) {
        if (original != null) {
            result.setWard(original.getWard());
            result.setWardPlace(original.getWardPlace());
            result.setPlaceLocked(original.getPlaceLocked());
        }
    }
}
