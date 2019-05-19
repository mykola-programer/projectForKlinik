package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.SurgeonPlan;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.vo.SurgeonPlanVO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class SurgeonPlanService {

    private DAO<SurgeonPlan> surgeonPlanDAO;

    @Autowired
    private SurgeonService surgeonService;

    @Autowired
    private DatePlanService datePlanService;

    @Autowired
    public void setSurgeonPlanDAO(DAO<SurgeonPlan> surgeonPlanDAO) {
        this.surgeonPlanDAO = surgeonPlanDAO;
        this.surgeonPlanDAO.setClassEO(SurgeonPlan.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SurgeonPlanVO findByID(int surgeonPlanID) {
        return convertToSurgeonPlanVO(surgeonPlanDAO.findByID(surgeonPlanID));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SurgeonPlan findEntityByID(int entityID) {
        return surgeonPlanDAO.findByID(entityID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SurgeonPlanVO> findBySurgeonID(int surgeonID, LocalDate minDate) {
        Object[] param = {surgeonID, minDate};
        List<SurgeonPlan> entities = surgeonPlanDAO.findAll("SurgeonPlan.findBySurgeon", param);
        if (entities == null) return null;
        List<SurgeonPlanVO> result = new ArrayList<SurgeonPlanVO>();
        for (SurgeonPlan entity : entities) {
            result.add(convertToSurgeonPlanVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SurgeonPlanVO> findAll() {
        List<SurgeonPlan> entities = surgeonPlanDAO.findAll("SurgeonPlan.findAll");
        if (entities == null) return null;
        List<SurgeonPlanVO> result = new ArrayList<SurgeonPlanVO>();
        for (SurgeonPlan entity : entities) {
            result.add(convertToSurgeonPlanVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SurgeonPlanVO create(SurgeonPlanVO surgeonPlanVO) {
        SurgeonPlan entity = copyToSurgeonPlan(surgeonPlanVO, null);
        return convertToSurgeonPlanVO(surgeonPlanDAO.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SurgeonPlanVO update(int surgeonPlanID, SurgeonPlanVO surgeonPlanVO) {
        SurgeonPlan originalEntity = surgeonPlanDAO.findByID(surgeonPlanID);
        SurgeonPlan updatedEntity = copyToSurgeonPlan(surgeonPlanVO, originalEntity);
        return convertToSurgeonPlanVO(surgeonPlanDAO.update(updatedEntity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int surgeonPlanID) {
        return surgeonPlanDAO.remove(surgeonPlanID);
    }

    private SurgeonPlanVO convertToSurgeonPlanVO(SurgeonPlan surgeonPlan) {
        if (surgeonPlan == null) return null;
        SurgeonPlanVO result = new SurgeonPlanVO();
        result.setSurgeonPlanId(surgeonPlan.getSurgeonPlanId());

        result.setDatePlan(surgeonPlan.getDatePlan() != null ? datePlanService.findByID(surgeonPlan.getDatePlan().getDatePlanId()) : null);

        result.setSurgeonId(surgeonPlan.getSurgeon() != null ? surgeonPlan.getSurgeon().getSurgeonId() : 0);
        result.setDisable(surgeonPlan.isDisable());
        return result;
    }

    private SurgeonPlan copyToSurgeonPlan(SurgeonPlanVO original, SurgeonPlan result) {
        if (original != null) {
            if (result == null) result = new SurgeonPlan();
            result.setDatePlan(datePlanService.findEntityByID(original.getDatePlan().getDatePlanId()));
            result.setSurgeon(surgeonService.findEntityByID(original.getSurgeonId()));
            result.setDisable(original.isDisable());
        }
        return result;
    }

}