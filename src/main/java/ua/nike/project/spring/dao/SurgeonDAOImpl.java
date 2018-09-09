package ua.nike.project.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Surgeon;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.SurgeonVO;
import ua.nike.project.spring.vo.VisitVO;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class SurgeonDAOImpl implements SurgeonDAO {

    @Autowired
    private VisitDAO visitDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public int addSurgeon(SurgeonVO surgeonVO) {
        Surgeon surgeon = new Surgeon();
        this.copyToSurgeon(surgeonVO, surgeon);
        this.entityManager.persist(surgeon);
        this.entityManager.flush();
        return surgeon.getSurgeonId();
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SurgeonVO editSurgeon(int surgeonId, SurgeonVO surgeonVO) throws BusinessException {
        Surgeon surgeon = this.entityManager.find(Surgeon.class, surgeonId);
        if (surgeon == null) throw new BusinessException("This surgeon is not find in database !");
        this.copyToSurgeon(surgeonVO, surgeon);
        this.entityManager.flush();
        return transformToSurgeonVO(surgeon);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SurgeonVO findSurgeon(int surgeonID) throws BusinessException {
        Surgeon surgeon = this.entityManager.find(Surgeon.class, surgeonID);
        if (surgeon == null) throw new BusinessException("This surgeon is not find in database !");
        return transformToSurgeonVO(surgeon);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SurgeonVO> getSurgeons() {
        List<Surgeon> surgeons = this.entityManager.createNamedQuery("Surgeon.findAll", Surgeon.class).getResultList();
        List<SurgeonVO> result = new ArrayList<>();
        for (Surgeon surgeon : surgeons) {
            result.add(transformToSurgeonVO(surgeon));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean removeSurgeon(int surgeonId) {
        try {
            this.entityManager.remove(entityManager.getReference(Surgeon.class, surgeonId));
            return true;

        } catch (EntityNotFoundException e) {
            return false;
        }

    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitVO> getListVisitsOfSurgeon(int surgeonId) throws BusinessException {
        Surgeon surgeon = this.entityManager.find(Surgeon.class, surgeonId);
        if (surgeon == null) throw new BusinessException("This surgeon is not find in database !");

        List<VisitVO> result = new ArrayList<>();
        for (Visit visit : surgeon.getVisits()) {
            result.add(this.visitDAO.transformToVisitVO(visit));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SurgeonVO transformToSurgeonVO(Surgeon surgeon) {
        if (surgeon == null) return null;
        SurgeonVO result = new SurgeonVO();
        result.setSurgeonId(surgeon.getSurgeonId());
        result.setSurname(surgeon.getSurname());
        result.setFirstName(surgeon.getFirstName());
        result.setSecondName(surgeon.getSecondName());
        return result;
    }

    private void copyToSurgeon(SurgeonVO original, Surgeon result) {
        if (original != null) {
            result.setSurname(original.getSurname());
            result.setFirstName(original.getFirstName());
            result.setSecondName(original.getSecondName());
        } else result = new Surgeon();
    }

}
