package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.DatePlan;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.vo.DatePlanVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DatePlanService {

    private DAO<DatePlan> datePlanDAO;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    public void setDatePlanDAO(DAO<DatePlan> datePlanDAO) {
        this.datePlanDAO = datePlanDAO;
        this.datePlanDAO.setClassEO(DatePlan.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DatePlanVO findByID(int datePlanID) {
        return convertToDatePlanVO(datePlanDAO.findByID(datePlanID));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DatePlan findEntityByID(int entityID) {
        return datePlanDAO.findByID(entityID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<DatePlanVO> findAll() {
        List<DatePlan> entities = datePlanDAO.findAll("DatePlan.findAll");
        if (entities == null) return null;
        List<DatePlanVO> result = new ArrayList<>();
        for (DatePlan entity : entities) {
            result.add(convertToDatePlanVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DatePlanVO create(DatePlanVO datePlanVO) {
        DatePlan entity = copyToDatePlan(datePlanVO, null);
        return convertToDatePlanVO(datePlanDAO.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DatePlanVO update(int datePlanID, DatePlanVO datePlanVO) {
        DatePlan originalEntity = datePlanDAO.findByID(datePlanID);
        DatePlan updatedEntity = copyToDatePlan(datePlanVO, originalEntity);
        return convertToDatePlanVO(datePlanDAO.update(updatedEntity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int datePlanID) {
        return datePlanDAO.remove(datePlanID);
    }

    private DatePlanVO convertToDatePlanVO(DatePlan datePlan) {
        if (datePlan == null) return null;
        DatePlanVO result = new DatePlanVO();
        result.setDatePlanId(datePlan.getDatePlanId());
        result.setDate(datePlan.getDate());
        result.setDepartmentID(datePlan.getDepartment() != null ? datePlan.getDepartment().getDepartmentId() : 0);
        result.setDisable(datePlan.isDisable());
        return result;
    }

    private DatePlan copyToDatePlan(DatePlanVO original, DatePlan result) {
        if (original != null) {
            if (result == null) result = new DatePlan();
            result.setDate(original.getDate());
            result.setDepartment(departmentService.findEntityByID(original.getDepartmentID()));
            result.setDisable(original.isDisable());
        }
        return result;
    }


/*    @Deprecated
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean deleteByIDs(List<Integer> datePlanIDs) {
        return datePlanDAO.remove("DatePlan.deleteByIDs", datePlanIDs);

    }

    @Deprecated
    @Transactional(propagation = Propagation.REQUIRED)
    public DatePlanVO deactivateByID(int datePlanID) {
        DatePlan datePlan = datePlanDAO.findByID(datePlanID);
        datePlan.setDisable(true);
        return convertToDatePlanVO(datePlanDAO.update(datePlan));
    }

    @Deprecated
    @Transactional(propagation = Propagation.REQUIRED)
    public DatePlanVO activateByID(int datePlanID) {
        DatePlan datePlan = datePlanDAO.findByID(datePlanID);
        datePlan.setDisable(false);
        return convertToDatePlanVO(datePlanDAO.update(datePlan));
    }
@Transactional(propagation = Propagation.REQUIRES_NEW)
public List<DatePlanVO> putDatePlans(List<DatePlanVO> datePlanVOS) {
    List<DatePlanVO> result = new ArrayList<>();
    for (DatePlanVO datePlanVO : datePlanVOS) {
        if (datePlanVO != null) {
            if (datePlanVO.getDatePlanId() > 0) {
                result.add(update(datePlanVO.getDatePlanId(), datePlanVO));
            } else {
                result.add(create(datePlanVO));
            }
        }
    }
    return result;
}


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<DatePlanVO> findAllActive() {
        List<DatePlan> entities = datePlanDAO.findAll("DatePlan.findAllActive", null);
        if (entities == null) return null;
        List<DatePlanVO> result = new ArrayList<>();
        for (DatePlan entity : entities) {
            result.add(convertToDatePlanVO(entity));
        }
        return result;
    }
*/
}