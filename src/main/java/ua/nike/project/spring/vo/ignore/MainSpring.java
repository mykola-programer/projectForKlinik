package ua.nike.project.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.nike.project.spring.dao.VisitDAO;
import ua.nike.project.spring.dao.VisitDateDAO;
import ua.nike.project.spring.dao.ClientDAO;
import ua.nike.project.spring.vo.VisitDateVO;
import ua.nike.project.spring.vo.VisitVO;
import ua.nike.project.spring.vo.ClientVO;

import java.time.LocalDate;
import java.util.List;

public class MainSpring {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/spring/applicationContext.xml");

        ClientDAO clientDAO = applicationContext.getBean(ClientDAO.class);

        System.out.println("__________Patient___________");
        for (ClientVO p : clientDAO.getClients()) {
            System.out.println(p);
        }


        VisitDAO visitDAO = applicationContext.getBean(VisitDAO.class);

        System.out.println("__________Operation___________");
        List<VisitVO> operationList = visitDAO.getVisits();
        for (VisitVO visitVO : operationList) {
            System.out.println(visitVO);
        }

        VisitDateDAO visitDateDAO = applicationContext.getBean(VisitDateDAO.class);

        System.out.println("__________OperationDay___________");
        for (VisitDateVO p : visitDateDAO.getListUnlockedVisitDates()) {
            System.out.println(p);
        }

        System.out.println("__________Days___________");
        for (VisitDateVO p : visitDateDAO.getVisitDates()) {
            System.out.println(p);
        }





        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ((ClassPathXmlApplicationContext) applicationContext).close();
    }
}
