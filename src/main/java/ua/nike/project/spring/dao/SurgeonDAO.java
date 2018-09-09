package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.Surgeon;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.SurgeonVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@Component
public interface SurgeonDAO {

    int addSurgeon(SurgeonVO surgeonVO);

    SurgeonVO editSurgeon(int surgeonId, SurgeonVO surgeonVO) throws BusinessException;

    SurgeonVO findSurgeon(int surgeonId) throws BusinessException;

    List<SurgeonVO> getSurgeons();

    boolean removeSurgeon(int surgeonId);

    List<VisitVO> getListVisitsOfSurgeon(int surgeonId) throws BusinessException;

    SurgeonVO transformToSurgeonVO(Surgeon surgeon);

}
