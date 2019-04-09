package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Department;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.vo.DepartmentVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class DepartmentService {

    private DAO<Department> departmentDAO;

    @Autowired
    public void setDepartmentDAO(DAO<Department> departmentDAO) {
        this.departmentDAO = departmentDAO;
        this.departmentDAO.setClassEO(Department.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public DepartmentVO findByID(int departmentID) {
        return convertToDepartmentVO(departmentDAO.findByID(departmentID));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Department findEntityByID(int entityID) {
        return departmentDAO.findByID(entityID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<DepartmentVO> findAll() {
        List<Department> entities = departmentDAO.findAll("Department.findAll");
        if (entities == null) return null;
        List<DepartmentVO> result = new ArrayList<DepartmentVO>();
        for (Department entity : entities) {
            result.add(convertToDepartmentVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DepartmentVO create(DepartmentVO departmentVO) {
        Department entity = copyToDepartment(departmentVO, null);
        return convertToDepartmentVO(departmentDAO.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public DepartmentVO update(int departmentID, DepartmentVO departmentVO) {
        Department originalEntity = departmentDAO.findByID(departmentID);
        Department updatedEntity = copyToDepartment(departmentVO, originalEntity);
        return convertToDepartmentVO(departmentDAO.update(updatedEntity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int departmentID) {
        return departmentDAO.remove(departmentID);
    }

    private DepartmentVO convertToDepartmentVO(Department department) {
        if (department == null) return null;
        DepartmentVO result = new DepartmentVO();
        result.setDepartmentId(department.getDepartmentId());
        result.setName(department.getName());
        result.setDisable(department.getDisable());
        return result;
    }

    private Department copyToDepartment(DepartmentVO original, Department result) {
        if (original != null) {
            if (result == null) result = new Department();
            result.setName(original.getName());
            result.setDisable(original.getDisable());
        }
        return result;
    }

}