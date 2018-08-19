package ua.nike.project.spring.dao;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Procedure;
import ua.nike.project.spring.vo.ProcedureVO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProcedureDAOImpl implements ProcedureDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ProcedureVO findProcedure(int procedureID) {
        return transformToProcedureVO(this.entityManager.find(Procedure.class, procedureID));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ProcedureVO> getListProcedures() {
        List<Procedure> procedures = this.entityManager.createNamedQuery("Procedure.findAll", Procedure.class).getResultList();
        List<ProcedureVO> result = new ArrayList<>();
        for (Procedure procedure : procedures) {
            result.add(transformToProcedureVO(procedure));
        }
        return result;
    }

    private ProcedureVO transformToProcedureVO(Procedure procedure) {
        ProcedureVO procedureVO = new ProcedureVO();
        procedureVO.setProcedureId(procedure.getProcedureId());
        procedureVO.setName(procedure.getName());
        return procedureVO;
    }

}
