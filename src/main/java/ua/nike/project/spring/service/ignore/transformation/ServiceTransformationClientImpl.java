package ua.nike.project.spring.service.transformation;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import ua.nike.project.hibernate.entity.Client;
import ua.nike.project.hibernate.type.Sex;
import ua.nike.project.spring.vo.ClientVO;

@Service
@Scope(BeanDefinition.SCOPE_PROTOTYPE)
public class ServiceTransformationClientImpl implements ServiceTransformation<ClientVO, Client> {

    @Override
    public ClientVO convertToVisualObject(Client client) {
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

    @Override
    public Client copyToEntityObject(ClientVO original, Client result) {
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
    @Override
    public boolean isRelated(Client entity){
        return !(entity != null && entity.getVisitsForClient().size() == 0 && entity.getVisitsForRelative().size() == 0);
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

}
