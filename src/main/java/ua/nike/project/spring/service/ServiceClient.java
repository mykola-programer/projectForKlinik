package ua.nike.project.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ua.nike.project.hibernate.entity.Client;
import ua.nike.project.hibernate.type.Sex;
import ua.nike.project.spring.dao.DAO;
import ua.nike.project.spring.exceptions.ApplicationException;
import ua.nike.project.spring.vo.ClientVO;

import java.util.ArrayList;
import java.util.List;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ServiceClient {

    private DAO<Client> dao;

    @Autowired
    public void setDao(DAO<Client> dao) {
        this.dao = dao;
        this.dao.setClassEO(Client.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ClientVO findByID(int clientID) throws ApplicationException {
        return convertToClientVO(dao.findByID(clientID));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Client findEntityByID(int entityID) throws ApplicationException {
        return dao.findByID(entityID);
    }


    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ClientVO> findAll() {
        List<Client> entities = dao.findAll("Client.findAll", null);
        if (entities == null) return null;
        List<ClientVO> result = new ArrayList<>();
        for (Client entity : entities) {
            result.add(convertToClientVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ClientVO create(ClientVO clientVO) {
        Client entity = copyToClient(clientVO, null);
        return convertToClientVO(dao.save(entity));
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public ClientVO update(int clientID, ClientVO clientVO) throws ApplicationException {
        Client originalEntity = dao.findByID(clientID);
        Client updatedEntity = copyToClient(clientVO, originalEntity);
        return convertToClientVO(dao.update(updatedEntity));
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public List<ClientVO> putClients(List<ClientVO> clientsVO) throws ApplicationException {
        List<ClientVO> result = new ArrayList<>();
        for (ClientVO clientVO : clientsVO) {
            if (clientVO != null) {
                if (clientVO.getClientId() > 0) {
                    result.add(update(clientVO.getClientId(), clientVO));
                } else {
                    result.add(create(clientVO));
                }
            }
        }
        return result;
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int clientID) {
        return dao.remove(clientID);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean deleteByIDs(List<Integer> clientIDs) {
        return dao.remove("DELETE Client cl WHERE cl.clientId IN (:IDs)", clientIDs);
    }

    private ClientVO convertToClientVO(Client client) {
        if (client == null) return null;
        ClientVO result = new ClientVO();
        result.setClientId(client.getClientId());
        result.setSurname(client.getSurname());
        result.setFirstName(client.getFirstName());
        result.setSecondName(client.getSecondName());
        result.setSex(client.getSex().toCharacter());
        result.setBirthday(client.getBirthday());
        result.setTelephone(client.getTelephone());
        return result;
    }

    private Client copyToClient(ClientVO original, Client result) {
        if (original != null) {
            result.setSurname(original.getSurname());
            result.setFirstName(original.getFirstName());
            result.setSecondName(original.getSecondName());
            result.setSex(Sex.getInstance(original.getSex()));
            result.setBirthday(original.getBirthday());
            result.setTelephone(original.getTelephone());
        }
        return result;
    }

}














    /*
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ClientVO> getListByQuery(String hqlQuery, Map<String, Object> parameters) throws
            ApplicationException {
        List<Client> entities = dao.getEntitiesByQuery(hqlQuery, parameters);
        List<ClientVO> result = new ArrayList<>();
        for (Client entity : entities) {
            result.add(convertToClientVO(entity));
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
//    public Client getEntityByID(int clientID) throws ApplicationException {
//        return dao.findByID(clientID);
//    }

        private ClientVO convertToClientVO(Client entity) throws ApplicationException {
            if (entity == null) return null;
            switch (entity.getClass().getSimpleName()) {
                case "Client":
                    return (ClientVO) transformToClientVO((Client) entity);
                case "Visit":
                    return (ClientVO) transformToVisitVO((Visit) entity);
                case "Accomodation":
                    return (ClientVO) transformToAccomodationVO((Accomodation) entity);
                case "Manager":
                    return (ClientVO) transformToManagerVO((Manager) entity);
                case "OperationType":
                    return (ClientVO) transformToOperationTypeVO((OperationType) entity);
                case "Surgeon":
                    return (ClientVO) transformToSurgeonVO((Surgeon) entity);
                case "VisitDate":
                    return (ClientVO) transformToVisitDateVO((VisitDate) entity);

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
            result.setClient(transformToClientVO(visit.getClient()));
            result.setPatient(transformToClientVO(visit.getPatient()));
            result.setStatus(convertClientStatus(visit.getStatus()));
            result.setOperationType(transformToOperationTypeVO(visit.getOperationType()));
            result.setEye(visit.getEye());
            result.setSurgeon(transformToSurgeonVO(visit.getSurgeon()));
            result.setManager(transformToManagerVO(visit.getManager()));
            result.setAccomodation(transformToAccomodationVO(visit.getAccomodation()));
            result.setNote(visit.getNote());
            result.setInactive(visit.getInactive());
            return result;
        }

        private ClientVO transformToClientVO(Client client) {
            if (client == null) return null;
            ClientVO result = new ClientVO();
            result.setClientId(client.getClientId());
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

        private Client copyToEntityObject(ClientVO clientVO, Client entity) throws ApplicationException {
            if (clientVO == null) return null;
            switch (clientVO.getClass().getSimpleName()) {
                case "ClientVO": {
                    if (entity == null) entity = (Client) new Client();
                    return (Client) copyToClient((ClientVO) clientVO, (Client) entity);
                }
                case "AccomodationVO": {
                    if (entity == null) entity = (Client) new Accomodation();
                    return (Client) copyToAccomodation((AccomodationVO) clientVO, (Accomodation) entity);
                }
                case "ManagerVO": {
                    if (entity == null) entity = (Client) new Manager();
                    return (Client) copyToManager((ManagerVO) clientVO, (Manager) entity);
                }
                case "OperationTypeVO": {
                    if (entity == null) entity = (Client) new OperationType();
                    return (Client) copyToOperationType((OperationTypeVO) clientVO, (OperationType) entity);
                }
                case "SurgeonVO": {
                    if (entity == null) entity = (Client) new Surgeon();
                    return (Client) copyToSurgeon((SurgeonVO) clientVO, (Surgeon) entity);
                }
                case "VisitVO": {
                    if (entity == null) entity = (Client) new Visit();
                    return (Client) copyToVisit((VisitVO) clientVO, (Visit) entity);
                }
                case "VisitDateVO": {
                    if (entity == null) entity = (Client) new VisitDate();
                    return (Client) copyToVisitDate((VisitDateVO) clientVO, (VisitDate) entity);
                }

                default:
                    throw new ApplicationException("Class not find.");
            }
        }

        private Visit copyToVisit(VisitVO original, Visit result) {

            if (original != null) {

                result.setTimeForCome(original.getTimeForCome());
                result.setOrderForCome(original.getOrderForCome());
                result.setStatus(convertClientStatus(original.getStatus()));
                result.setEye(original.getEye());
                result.setNote(original.getNote());
                result.setInactive(original.getInactive());

                if (original.getVisitDate() != null && original.getVisitDate().getVisitDateId() > 0) {
                    VisitDate visitDate = new VisitDate();
                    visitDate.setVisitDateId(original.getVisitDate().getVisitDateId());
                    result.setVisitDate(copyToVisitDate(original.getVisitDate(), visitDate));
                } else result.setVisitDate(null);

                if (original.getPatient() != null && original.getPatient().getClientId() > 0) {
                    Client patient = new Client();
                    patient.setClientId(original.getPatient().getClientId());
                    result.setPatient(copyToClient(original.getPatient(), patient));
                } else result.setPatient(null);

                if (original.getClient() != null && original.getClient().getClientId() > 0) {
                    Client client = new Client();
                    client.setClientId(original.getClient().getClientId());
                    result.setClient(copyToClient(original.getClient(), client));
                } else result.setClient(null);


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

        private Client copyToClient(ClientVO original, Client result) {
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

        private ClientStatus convertClientStatus(String status) {
            switch (status) {
                case "пацієнт":
                    return ClientStatus.PATIENT;
                case "супров.":
                case "супроводжуючий":
                default:
                    return ClientStatus.RELATIVE;
            }
        }

        private String convertClientStatus(ClientStatus status) {
            if (status == null) return null;

            switch (status.toString().toUpperCase()) {
                case "RELATIVE":
                    return "супров.";
                case "PATIENT":
                default:
                    return "пацієнт";
            }
        }

    private boolean isRelated(Client entity) throws ApplicationException {
//        if (entity == null) throw new ApplicationException("Entity is not correct.");
//        switch (entity.getClass().getSimpleName()) {
//            case "Client": {
//                Map<String, Object> parametersClient = new HashMap<>();
//                parametersClient.put("client", (Client) entity);
//                Map<String, Object> parametersPatient = new HashMap<>();
//                parametersPatient.put("patient", (Client) entity);
//                return ((Long) dao.getObjectByQuery("SELECT count(*) FROM Visit v WHERE v.client = :client", parametersClient) != 0
//                        || dao.getEntitiesByNamedQuery("Visit.findByPatient", parametersPatient, (Class<Client>) Visit.class).size() != 0);
//            }
//            case "Manager": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("manager", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByManager", parameters, (Class<Client>) Visit.class).size() != 0;
//            }
//            case "OperationType": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("operationType", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByOperationType", parameters, (Class<Client>) Visit.class).size() != 0;
//            }
//            case "Surgeon": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("surgeon", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findBySurgeon", parameters, (Class<Client>) Visit.class).size() != 0;
//            }
//            case "VisitDate": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("visitDate", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByVisitDate", parameters, (Class<Client>) Visit.class).size() != 0;
//            }
//            case "Accomodation": {
//                Map<String, Object> parameters = new HashMap<>();
//                parameters.put("accomodation", entity);
//                return dao.getEntitiesByNamedQuery("Visit.findByAccomodation", parameters, (Class<Client>) Visit.class).size() != 0;
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