package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.ProcedureVO;

import java.util.List;

@Component
public interface ProcedureDAO {

    ProcedureVO findProcedure(int procedureID) throws BusinessException;

    List<ProcedureVO> getListProcedures();

}
