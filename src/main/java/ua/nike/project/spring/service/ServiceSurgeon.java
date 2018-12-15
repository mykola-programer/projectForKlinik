package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Surgeon;
import ua.nike.project.hibernate.type.Sex;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.vo.SurgeonVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ServiceSurgeon {

    @Autowired
    private DAO<Surgeon> dao;

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public SurgeonVO findByID(int surgeonID) throws ApplicationException {
        return convertToSurgeonVO(dao.findByID(surgeonID));
    }
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Surgeon findEntytiByID(int entityID) throws ApplicationException {
        return dao.findByID(entityID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SurgeonVO> findAll() {
        List<Surgeon> entities = dao.findAll();
        if (entities == null) return null;
        List<SurgeonVO> result = new ArrayList<>();
        for (Surgeon entity : entities) {
            result.add(convertToSurgeonVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SurgeonVO> findAllActive() {
        List<Surgeon> entities = dao.findAllActive();
        if (entities == null) return null;
        List<SurgeonVO> result = new ArrayList<>();
        for (Surgeon entity : entities) {
            result.add(convertToSurgeonVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public SurgeonVO create(SurgeonVO surgeonVO) {
        Surgeon entity = copyToSurgeon(surgeonVO, null);
        return convertToSurgeonVO(dao.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SurgeonVO update(int surgeonID, SurgeonVO surgeonVO) throws ApplicationException {
        Surgeon originalEntity = dao.findByID(surgeonID);
        Surgeon updatedEntity = copyToSurgeon(surgeonVO, originalEntity);
        return convertToSurgeonVO(dao.update(updatedEntity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int surgeonID) {
        return dao.remove(surgeonID);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SurgeonVO deactivateByID(int surgeonID) throws ApplicationException {
        Surgeon surgeon = dao.findByID(surgeonID);
        surgeon.setInactive(true);
        return convertToSurgeonVO(dao.update(surgeon));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public SurgeonVO activateByID(int surgeonID) throws ApplicationException {
        Surgeon surgeon = dao.findByID(surgeonID);
        surgeon.setInactive(false);
        return convertToSurgeonVO(dao.update(surgeon));
    }

    private SurgeonVO convertToSurgeonVO(Surgeon surgeon) {
        if (surgeon == null) return null;
        SurgeonVO result = new SurgeonVO();
        result.setSurgeonId(surgeon.getSurgeonId());
        result.setSurname(surgeon.getSurname());
        result.setFirstName(surgeon.getFirstName());
        result.setSecondName(surgeon.getSecondName());
        result.setSex(surgeon.getSex().toCharacter());
        result.setInactive(surgeon.isInactive());
        return result;
    }

    private Surgeon copyToSurgeon(SurgeonVO original, Surgeon result) {
        if (original != null) {
            result.setSurname(original.getSurname());
            result.setFirstName(original.getFirstName());
            result.setSecondName(original.getSecondName());
            result.setSex(Sex.getInstance(original.getSex()));
            result.setInactive(original.isInactive());
        }
        return result;
    }

}














    /*
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<SurgeonVO> getListByQuery(String hqlQuery, Map<String, Object> parameters) throws
            ApplicationException {
        List<Surgeon> entities = dao.getEntitiesByQuery(hqlQuery, parameters);
        List<SurgeonVO> result = new ArrayList<>();
        for (Surgeon entity : entities) {
            result.add(convertToSurgeonVO(entity));
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
//    public Surgeon getEntityByID(int surgeonID) throws ApplicationException {
//        return dao.findByID(surgeonID);
//    }

        private SurgeonVO convertToSurgeonVO(Surgeon entity) throws ApplicationException {
            if (entity == null) return null;
            switch (entity.getClass().getSimpleName()) {
                case "Surgeon":
                    return (SurgeonVO) transformToSurgeonVO((Surgeon) entity);
                case "Visit":
                    return (SurgeonVO) transformToVisitVO((Visit) entity);
                case "Accomodation":
                    return (SurgeonVO) transformToAccomodationVO((Accomodation) entity);
                case "Surgeon":
                    return (SurgeonVO) transformToSurgeonVO((Surgeon) entity);
                case "OperationType":
                    return (SurgeonVO) transformToOperationTypeVO((OperationType) entity);
                case "Surgeon":
                    return (SurgeonVO) transformToSurgeonVO((Surgeon) entity);
                case "VisitDate":
                    return (SurgeonVO) transformToVisitDateVO((VisitDate) entity);

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
            result.setSurgeon(transformToSurgeonVO(visit.getSurgeon()));
            result.setPatient(transformToSurgeonVO(visit.getPatient()));
            result.setStatus(convertSurgeonStatus(visit.getStatus()));
            result.setOperationType(transformToOperationTypeVO(visit.getOperationType()));
            result.setEye(visit.getEye());
            result.setSurgeon(transformToSurgeonVO(visit.getSurgeon()));
            result.setSurgeon(transformToSurgeonVO(visit.getSurgeon()));
            result.setAccomodation(transformToAccomodationVO(visit.getAccomodation()));
            result.setNote(visit.getNote());
            result.setInactive(visit.getInactive());
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
            result.setBirthday(surgeon.getBirthday());
            result.setTelephone(surgeon.getTelephone());
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

        private SurgeonVO transformToSurgeonVO(Surgeon surgeon) {
            if (surgeon == null) return null;
            SurgeonVO result = new SurgeonVO();
            result.setSurgeonId(surgeon.getSurgeonId());
            result.setSurname(surgeon.getSurname());
            result.setFirstName(surgeon.getFirstName());
            result.setSecondName(surgeon.getSecondName());
            result.setSex(convertSex(surgeon.getSex()));
            result.setCityFrom(surgeon.getCityFrom());
            result.setInactive(surgeon.isInactive());
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

        private Surgeon copyToEntityObject(SurgeonVO surgeonVO, Surgeon entity) throws ApplicationException {
            if (surgeonVO == null) return null;
            switch (surgeonVO.getClass().getSimpleName()) {
                case "SurgeonVO": {
                    if (entity == null) entity = (Surgeon) new Surgeon();
                    return (Surgeon) copyToSurgeon((SurgeonVO) surgeonVO, (Surgeon) entity);
                }
                case "AccomodationVO": {
                    if (entity == null) entity = (Surgeon) new Accomodation();
                    return (Surgeon) copyToAccomodation((AccomodationVO) surgeonVO, (Accomodation) entity);
                }
                case "SurgeonVO": {
                    if (entity == null) entity = (Surgeon) new Surgeon();
                    return (Surgeon) copyToSurgeon((SurgeonVO) surgeonVO, (Surgeon) entity);
                }
                case "OperationTypeVO": {
                    if (entity == null) entity = (Surgeon) new OperationType();
                    return (Surgeon) copyToOperationType((OperationTypeVO) surgeonVO, (OperationType) entity);
                }
                case "SurgeonVO": {
                    if (entity == null) entity = (Surgeon) new Surgeon();
                    return (Surgeon) copyToSurgeon((SurgeonVO) surgeonVO, (Surgeon) entity);
                }
                case "VisitVO": {
                    if (entity == null) entity = (Surgeon) new Visit();
                    return (Surgeon) copyToVisit((VisitVO) surgeonVO, (Visit) entity);
                }
                case "VisitDateVO": {
                    if (entity == null) entity = (Surgeon) new VisitDate();
                    return (Surgeon) copyToVisitDate((VisitDateVO) surgeonVO, (VisitDate) entity);
                }

                default:
                    throw new ApplicationException("Class not find.");
            }
        }

        private Visit copyToVisit(VisitVO original, Visit result) {

            if (original != null) {

                result.setTimeForCome(original.getTimeForCome());
                result.setOrderForCome(original.getOrderForCome());
                result.setStatus(convertSurgeonStatus(original.getStatus()));
                result.setEye(original.getEye());
                result.setNote(original.getNote());
                result.setInactive(original.getInactive());

                if (original.getVisitDate() != null && original.getVisitDate().getVisitDateId() > 0) {
                    VisitDate visitDate = new VisitDate();
                    visitDate.setVisitDateId(original.getVisitDate().getVisitDateId());
                    result.setVisitDate(copyToVisitDate(original.getVisitDate(), visitDate));
                } else result.setVisitDate(null);

                if (original.getPatient() != null && original.getPatient().getSurgeonId() > 0) {
                    Surgeon patient = new Surgeon();
                    patient.setSurgeonId(original.getPatient().getSurgeonId());
                    result.setPatient(copyToSurgeon(original.getPatient(), patient));
                } else result.setPatient(null);

                if (original.getSurgeon() != null && original.getSurgeon().getSurgeonId() > 0) {
                    Surgeon surgeon = new Surgeon();
                    surgeon.setSurgeonId(original.getSurgeon().getSurgeonId());
                    result.setSurgeon(copyToSurgeon(original.getSurgeon(), surgeon));
                } else result.setSurgeon(null);


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

                if (original.getSurgeon() != null && original.getSurgeon().getSurgeonId() > 0) {
                    Surgeon surgeon = new Surgeon();
                    surgeon.setSurgeonId(original.getSurgeon().getSurgeonId());
                    result.setSurgeon(copyToSurgeon(original.getSurgeon(), surgeon));
                } else result.setSurgeon(null);

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

        private Surgeon copyToSurgeon(SurgeonVO original, Surgeon result) {
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

        private Surgeon copyToSurgeon(SurgeonVO original, Surgeon result) {
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

        private SurgeonStatus convertSurgeonStatus(String status) {
            switch (status) {
                case "пацієнт":
                    return SurgeonStatus.PATIENT;
                case "супров.":
                case "супроводжуючий":
                default:
                    return SurgeonStatus.RELATIVE;
            }
        }

        private String convertSurgeonStatus(SurgeonStatus status) {
            if (status == null) return null;

            switch (status.toString().toUpperCase()) {
                case "RELATIVE":
                    return "супров.";
                case "PATIENT":
                default:
                    return "пацієнт";
            }
        }

    private boolean isRelated(Surgeon entity) throws ApplicationException {
//        if (entity == null) throw new ApplicationException("Entity is not correct.");
//        switch (entity.getClass().getSimpleName()) {
//            case "Surgeon": {
//                Map<String, Object> parametersSurgeon = new HashMap<>();
//                parametersSurgeon.put("surgeon", (Surgeon) entity);
//                Map<String, Object> parametersPatient = new HashMap<>();
//                parametersPatient.put("patient", (Surgeon) entity);
//                return ((Long) dao.getObjectByQuery("SELECT count(*) FROM Visit v WHERE v.surgeon = :surgeon", parametersSurgeon) != 0
//                        || dao.getEntitiesByNamedQuery("Visit.findByPatient", parametersPatient, (Class<Surgeon>) Visit.class).size() != 0);
//            }
//            case "Surgeon": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("surgeon", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findBySurgeon", parameters, (Class<Surgeon>) Visit.class).size() != 0;
//            }
//            case "OperationType": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("operationType", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByOperationType", parameters, (Class<Surgeon>) Visit.class).size() != 0;
//            }
//            case "Surgeon": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("surgeon", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findBySurgeon", parameters, (Class<Surgeon>) Visit.class).size() != 0;
//            }
//            case "VisitDate": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("visitDate", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByVisitDate", parameters, (Class<Surgeon>) Visit.class).size() != 0;
//            }
//            case "Accomodation": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("accomodation", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByAccomodation", parameters, (Class<Surgeon>) Visit.class).size() != 0;
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