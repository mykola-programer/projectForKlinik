package ua.nike.project.spring.dao;

import org.springframework.stereotype.Component;
import ua.nike.project.hibernate.entity.Client;
import ua.nike.project.spring.exceptions.BusinessException;
import ua.nike.project.spring.vo.ClientVO;
import ua.nike.project.spring.vo.VisitVO;

import java.util.List;

@Component
public interface ClientDAO {

    List<ClientVO> getClients();

    ClientVO editClient(int clientId, ClientVO clientVO) throws BusinessException;

    List<ClientVO> addClients(List<ClientVO> clientVOList);

    ClientVO findClient(int clientID) throws BusinessException;

    boolean removeClient(int clientId);

    List<VisitVO> getListVisitsOfClient(int clientId) throws BusinessException;

    ClientVO transformToClientVO(Client client);

}
