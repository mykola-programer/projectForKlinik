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
import ua.nike.project.spring.vo.ClientVO;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
@Scope(BeanDefinition.SCOPE_SINGLETON)
public class ClientService {

    private DAO<Client> dao;

    @Autowired
    public void setDao(DAO<Client> dao) {
        this.dao = dao;
        this.dao.setClassEO(Client.class);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ClientVO findByID(int clientID) {
        return convertToClientVO(dao.findByID(clientID));
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public Client findEntityByID(int entityID) {
        return dao.findByID(entityID);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ClientVO> getAll() {
        List<Client> entities = dao.findAll("Client.getAll");

        if (entities == null) return null;
        List<ClientVO> result = new ArrayList<>();
        for (Client entity : entities) {
            result.add(convertToClientVO(entity));
        }
        return result;
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ClientVO> search(String searchValue, int limit, int offset, String sort) {
        String[] parameters = Arrays.copyOf(searchValue.split(" ", 4), 3);
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i] == null) parameters[i] = "";
        }
        List<Client> entities;
        if (sort.equals("ASC")) {
            entities = dao.findAll("Client.searchAllASC", parameters, limit, offset);

        } else {
            entities = dao.findAll("Client.searchAllDESC", parameters, limit, offset);
        }

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
    public ClientVO update(int clientID, ClientVO clientVO) {
        Client originalEntity = dao.findByID(clientID);
        if (originalEntity == null) {
            throw new EntityNotFoundException();
        }
        Client updatedEntity = copyToClient(clientVO, originalEntity);
        return convertToClientVO(dao.update(updatedEntity));
    }


    @Transactional(propagation = Propagation.REQUIRED)
    public boolean deleteById(int clientID) {
        return dao.remove(clientID);
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
            if (result == null) result = new Client();
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

    @Deprecated
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ClientVO> putClients(List<ClientVO> clientsVO) {
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
*/
