package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.Surgeon;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.SurgeonVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@Component
public interface SurgeonDAO {

    SurgeonVO findSurgeon(int surgeonId) throws BusinessException;


    SurgeonVO addSurgeon(SurgeonVO surgeonVO);

    SurgeonVO editSurgeon(int surgeonId, SurgeonVO surgeonVO) throws BusinessException;

    List<SurgeonVO> getSurgeons();
    List<SurgeonVO> getUnlockSurgeons();

    boolean removeSurgeon(int surgeonId);

    List<VisitVO> getVisitsOfSurgeon(int surgeonId) throws BusinessException;

    SurgeonVO transformToSurgeonVO(Surgeon surgeon);

}
