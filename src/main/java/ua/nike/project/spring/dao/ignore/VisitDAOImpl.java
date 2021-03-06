package ua.nike.project.spring.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.*;
import ua.nike.project.hibernate.type.ClientStatus;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.service.ServiceDAO;
import ua.nike.project.spring.vo.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VisitDAOImpl implements VisitDAO {

    @Autowired
    ServiceDAO<ClientVO, Client> clientServiceDAO;
    @Autowired
    ServiceDAO<AccomodationVO, Accomodation> accomodationServiceDAO;
    @Autowired
    ServiceDAO<ManagerVO, Manager> managerServiceDAO;
    @Autowired
    ServiceDAO<OperationTypeVO, OperationType> operationTypeServiceDAO;
    @Autowired
    ServiceDAO<SurgeonVO, Surgeon> surgeonServiceDAO;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private VisitDateDAO visitDateDAO;

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public VisitVO saveVisit(VisitVO visitVO) {
        Visit visit = new Visit();
        copyToVisit(visitVO, visit);
        this.entityManager.persist(visit);
        this.entityManager.flush();
        return transformToVisitVO(visit);
    }

/*
    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<VisitVO> saveVisits(List<VisitVO> visitsVO) {
        List<VisitVO> result = new ArrayList<>();
        for (VisitVO visitVO : visitsVO) {
            result.add(saveVisit(visitVO));
        }
        return result;
    }
*/

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
    public List<VisitVO> getVisitsInDate(LocalDate date) {
        List<Visit> visits = this.entityManager
                .createQuery("FROM Visit v WHERE v.visitDate.date = :date", Visit.class)
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
    public VisitVO editVisit(int visitID, VisitVO visitVO) {
        Visit visit = this.entityManager.find(Visit.class, visitID);
        copyToVisit(visitVO, visit);
        this.entityManager.flush();
        return transformToVisitVO(visit);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean removeVisit(int visitID) {
        try {
            this.entityManager.remove(entityManager.getReference(Visit.class, visitID));
            return true;

        } catch (EntityNotFoundException e) {
            return false;
        }
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
        try {
            result.setClient(clientServiceDAO.findByID(visit.getClient() != null ? visit.getClient().getClientId() : 0, Client.class));
        } catch (BusinessException e) {
            result.setClient(null);
        }

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

        try {
            result.setPatient(clientServiceDAO.findByID(visit.getPatient() != null ? visit.getPatient().getClientId() : 0, Client.class));
        } catch (BusinessException e) {
            result.setPatient(null);
        }
        try {
            result.setOperationType(operationTypeServiceDAO.findByID(visit.getOperationType() != null ? visit.getOperationType().getOperationTypeId() : 0, OperationType.class));
        } catch (BusinessException e) {
            result.setOperationType(null);
        }

        result.setEye(visit.getEye());

        try {
            result.setSurgeon(surgeonServiceDAO.findByID(visit.getSurgeon() != null ? visit.getSurgeon().getSurgeonId() : 0, Surgeon.class));
        } catch (BusinessException e) {
            result.setSurgeon(null);
        }


        try {
            result.setManager(managerServiceDAO.findByID(visit.getManager() != null ? visit.getManager().getManagerId() : 0, Manager.class));
        } catch (BusinessException e) {
            result.setManager(null);
        }

        try {
            result.setAccomodation(accomodationServiceDAO.findByID(visit.getAccomodation() != null ? visit.getAccomodation().getAccomodationId() : 0, Accomodation.class));
        } catch (BusinessException e) {
            result.setAccomodation(null);
        }

        result.setNote(visit.getNote());
        return result;
    }

    private void copyToVisit(VisitVO original, Visit result) {
        if (original != null) {
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

            if (original.getPatient() != null) {
                result.setPatient(this.entityManager.find(Client.class, original.getPatient().getClientId()));
            } else {
                result.setPatient(null);
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
