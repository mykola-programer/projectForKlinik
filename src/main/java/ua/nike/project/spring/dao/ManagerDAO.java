package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.Client;
import ua.nike.project.hibernate.entity.Manager;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.ClientVO;
import ua.nike.project.spring.vo.ManagerVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@Component
public interface ManagerDAO {

    int addManager(ManagerVO managerVO);

    ManagerVO editManager(int managerId, ManagerVO managerVO) throws BusinessException;

    ManagerVO findManager(int managerId) throws BusinessException;

    List<ManagerVO> getManagers();

    boolean removeManager(int managerId);

    List<VisitVO> getListVisitsOfManager(int managerId) throws BusinessException;

    ManagerVO transformToManagerVO(Manager manager);

}
