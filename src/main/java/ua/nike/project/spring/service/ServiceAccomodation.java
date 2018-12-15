package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Accomodation;
import ua.nike.project.hibernate.type.Ward;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.vo.AccomodationVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ServiceAccomodation {

    @Autowired
    private DAO<Accomodation> dao;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public AccomodationVO findByID(int accomodationID) throws ApplicationException {
        return convertToAccomodationVO(dao.findByID(accomodationID));
    }
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Accomodation findEntytiByID(int entityID) throws ApplicationException {
        return dao.findByID(entityID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AccomodationVO> findAll() {
        List<Accomodation> entities = dao.findAll();
        if (entities == null) return null;
        List<AccomodationVO> result = new ArrayList<>();
        for (Accomodation entity : entities) {
            result.add(convertToAccomodationVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AccomodationVO> findAllActive() {
        List<Accomodation> entities = dao.findAllActive();
        if (entities == null) return null;
        List<AccomodationVO> result = new ArrayList<>();
        for (Accomodation entity : entities) {
            result.add(convertToAccomodationVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<Integer> getActiveWards() {
        List<Ward> wards = dao.getActiveWards();
        if (wards == null) return null;
        List<Integer> result = new ArrayList<>();
        for (Ward ward : wards) result.add(ward.toInteger());
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AccomodationVO create(AccomodationVO accomodationVO) {
        Accomodation entity = copyToAccomodation(accomodationVO, null);
        return convertToAccomodationVO(dao.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AccomodationVO update(int accomodationID, AccomodationVO accomodationVO) throws ApplicationException {
        Accomodation originalEntity = dao.findByID(accomodationID);
        Accomodation updatedEntity = copyToAccomodation(accomodationVO, originalEntity);
        return convertToAccomodationVO(dao.update(updatedEntity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int accomodationID) {
        return dao.remove(accomodationID);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AccomodationVO deactivateByID(int accomodationID) throws ApplicationException {
        Accomodation accomodation = dao.findByID(accomodationID);
        accomodation.setInactive(true);
        return convertToAccomodationVO(dao.update(accomodation));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AccomodationVO activateByID(int accomodationID) throws ApplicationException {
        Accomodation accomodation = dao.findByID(accomodationID);
        accomodation.setInactive(false);
        return convertToAccomodationVO(dao.update(accomodation));
    }

    private AccomodationVO convertToAccomodationVO(Accomodation accomodation) {
        if (accomodation == null) return null;
        AccomodationVO result = new AccomodationVO();
        result.setAccomodationId(accomodation.getAccomodationId());
        result.setWard(accomodation.getWard().toInteger());
        result.setWardPlace(accomodation.getWardPlace());
        result.setInactive(accomodation.isInactive());
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


}














    /*
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<AccomodationVO> getListByQuery(String hqlQuery, Map<String, Object> parameters) throws
            ApplicationException {
        List<Accomodation> entities = dao.getEntitiesByQuery(hqlQuery, parameters);
        List<AccomodationVO> result = new ArrayList<>();
        for (Accomodation entity : entities) {
            result.add(convertToAccomodationVO(entity));
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
//    public Accomodation getEntityByID(int accomodationID) throws ApplicationException {
//        return dao.findByID(accomodationID);
//    }

        private AccomodationVO convertToAccomodationVO(Accomodation entity) throws ApplicationException {
            if (entity == null) return null;
            switch (entity.getClass().getSimpleName()) {
                case "Accomodation":
                    return (AccomodationVO) transformToAccomodationVO((Accomodation) entity);
                case "Visit":
                    return (AccomodationVO) transformToVisitVO((Visit) entity);
                case "Accomodation":
                    return (AccomodationVO) transformToAccomodationVO((Accomodation) entity);
                case "Manager":
                    return (AccomodationVO) transformToManagerVO((Manager) entity);
                case "OperationType":
                    return (AccomodationVO) transformToOperationTypeVO((OperationType) entity);
                case "Surgeon":
                    return (AccomodationVO) transformToSurgeonVO((Surgeon) entity);
                case "VisitDate":
                    return (AccomodationVO) transformToVisitDateVO((VisitDate) entity);

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
            result.setAccomodation(transformToAccomodationVO(visit.getAccomodation()));
            result.setPatient(transformToAccomodationVO(visit.getPatient()));
            result.setStatus(convertAccomodationStatus(visit.getStatus()));
            result.setOperationType(transformToOperationTypeVO(visit.getOperationType()));
            result.setEye(visit.getEye());
            result.setSurgeon(transformToSurgeonVO(visit.getSurgeon()));
            result.setManager(transformToManagerVO(visit.getManager()));
            result.setAccomodation(transformToAccomodationVO(visit.getAccomodation()));
            result.setNote(visit.getNote());
            result.setInactive(visit.getInactive());
            return result;
        }

        private AccomodationVO transformToAccomodationVO(Accomodation client) {
            if (client == null) return null;
            AccomodationVO result = new AccomodationVO();
            result.setAccomodationId(client.getAccomodationId());
            result.setSurname(client.getSurname());
            result.setFirstName(client.getFirstName());
            result.setSecondName(client.getSecondName());
            result.setSex(convertSex(client.getSex()));
            result.setBirthday(client.getBirthday());
            result.setTelephone(client.getTelephone());
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

        private ManagerVO transformToManagerVO(Manager manager) {
            if (manager == null) return null;
            ManagerVO result = new ManagerVO();
            result.setManagerId(manager.getManagerId());
            result.setSurname(manager.getSurname());
            result.setFirstName(manager.getFirstName());
            result.setSecondName(manager.getSecondName());
            result.setSex(convertSex(manager.getSex()));
            result.setCityFrom(manager.getCityFrom());
            result.setInactive(manager.isInactive());
            return result;
        }

        private SurgeonVO transformToSurgeonVO(Surgeon surgeon) {
            if (surgeon == null) return null;
            SurgeonVO result = new SurgeonVO();
            result.setSurgeonId(surgeon.getSurgeonId());
            result.setSurname(surgeon.getSurname());
            result.setFirstName(surgeon.getFirstName());
            result.setSecondName(surgeon.getSecondName());
            result.setSex(convertSex(surgeon.getSex()));
            result.setInactive(surgeon.isInactive());
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

        private Accomodation copyToEntityObject(AccomodationVO accomodationVO, Accomodation entity) throws ApplicationException {
            if (accomodationVO == null) return null;
            switch (accomodationVO.getClass().getSimpleName()) {
                case "AccomodationVO": {
                    if (entity == null) entity = (Accomodation) new Accomodation();
                    return (Accomodation) copyToAccomodation((AccomodationVO) accomodationVO, (Accomodation) entity);
                }
                case "AccomodationVO": {
                    if (entity == null) entity = (Accomodation) new Accomodation();
                    return (Accomodation) copyToAccomodation((AccomodationVO) accomodationVO, (Accomodation) entity);
                }
                case "ManagerVO": {
                    if (entity == null) entity = (Accomodation) new Manager();
                    return (Accomodation) copyToManager((ManagerVO) accomodationVO, (Manager) entity);
                }
                case "OperationTypeVO": {
                    if (entity == null) entity = (Accomodation) new OperationType();
                    return (Accomodation) copyToOperationType((OperationTypeVO) accomodationVO, (OperationType) entity);
                }
                case "SurgeonVO": {
                    if (entity == null) entity = (Accomodation) new Surgeon();
                    return (Accomodation) copyToSurgeon((SurgeonVO) accomodationVO, (Surgeon) entity);
                }
                case "VisitVO": {
                    if (entity == null) entity = (Accomodation) new Visit();
                    return (Accomodation) copyToVisit((VisitVO) accomodationVO, (Visit) entity);
                }
                case "VisitDateVO": {
                    if (entity == null) entity = (Accomodation) new VisitDate();
                    return (Accomodation) copyToVisitDate((VisitDateVO) accomodationVO, (VisitDate) entity);
                }

                default:
                    throw new ApplicationException("Class not find.");
            }
        }

        private Visit copyToVisit(VisitVO original, Visit result) {

            if (original != null) {

                result.setTimeForCome(original.getTimeForCome());
                result.setOrderForCome(original.getOrderForCome());
                result.setStatus(convertAccomodationStatus(original.getStatus()));
                result.setEye(original.getEye());
                result.setNote(original.getNote());
                result.setInactive(original.getInactive());

                if (original.getVisitDate() != null && original.getVisitDate().getVisitDateId() > 0) {
                    VisitDate visitDate = new VisitDate();
                    visitDate.setVisitDateId(original.getVisitDate().getVisitDateId());
                    result.setVisitDate(copyToVisitDate(original.getVisitDate(), visitDate));
                } else result.setVisitDate(null);

                if (original.getPatient() != null && original.getPatient().getAccomodationId() > 0) {
                    Accomodation patient = new Accomodation();
                    patient.setAccomodationId(original.getPatient().getAccomodationId());
                    result.setPatient(copyToAccomodation(original.getPatient(), patient));
                } else result.setPatient(null);

                if (original.getAccomodation() != null && original.getAccomodation().getAccomodationId() > 0) {
                    Accomodation client = new Accomodation();
                    client.setAccomodationId(original.getAccomodation().getAccomodationId());
                    result.setAccomodation(copyToAccomodation(original.getAccomodation(), client));
                } else result.setAccomodation(null);


                if (original.getOperationType() != null && original.getOperationType().getOperationTypeId() > 0) {
                    OperationType operationType = new OperationType();
                    operationType.setOperationTypeId(original.getOperationType().getOperationTypeId());
                    result.setOperationType(copyToOperationType(original.getOperationType(), operationType));
                } else result.setOperationType(null);

                if (original.getSurgeon() != null && original.getSurgeon().getSurgeonId() > 0) {
                    Surgeon surgeon = new Surgeon();
                    surgeon.setSurgeonId(original.getSurgeon().getSurgeonId());
                    result.setSurgeon(copyToSurgeon(original.getSurgeon(), surgeon));
                } else result.setSurgeon(null);

                if (original.getManager() != null && original.getManager().getManagerId() > 0) {
                    Manager manager = new Manager();
                    manager.setManagerId(original.getManager().getManagerId());
                    result.setManager(copyToManager(original.getManager(), manager));
                } else result.setManager(null);

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

        private Surgeon copyToSurgeon(SurgeonVO original, Surgeon result) {
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

        private Manager copyToManager(ManagerVO original, Manager result) {
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

        private Accomodation copyToAccomodation(AccomodationVO original, Accomodation result) {
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

        private AccomodationStatus convertAccomodationStatus(String status) {
            switch (status) {
                case "пацієнт":
                    return AccomodationStatus.PATIENT;
                case "супров.":
                case "супроводжуючий":
                default:
                    return AccomodationStatus.RELATIVE;
            }
        }

        private String convertAccomodationStatus(AccomodationStatus status) {
            if (status == null) return null;

            switch (status.toString().toUpperCase()) {
                case "RELATIVE":
                    return "супров.";
                case "PATIENT":
                default:
                    return "пацієнт";
            }
        }

    private boolean isRelated(Accomodation entity) throws ApplicationException {
//        if (entity == null) throw new ApplicationException("Entity is not correct.");
//        switch (entity.getClass().getSimpleName()) {
//            case "Accomodation": {
//                Map<String, Object> parametersAccomodation = new HashMap<>();
//                parametersAccomodation.put("client", (Accomodation) entity);
//                Map<String, Object> parametersPatient = new HashMap<>();
//                parametersPatient.put("patient", (Accomodation) entity);
//                return ((Long) dao.getObjectByQuery("SELECT count(*) FROM Visit v WHERE v.client = :client", parametersAccomodation) != 0
//                        || dao.getEntitiesByNamedQuery("Visit.findByPatient", parametersPatient, (Class<Accomodation>) Visit.class).size() != 0);
//            }
//            case "Manager": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("manager", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByManager", parameters, (Class<Accomodation>) Visit.class).size() != 0;
//            }
//            case "OperationType": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("operationType", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByOperationType", parameters, (Class<Accomodation>) Visit.class).size() != 0;
//            }
//            case "Surgeon": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("surgeon", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findBySurgeon", parameters, (Class<Accomodation>) Visit.class).size() != 0;
//            }
//            case "VisitDate": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("visitDate", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByVisitDate", parameters, (Class<Accomodation>) Visit.class).size() != 0;
//            }
//            case "Accomodation": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("accomodation", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByAccomodation", parameters, (Class<Accomodation>) Visit.class).size() != 0;
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