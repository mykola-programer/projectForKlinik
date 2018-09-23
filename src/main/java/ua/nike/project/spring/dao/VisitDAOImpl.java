package ua.nike.project.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.*;
import ua.nike.project.hibernate.type.ClientStatus;
import ua.nike.project.spring.vo.VisitVO;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitDAOImpl implements VisitDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private VisitDateDAO visitDateDAO;

    @Autowired
    private ClientDAO clientDAO;

    @Autowired
    private OperationTypeDAO operationTypeDAO;

    @Autowired
    private SurgeonDAO surgeonDAO;

    @Autowired
    private ManagerDAO managerDAO;

    @Autowired
    private AccomodationDAO accomodationDAO;

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public Integer saveVisit(VisitVO visitVO) {
        Visit visit = new Visit();
        copyToVisit(visitVO, visit);
        this.entityManager.persist(visit);
        this.entityManager.flush();
        return visit.getVisitId();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public VisitVO findVisit(int visitID) {
        return transformToVisitVO(this.entityManager.find(Visit.class, visitID));
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitVO> getVisits() {
        List<Visit> visits = this.entityManager.createNamedQuery("Visit.findAll", Visit.class).getResultList();
        List<VisitVO> result = new ArrayList<>();
        for (Visit visit : visits) {
            result.add(transformToVisitVO(visit));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitVO> getVisitsInDateOfWard(LocalDate date) {
        List<Visit> visits = this.entityManager
                .createQuery("FROM Visit v WHERE v.visitDate.date = :date AND v.accomodation IS NOT NULL ORDER BY v.accomodation.ward, v.accomodation.wardPlace", Visit.class)
                .setParameter("date", date)
                .getResultList();
        List<VisitVO> result = new ArrayList<>();
        for (Object visit : visits) {
            result.add(transformToVisitVO((Visit) visit));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitVO> getVisitsInDateOfNoWard(LocalDate date) {
        List<Visit> visits = this.entityManager
                .createQuery("FROM Visit v WHERE v.visitDate.date = :date AND v.accomodation IS NULL order by v.orderForCome", Visit.class)
                .setParameter("date", date)
                .getResultList();

        List<VisitVO> result = new ArrayList<>();
        for (Visit visit : visits) {
            result.add(transformToVisitVO(visit));
        }
        return result;
    }


    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Integer editVisit(VisitVO visitVO) {
        Visit visit = this.entityManager.find(Visit.class, visitVO.getVisitId());
        copyToVisit(visitVO, visit);
        return visit.getVisitId();
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public VisitVO transformToVisitVO(Visit visit) {
        if (visit == null) return null;

        VisitVO result = new VisitVO();
        result.setVisitId(visit.getVisitId());
        result.setVisitDate(this.visitDateDAO.transformToVisitDateVO(visit.getVisitDate()));
        result.setTimeForCome(visit.getTimeForCome());
        result.setOrderForCome(visit.getOrderForCome());
        result.setClient(this.clientDAO.transformToClientVO(visit.getClient()));

        try {
            switch (visit.getStatus().toString().toUpperCase()) {
                case "PATIENT":
                    result.setStatus("пацієнт");
                    break;
                case "RELATIVE":
                    result.setStatus("супров.");
                    break;
                default:
                    result.setStatus(null);
            }
        } catch (IndexOutOfBoundsException e) {
            result.setStatus(null);
        }

        result.setRelative(this.clientDAO.transformToClientVO(visit.getRelative()));
        result.setOperationType(this.operationTypeDAO.transformToOperationTypeVO(visit.getOperationType()));
        result.setEye(visit.getEye());
        result.setSurgeon(this.surgeonDAO.transformToSurgeonVO(visit.getSurgeon()));
        result.setManager(this.managerDAO.transformToManagerVO(visit.getManager()));
        result.setAccomodation(this.accomodationDAO.transformToAccomodationVO(visit.getAccomodation()));
        result.setNote(visit.getNote());
        return result;
    }

    private void copyToVisit(VisitVO original, Visit result) {
        if (original != null) {
            result.setVisitId(original.getVisitId());
            if (original.getVisitDate() != null) {
                result.setVisitDate(this.entityManager.find(VisitDate.class, original.getVisitDate().getVisitDateId()));
            } else {
                result.setVisitDate(null);
            }
            result.setTimeForCome(original.getTimeForCome());
            result.setOrderForCome(original.getOrderForCome());

            if (original.getClient() != null) {
                result.setClient(this.entityManager.find(Client.class, original.getClient().getClientId()));
            } else {
                result.setClient(null);
            }

            switch (original.getStatus().toLowerCase()) {
                case "пацієнт":
                    result.setStatus(ClientStatus.PATIENT);
                    break;
                case "супров.":
                case "супроводжуючий":
                    result.setStatus(ClientStatus.RELATIVE);
                    break;
                default:
                    result.setStatus(ClientStatus.PATIENT);
            }

            if (original.getRelative() != null) {
                result.setRelative(this.entityManager.find(Client.class, original.getRelative().getClientId()));
            } else {
                result.setRelative(null);
            }

            if (original.getOperationType() != null) {
                result.setOperationType(
                        this.entityManager.find(OperationType.class,
                                original.getOperationType().getOperationTypeId()));
            } else {
                result.setOperationType(null);
            }

            result.setEye(original.getEye());

            if (original.getSurgeon() != null) {
                result.setSurgeon(
                        this.entityManager.find(Surgeon.class,
                                original.getSurgeon().getSurgeonId()));
            } else {
                result.setSurgeon(null);
            }

            if (original.getManager() != null) {
                result.setManager(
                        this.entityManager.find(Manager.class,
                                original.getManager().getManagerId()));
            } else {
                result.setManager(null);
            }

            if (original.getAccomodation() != null) {
                result.setAccomodation(
                        this.entityManager.find(Accomodation.class,
                                original.getAccomodation().getAccomodationId()));
            } else {
                result.setAccomodation(null);
            }

            result.setNote(original.getNote());
        }
    }

}
