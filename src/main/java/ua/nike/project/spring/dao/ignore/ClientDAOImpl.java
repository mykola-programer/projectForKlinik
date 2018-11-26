package ua.nike.project.spring.dao;

import org.springframework.stereotype.Repository;

@Repository
public class ClientDAOImpl implements ClientDAO {

/*
    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private VisitDAO visitDAO;


    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<VisitVO> getVisitsOfClient(int clientId) throws BusinessException {
        Client client = this.entityManager.find(Client.class, clientId);
        if (client == null) throw new BusinessException("This client is not find in database !");

        List<VisitVO> result = new ArrayList<>();
        for (Visit visit : client.getVisitsForClient()) {
            result.add(this.visitDAO.transformToVisitVO(visit));
        }
        return result;
    }
*/


/*
    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ClientVO> getClients() {
        List<Client> clients = this.entityManager.createNamedQuery("Client.findAll", Client.class).getResultList();
        List<ClientVO> result = new ArrayList<>();
        for (Client client : clients) {
            result.add(transformToClientVO(client));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public List<ClientVO> getUnlockClients() {
        List<Client> clients = this.entityManager.createNamedQuery("Client.findAll", Client.class).getResultList();
        List<ClientVO> result = new ArrayList<>();
        for (Client client : clients) {
            result.add(transformToClientVO(client));
        }
        return result;
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientVO addClient(ClientVO clientVO) {
        Client client = new Client();
        this.copyToClient(clientVO, client);
        this.entityManager.persist(client);
        this.entityManager.flush();
        return transformToClientVO(client);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ClientVO editClient(int clientId, ClientVO clientVO) throws BusinessException {
        Client client = this.entityManager.find(Client.class, clientId);
        if (client == null) throw new BusinessException("This client is not find in database !");
        this.copyToClient(clientVO, client);
        this.entityManager.flush();
        return transformToClientVO(client);
    }

    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ClientVO findClient(int clientID) throws BusinessException {
        Client client = this.entityManager.find(Client.class, clientID);
        if (client == null) throw new BusinessException("This client is not find in database !");
        return transformToClientVO(client);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean removeClient(int clientId) {
        try {
            this.entityManager.remove(entityManager.getReference(Client.class, clientId));
            return true;

        } catch (EntityNotFoundException e) {
            return false;
        }
    }



    @Override
    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
    public ClientVO transformToClientVO(Client client) {
        if (client == null) return null;
        ClientVO result = new ClientVO();
        result.setClientId(client.getClientId());
        result.setSurname(client.getSurname());
        result.setFirstName(client.getFirstName());
        result.setSecondName(client.getSecondName());
        try {
            switch (client.getSex().toString().charAt(0)) {
                case 'W':
                case 'w':
                    result.setSex('Ж');
                    break;
                case 'M':
                case 'm':
                default:
                    result.setSex('Ч');
            }
        } catch (IndexOutOfBoundsException e) {
            result.setSex('Ч');
        }
        result.setBirthday(client.getBirthday());
        result.setTelephone(client.getTelephone());
        return result;
    }

    private void copyToClient(ClientVO original, Client result) {
        if (original != null) {
            result.setSurname(original.getSurname());
            result.setFirstName(original.getFirstName());
            result.setSecondName(original.getSecondName());
            switch (original.getSex()) {
                case 'Ж':
                case 'ж':
                    result.setSex(Sex.W);
                    break;
                case 'Ч':
                case 'ч':
                default:
                    result.setSex(Sex.M);
            }
            result.setBirthday(original.getBirthday());
            result.setTelephone(original.getTelephone());
        }
    }
*/

}
