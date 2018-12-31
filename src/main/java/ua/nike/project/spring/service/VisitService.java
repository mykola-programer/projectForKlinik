package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Visit;
import ua.nike.project.hibernate.type.ClientStatus;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.vo.VisitVO;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class VisitService {

    private DAO<Visit> dao;
    @Autowired
    private VisitDateService visitDateService;
    @Autowired
    private ClientService clientService;
    @Autowired
    private OperationTypeService operationTypeService;
    @Autowired
    private SurgeonService surgeonService;
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
        List<Visit> entities = dao.findAll("Visit.findAll", null);
        if (entities == null) return null;
        List<VisitVO> result = new ArrayList<>();
        for (Visit entity : entities) {
            result.add(convertToVisitVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitVO> findAllActive() {
        List<Visit> entities = dao.findAll("Visit.findAllActive", null);
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

    @Transactional(propagation = Propagation.REQUIRED)
    public VisitVO deactivateByID(int visitID) {
        Visit visit = dao.findByID(visitID);
        visit.setInactive(true);
        return convertToVisitVO(dao.update(visit));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public VisitVO activateByID(int visitID) {
        Visit visit = dao.findByID(visitID);
        visit.setInactive(false);
        return convertToVisitVO(dao.update(visit));
    }

    public List<VisitVO> getVisitsByDate(LocalDate date) {

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("date", date);

        List<Visit> entities = dao.findAll("Visit.findAllActiveByDate", parameters);
        if (entities == null) return null;
        List<VisitVO> result = new ArrayList<>();
        for (Visit entity : entities) {
            result.add(convertToVisitVO(entity));
        }
        return result;
    }


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

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<VisitVO> displaceVisits(List<VisitVO> visitsVO) {
        List<VisitVO> result = new ArrayList<>();
        for (VisitVO visitVO : visitsVO) {
            if (visitVO != null) {
                visitVO.setAccomodation(null);
                if (visitVO.getVisitId() > 0) {
                    result.add(update(visitVO.getVisitId(), visitVO));
                } else {
                    result.add(create(visitVO));
                }
            }
        }
        return result;
    }

    private Visit copyToVisit(VisitVO original, Visit result) {
        if (original != null) {
            if (result == null) result = new Visit();
            result.setTimeForCome(original.getTimeForCome());
            result.setOrderForCome(original.getOrderForCome());
            result.setStatus(ClientStatus.getInstance(original.getStatus()));
            result.setEye(original.getEye());
            result.setNote(original.getNote());
            result.setInactive(original.getInactive() != null ? original.getInactive() : false);

            if (original.getVisitDate() != null && original.getVisitDate().getVisitDateId() > 0) {
                result.setVisitDate(visitDateService.findEntityByID(original.getVisitDate().getVisitDateId()));
            } else result.setVisitDate(null);


            if (original.getPatient() != null && original.getPatient().getClientId() > 0 && !original.getStatus().equals("пацієнт")) {
                result.setPatient(clientService.findEntityByID(original.getPatient().getClientId()));
            } else result.setPatient(null);


            if (original.getClient() != null && original.getClient().getClientId() > 0) {
                result.setClient(clientService.findEntityByID(original.getClient().getClientId()));
            } else result.setClient(null);


            if (original.getOperationType() != null && original.getOperationType().getOperationTypeId() > 0) {
                result.setOperationType(operationTypeService.findEntityByID(original.getOperationType().getOperationTypeId()));
            } else result.setOperationType(null);


            if (original.getSurgeon() != null && original.getSurgeon().getSurgeonId() > 0) {
                result.setSurgeon(surgeonService.findEntityByID(original.getSurgeon().getSurgeonId()));
            } else result.setSurgeon(null);


            if (original.getManager() != null && original.getManager().getManagerId() > 0) {
                result.setManager(managerService.findEntytiByID(original.getManager().getManagerId()));
            } else result.setManager(null);

            if (original.getAccomodation() != null && original.getAccomodation().getAccomodationId() > 0) {
                result.setAccomodation(accomodationService.findEntityByID(original.getAccomodation().getAccomodationId()));
            } else result.setAccomodation(null);
        }
        return result;
    }

    private VisitVO convertToVisitVO(Visit visit) {
        if (visit == null) return null;

        VisitVO result = new VisitVO();
        result.setVisitId(visit.getVisitId());
        result.setTimeForCome(visit.getTimeForCome());
        result.setOrderForCome(visit.getOrderForCome());
        result.setEye(visit.getEye());
        result.setStatus(visit.getStatus().convertToCyrillic());
        result.setNote(visit.getNote());
        result.setInactive(visit.getInactive());

        if (visit.getVisitDate() != null) {
            result.setVisitDate(visitDateService.findByID(visit.getVisitDate().getVisitDateId()));
        } else result.setVisitDate(null);

        if (visit.getClient() != null) {
            result.setClient(clientService.findByID(visit.getClient().getClientId()));
        } else result.setClient(null);

        if (visit.getPatient() != null && visit.getStatus() != null && visit.getStatus().toString().equals(ClientStatus.RELATIVE.toString())) {
            result.setPatient(clientService.findByID(visit.getPatient().getClientId()));
        } else result.setPatient(null);


        if (visit.getOperationType() != null) {
            result.setOperationType(operationTypeService.findByID(visit.getOperationType().getOperationTypeId()));
        } else result.setOperationType(null);


        if (visit.getSurgeon() != null) {
            result.setSurgeon(surgeonService.findByID(visit.getSurgeon().getSurgeonId()));
        } else result.setSurgeon(null);


        if (visit.getManager() != null) {
            result.setManager(managerService.findByID(visit.getManager().getManagerId()));
        } else result.setManager(null);

        if (visit.getAccomodation() != null) {
            result.setAccomodation(accomodationService.findByID(visit.getAccomodation().getAccomodationId()));
        } else result.setAccomodation(null);


        return result;
    }
}














    /*
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
                case "VisitDate":
                    return (VisitVO) transformToVisitDateVO((VisitDate) entity);

                default:
                    throw new ApplicationException("Class not find.");
            }
        }

        private VisitVO transformToVisitVO(Visit visit) {
            if (visit == null) return null;

            VisitVO result = new VisitVO();
            result.setVisitId(visit.getVisitId());
            result.setVisitDate(transformToVisitDateVO(visit.getVisitDate()));
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
            result.setInactive(visit.getInactive());
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
            result.setInactive(accomodation.isInactive());
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
            result.setCityFrom(visit.getCityFrom());
            result.setInactive(visit.isInactive());
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
            result.setInactive(visit.isInactive());
            return result;
        }

        private OperationTypeVO transformToOperationTypeVO(OperationType operationType) {
            if (operationType == null) return null;
            OperationTypeVO operationTypeVO = new OperationTypeVO();
            operationTypeVO.setOperationTypeId(operationType.getOperationTypeId());
            operationTypeVO.setName(operationType.getName());
            operationTypeVO.setInactive(operationType.isInactive());
            return operationTypeVO;
        }

        private VisitDateVO transformToVisitDateVO(VisitDate visitDate) {
            if (visitDate == null) return null;
            VisitDateVO result = new VisitDateVO();
            result.setVisitDateId(visitDate.getVisitDateId());
            result.setDate(visitDate.getDate());
            result.setInactive(visitDate.isInactive());
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
                case "VisitDateVO": {
                    if (entity == null) entity = (Visit) new VisitDate();
                    return (Visit) copyToVisitDate((VisitDateVO) visitVO, (VisitDate) entity);
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
                result.setInactive(original.getInactive());

                if (original.getVisitDate() != null && original.getVisitDate().getVisitDateId() > 0) {
                    VisitDate visitDate = new VisitDate();
                    visitDate.setVisitDateId(original.getVisitDate().getVisitDateId());
                    result.setVisitDate(copyToVisitDate(original.getVisitDate(), visitDate));
                } else result.setVisitDate(null);

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

        private VisitDate copyToVisitDate(VisitDateVO original, VisitDate result) {
            if (original != null) {
                result.setDate(original.getDate());
                result.setInactive(original.isInactive());
            }
            return result;
        }

        private Visit copyToVisit(VisitVO original, Visit result) {
            if (original != null) {
                result.setSurname(original.getSurname());
                result.setFirstName(original.getFirstName());
                result.setSecondName(original.getSecondName());
                result.setSex(convertSex(original.getSex()));
                result.setInactive(original.isInactive());
            }
            return result;
        }

        private OperationType copyToOperationType(OperationTypeVO original, OperationType result) {
            if (original != null) {
                result.setName(original.getName());
                result.setInactive(original.isInactive());
            }
            return result;
        }

        private Visit copyToVisit(VisitVO original, Visit result) {
            if (original != null) {
                result.setSurname(original.getSurname());
                result.setFirstName(original.getFirstName());
                result.setSecondName(original.getSecondName());
                result.setCityFrom(original.getCityFrom());
                result.setSex(convertSex(original.getSex()));
                result.setInactive(original.isInactive());
            }
            return result;
        }

        private Accomodation copyToAccomodation(AccomodationVO original, Accomodation result) {
            if (original != null) {
                result.setWard(Ward.valueOf("N" + original.getWard()));
                result.setWardPlace(original.getWardPlace());
                result.setInactive(original.isInactive());
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
//            case "VisitDate": {
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