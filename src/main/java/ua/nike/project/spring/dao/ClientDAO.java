package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.Client;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.ClientVO;
import ua.nike.project.spring.vo.VisitVO;

import javax.validation.Validator;
import java.util.List;

@Component
public interface ClientDAO {

    List<ClientVO> getClients();

    List<ClientVO> getUnlockClients();

    ClientVO editClient(int clientId, ClientVO clientVO) throws BusinessException;

    ClientVO addClient(ClientVO clientVO);

    ClientVO findClient(int clientID) throws BusinessException;

    boolean removeClient(int clientId);

    List<VisitVO> getVisitsOfClient(int clientId) throws BusinessException;

    ClientVO transformToClientVO(Client client);

}
