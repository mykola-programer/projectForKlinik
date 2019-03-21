package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.hibernate.type.ClientStatus;
import ua.nike.project.hibernate.type.Eye;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.vo.VisitVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class VisitService {

    private DAO<Visit> dao;
    @Autowired
    private DatePlanService datePlanService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private OperationTypeService operationTypeService;
    @Autowired
    private SurgeonPlanService surgeonPlanService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private AccomodationService accomodationService;

    @Autowired
    public void setDao(DAO<Visit> dao) {
        this.dao = dao;
        this.dao.setClassEO(Visit.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public VisitVO findByID(int visitID) {
        return convertToVisitVO(dao.findByID(visitID));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitVO> findAll() {
        List<Visit> entities = dao.findAll("Visit.findAll");
        if (entities == null) return null;
        List<VisitVO> result = new ArrayList<>();
        for (Visit entity : entities) {
            result.add(convertToVisitVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public VisitVO create(VisitVO visitVO) {
        Visit entity = copyToVisit(visitVO, null);
        return convertToVisitVO(dao.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public VisitVO update(int visitID, VisitVO visitVO) {
        Visit originalEntity = dao.findByID(visitID);
        Visit updatedEntity = copyToVisit(visitVO, originalEntity);
        return convertToVisitVO(dao.update(updatedEntity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int visitID) {
        return dao.remove(visitID);
    }


    public List<VisitVO> getVisitsByDate(LocalDate date) {
        List<Visit> entities = dao.findAll("Visit.findAllByDate", new Object[]{date});
        if (entities == null) return null;
        List<VisitVO> result = new ArrayList<>();
        for (Visit entity : entities) {
            result.add(convertToVisitVO(entity));
        }
        return result;
    }

    private Visit copyToVisit(VisitVO original, Visit result) {
        if (original != null) {
            if (result == null) result = new Visit();
            result.setTimeForCome(original.getTimeForCome());
            result.setOrderForCome(original.getOrderForCome());
            result.setStatus(ClientStatus.getInstance(original.getStatus()));
            result.setNote(original.getNote());

            if (original.getSurgeonPlanId() > 0) {
                result.setSurgeonPlan(surgeonPlanService.findEntityByID(original.getSurgeonPlanId()));
            } else result.setSurgeonPlan(null);

            if (original.getClientID() > 0) {
                result.setClient(clientService.findEntityByID(original.getClientID()));
            } else result.setClient(null);

            if (original.getAccomodationID() > 0) {
                result.setAccomodation(accomodationService.findEntityByID(original.getAccomodationID()));
            } else result.setAccomodation(null);

            if (original.getManagerID() > 0) {
                result.setManager(managerService.findEntityByID(original.getManagerID()));
            } else result.setManager(null);

            if (original.getPatientID() > 0 && !original.getStatus().equals("пацієнт")) {
                result.setPatient(clientService.findEntityByID(original.getPatientID()));
            } else result.setPatient(null);

            if (original.getOperationTypeID() > 0 && original.getStatus().equals("пацієнт")) {
                result.setOperationType(operationTypeService.findEntityByID(original.getOperationTypeID()));
            } else result.setOperationType(null);

            if (original.getEye() != null && original.getStatus().equals("пацієнт")) {
                result.setEye(Eye.valueOf(original.getEye()));
            } else result.setEye(null);
        }
        return result;
    }

    private VisitVO convertToVisitVO(Visit visit) {
        if (visit == null) return null;

        VisitVO result = new VisitVO();
        result.setVisitId(visit.getVisitId());
        result.setTimeForCome(visit.getTimeForCome());
        result.setOrderForCome(visit.getOrderForCome());
        result.setStatus(visit.getStatus().convertToCyrillic());
        result.setNote(visit.getNote());

        if (visit.getClient() != null) {
            result.setClientID(visit.getClient().getClientId());
        } else result.setClientID(0);

        if (visit.getAccomodation() != null) {
            result.setAccomodationID(visit.getAccomodation().getAccomodationId());
        } else result.setAccomodationID(0);

        if (visit.getManager() != null) {
            result.setManagerID(visit.getManager().getManagerId());
        } else result.setManagerID(0);

        if (visit.getPatient() != null && visit.getStatus() != null && visit.getStatus().toString().equals(ClientStatus.RELATIVE.toString())) {
            result.setPatientID(visit.getPatient().getClientId());
        } else result.setPatientID(0);

        if (visit.getOperationType() != null && visit.getStatus() != null && visit.getStatus().toString().equals(ClientStatus.PATIENT.toString())) {
            result.setOperationTypeID(visit.getOperationType().getOperationTypeId());
        } else result.setOperationTypeID(0);

        if (visit.getSurgeonPlan() != null && visit.getStatus() != null && visit.getStatus().toString().equals(ClientStatus.PATIENT.toString())) {
            result.setSurgeonPlanId(visit.getSurgeonPlan().getSurgeonPlanId());
        } else result.setSurgeonPlanId(0);

        if (visit.getEye() != null && visit.getStatus() != null && visit.getStatus().toString().equals(ClientStatus.PATIENT.toString())) {
            result.setEye(visit.getEye().toString());
        } else result.setEye(null);
        return result;
    }
}














    /*




    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<VisitVO> putVisits(List<VisitVO> visitsVO) {
        List<VisitVO> result = new ArrayList<>();
        for (VisitVO visitVO : visitsVO) {
            if (visitVO != null) {
                if (visitVO.getVisitId() > 0) {
                    result.add(update(visitVO.getVisitId(), visitVO));
                } else {
                    result.add(create(visitVO));
                }
            }
        }
        return result;
    }



    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitVO> getListByQuery(String hqlQuery, Map<String, Object> parameters) throws
            ApplicationException {
        List<Visit> entities = dao.getEntitiesByQuery(hqlQuery, parameters);
        List<VisitVO> result = new ArrayList<>();
        for (Visit entity : entities) {
            result.add(convertToVisitVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<? extends Object> getObjectsByQuery(String hqlQuery, Map<String, Object> parameters, Class<? extends
            Object> oClass) {
        return dao.getObjectsByQuery(hqlQuery, parameters, oClass);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Object getObjectByQuery(String hqlQuery, Map<String, Object> parameters) {
        return dao.getObjectByQuery(hqlQuery, parameters);
    }
*/
    /*
    
    
//    @Override
//    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
//    public Visit getEntityByID(int visitID) throws ApplicationException {
//        return dao.findByID(visitID);
//    }

        private VisitVO convertToVisitVO(Visit entity) throws ApplicationException {
            if (entity == null) return null;
            switch (entity.getClass().getSimpleName()) {
                case "Visit":
                    return (VisitVO) transformToVisitVO((Visit) entity);
                case "Visit":
                    return (VisitVO) transformToVisitVO((Visit) entity);
                case "Accomodation":
                    return (VisitVO) transformToAccomodationVO((Accomodation) entity);
                case "Visit":
                    return (VisitVO) transformToVisitVO((Visit) entity);
                case "OperationType":
                    return (VisitVO) transformToOperationTypeVO((OperationType) entity);
                case "Visit":
                    return (VisitVO) transformToVisitVO((Visit) entity);
                case "DatePlan":
                    return (VisitVO) transformToVisitDateVO((DatePlan) entity);

                default:
                    throw new ApplicationException("Class not find.");
            }
        }

        private VisitVO transformToVisitVO(Visit visit) {
            if (visit == null) return null;

            VisitVO result = new VisitVO();
            result.setVisitId(visit.getVisitId());
            result.setDatePlan(transformToVisitDateVO(visit.getDatePlan()));
            result.setTimeForCome(visit.getTimeForCome());
            result.setOrderForCome(visit.getOrderForCome());
            result.setVisit(transformToVisitVO(visit.getVisit()));
            result.setPatient(transformToVisitVO(visit.getPatient()));
            result.setStatus(convertVisitStatus(visit.getStatus()));
            result.setOperationType(transformToOperationTypeVO(visit.getOperationType()));
            result.setEye(visit.getEye());
            result.setVisit(transformToVisitVO(visit.getVisit()));
            result.setVisit(transformToVisitVO(visit.getVisit()));
            result.setAccomodation(transformToAccomodationVO(visit.getAccomodation()));
            result.setNote(visit.getNote());
            result.setDisable(visit.isDisable());
            return result;
        }

        private VisitVO transformToVisitVO(Visit visit) {
            if (visit == null) return null;
            VisitVO result = new VisitVO();
            result.setVisitId(visit.getVisitId());
            result.setSurname(visit.getSurname());
            result.setFirstName(visit.getFirstName());
            result.setSecondName(visit.getSecondName());
            result.setSex(convertSex(visit.getSex()));
            result.setBirthday(visit.getBirthday());
            result.setTelephone(visit.getTelephone());
            return result;
        }

        private AccomodationVO transformToAccomodationVO(Accomodation accomodation) {
            if (accomodation == null) return null;
            AccomodationVO result = new AccomodationVO();
            result.setAccomodationId(accomodation.getAccomodationId());
            result.setWard(Integer.valueOf(accomodation.getWard().toString().substring(1)));
            result.setWardPlace(accomodation.getWardPlace());
            result.setDisable(accomodation.isDisable());
            return result;
        }

        private VisitVO transformToVisitVO(Visit visit) {
            if (visit == null) return null;
            VisitVO result = new VisitVO();
            result.setVisitId(visit.getVisitId());
            result.setSurname(visit.getSurname());
            result.setFirstName(visit.getFirstName());
            result.setSecondName(visit.getSecondName());
            result.setSex(convertSex(visit.getSex()));
            result.setCity(visit.getCity());
            result.setDisable(visit.isDisable());
            return result;
        }

        private VisitVO transformToVisitVO(Visit visit) {
            if (visit == null) return null;
            VisitVO result = new VisitVO();
            result.setVisitId(visit.getVisitId());
            result.setSurname(visit.getSurname());
            result.setFirstName(visit.getFirstName());
            result.setSecondName(visit.getSecondName());
            result.setSex(convertSex(visit.getSex()));
            result.setDisable(visit.isDisable());
            return result;
        }

        private OperationTypeVO transformToOperationTypeVO(OperationType operationType) {
            if (operationType == null) return null;
            OperationTypeVO operationTypeVO = new OperationTypeVO();
            operationTypeVO.setOperationTypeId(operationType.getOperationTypeId());
            operationTypeVO.setName(operationType.getName());
            operationTypeVO.setDisable(operationType.isDisable());
            return operationTypeVO;
        }

        private DatePlanVO transformToVisitDateVO(DatePlan visitDate) {
            if (visitDate == null) return null;
            DatePlanVO result = new DatePlanVO();
            result.setDatePlanId(visitDate.getDatePlanId());
            result.setDate(visitDate.getDate());
            result.setDisable(visitDate.isDisable());
            return result;
        }

        private Visit copyToEntityObject(VisitVO visitVO, Visit entity) throws ApplicationException {
            if (visitVO == null) return null;
            switch (visitVO.getClass().getSimpleName()) {
                case "VisitVO": {
                    if (entity == null) entity = (Visit) new Visit();
                    return (Visit) copyToVisit((VisitVO) visitVO, (Visit) entity);
                }
                case "AccomodationVO": {
                    if (entity == null) entity = (Visit) new Accomodation();
                    return (Visit) copyToAccomodation((AccomodationVO) visitVO, (Accomodation) entity);
                }
                case "VisitVO": {
                    if (entity == null) entity = (Visit) new Visit();
                    return (Visit) copyToVisit((VisitVO) visitVO, (Visit) entity);
                }
                case "OperationTypeVO": {
                    if (entity == null) entity = (Visit) new OperationType();
                    return (Visit) copyToOperationType((OperationTypeVO) visitVO, (OperationType) entity);
                }
                case "VisitVO": {
                    if (entity == null) entity = (Visit) new Visit();
                    return (Visit) copyToVisit((VisitVO) visitVO, (Visit) entity);
                }
                case "VisitVO": {
                    if (entity == null) entity = (Visit) new Visit();
                    return (Visit) copyToVisit((VisitVO) visitVO, (Visit) entity);
                }
                case "DatePlanVO": {
                    if (entity == null) entity = (Visit) new DatePlan();
                    return (Visit) copyToVisitDate((DatePlanVO) visitVO, (DatePlan) entity);
                }

                default:
                    throw new ApplicationException("Class not find.");
            }
        }

        private Visit copyToVisit(VisitVO original, Visit result) {

            if (original != null) {

                result.setTimeForCome(original.getTimeForCome());
                result.setOrderForCome(original.getOrderForCome());
                result.setStatus(convertVisitStatus(original.getStatus()));
                result.setEye(original.getEye());
                result.setNote(original.getNote());
                result.setDisable(original.isDisable());

                if (original.getDatePlan() != null && original.getDatePlan().getDatePlanId() > 0) {
                    DatePlan visitDate = new DatePlan();
                    visitDate.setDatePlanId(original.getDatePlan().getDatePlanId());
                    result.setDatePlan(copyToVisitDate(original.getDatePlan(), visitDate));
                } else result.setDatePlan(null);

                if (original.getPatient() != null && original.getPatient().getVisitId() > 0) {
                    Visit patient = new Visit();
                    patient.setVisitId(original.getPatient().getVisitId());
                    result.setPatient(copyToVisit(original.getPatient(), patient));
                } else result.setPatient(null);

                if (original.getVisit() != null && original.getVisit().getVisitId() > 0) {
                    Visit visit = new Visit();
                    visit.setVisitId(original.getVisit().getVisitId());
                    result.setVisit(copyToVisit(original.getVisit(), visit));
                } else result.setVisit(null);


                if (original.getOperationType() != null && original.getOperationType().getOperationTypeId() > 0) {
                    OperationType operationType = new OperationType();
                    operationType.setOperationTypeId(original.getOperationType().getOperationTypeId());
                    result.setOperationType(copyToOperationType(original.getOperationType(), operationType));
                } else result.setOperationType(null);

                if (original.getVisit() != null && original.getVisit().getVisitId() > 0) {
                    Visit visit = new Visit();
                    visit.setVisitId(original.getVisit().getVisitId());
                    result.setVisit(copyToVisit(original.getVisit(), visit));
                } else result.setVisit(null);

                if (original.getVisit() != null && original.getVisit().getVisitId() > 0) {
                    Visit visit = new Visit();
                    visit.setVisitId(original.getVisit().getVisitId());
                    result.setVisit(copyToVisit(original.getVisit(), visit));
                } else result.setVisit(null);

                if (original.getAccomodation() != null && original.getAccomodation().getAccomodationId() > 0) {
                    Accomodation accomodation = new Accomodation();
                    accomodation.setAccomodationId(original.getAccomodation().getAccomodationId());
                    result.setAccomodation(copyToAccomodation(original.getAccomodation(), accomodation));
                } else result.setAccomodation(null);

            }
            return result;
        }

        private DatePlan copyToVisitDate(DatePlanVO original, DatePlan result) {
            if (original != null) {
                result.setDate(original.getDate());
                result.setDisable(original.isDisable());
            }
            return result;
        }

        private Visit copyToVisit(VisitVO original, Visit result) {
            if (original != null) {
                result.setSurname(original.getSurname());
                result.setFirstName(original.getFirstName());
                result.setSecondName(original.getSecondName());
                result.setSex(convertSex(original.getSex()));
                result.setDisable(original.isDisable());
            }
            return result;
        }

        private OperationType copyToOperationType(OperationTypeVO original, OperationType result) {
            if (original != null) {
                result.setName(original.getName());
                result.setDisable(original.isDisable());
            }
            return result;
        }

        private Visit copyToVisit(VisitVO original, Visit result) {
            if (original != null) {
                result.setSurname(original.getSurname());
                result.setFirstName(original.getFirstName());
                result.setSecondName(original.getSecondName());
                result.setCity(original.getCity());
                result.setSex(convertSex(original.getSex()));
                result.setDisable(original.isDisable());
            }
            return result;
        }

        private Accomodation copyToAccomodation(AccomodationVO original, Accomodation result) {
            if (original != null) {
                result.setWard(Ward.valueOf("N" + original.getWard()));
                result.setWardPlace(original.getWardPlace());
                result.setDisable(original.isDisable());
            }
            return result;
        }

        private Visit copyToVisit(VisitVO original, Visit result) {
            if (original != null) {
                result.setSurname(original.getSurname());
                result.setFirstName(original.getFirstName());
                result.setSecondName(original.getSecondName());
                result.setSex(convertSex(original.getSex()));
                result.setBirthday(original.getBirthday());
                result.setTelephone(original.getTelephone());
            }
            return result;
        }

        private Character convertSex(Sex sex) {
            if (sex == null) return null;
            try {
                switch (sex.toString().charAt(0)) {
                    case 'W':
                    case 'w':
                        return 'Ж';
                    case 'M':
                    case 'm':
                    default:
                        return 'Ч';
                }
            } catch (IndexOutOfBoundsException e) {
                return 'Ч';
            }
        }

        private Sex convertSex(Character symbol) {
            if (symbol == null) return null;
            switch (symbol) {
                case 'Ж':
                case 'ж':
                    return Sex.W;
                case 'Ч':
                case 'ч':
                default:
                    return Sex.M;
            }
        }

        private VisitStatus convertVisitStatus(String status) {
            switch (status) {
                case "пацієнт":
                    return VisitStatus.PATIENT;
                case "супров.":
                case "супроводжуючий":
                default:
                    return VisitStatus.RELATIVE;
            }
        }

        private String convertVisitStatus(VisitStatus status) {
            if (status == null) return null;

            switch (status.toString().toUpperCase()) {
                case "RELATIVE":
                    return "супров.";
                case "PATIENT":
                default:
                    return "пацієнт";
            }
        }

    private boolean isRelated(Visit entity) throws ApplicationException {
//        if (entity == null) throw new ApplicationException("Entity is not correct.");
//        switch (entity.getClass().getSimpleName()) {
//            case "Visit": {
//                Map<String, Object> parametersVisit = new HashMap<>();
//                parametersVisit.put("visit", (Visit) entity);
//                Map<String, Object> parametersPatient = new HashMap<>();
//                parametersPatient.put("patient", (Visit) entity);
//                return ((Long) dao.getObjectByQuery("SELECT count(*) FROM Visit v WHERE v.visit = :visit", parametersVisit) != 0
//                        || dao.getEntitiesByNamedQuery("Visit.findByPatient", parametersPatient, (Class<Visit>) Visit.class).size() != 0);
//            }
//            case "Visit": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("visit", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByVisit", parameters, (Class<Visit>) Visit.class).size() != 0;
//            }
//            case "OperationType": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("operationType", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByOperationType", parameters, (Class<Visit>) Visit.class).size() != 0;
//            }
//            case "Visit": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("visit", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByVisit", parameters, (Class<Visit>) Visit.class).size() != 0;
//            }
//            case "DatePlan": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("visitDate", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByVisitDate", parameters, (Class<Visit>) Visit.class).size() != 0;
//            }
//            case "Accomodation": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("accomodation", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByAccomodation", parameters, (Class<Visit>) Visit.class).size() != 0;
//            }
//            case "Visit": {
//                return false;
//            }
////-----------------------------------------------------------
//
//            default:
//                throw new ApplicationException("Class not find.");
//        }
        return true;
    }
*/