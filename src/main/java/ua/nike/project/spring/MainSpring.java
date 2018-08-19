package ua.nike.project.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ua.nike.project.hibernate.entity.Operation;
import ua.nike.project.hibernate.entity.OperationDate;
import ua.nike.project.spring.dao.OperationDAO;
import ua.nike.project.spring.dao.OperationDateDAO;
import ua.nike.project.spring.dao.PatientDAO;
import ua.nike.project.spring.vo.OperationDateVO;
import ua.nike.project.spring.vo.OperationVO;
import ua.nike.project.spring.vo.PatientVO;

import java.time.LocalDate;
import java.util.List;

public class MainSpring {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/spring/applicationContext.xml");

        PatientDAO patientDAO = applicationContext.getBean(PatientDAO.class);

        System.out.println("__________Patient___________");
        for (PatientVO p : patientDAO.listPatients()) {
            System.out.println(p);
        }


        OperationDAO operationDAO = applicationContext.getBean(OperationDAO.class);

        System.out.println("__________Operation___________");
        List<OperationVO> operationList = operationDAO.listOperations();
        for (OperationVO operationVO : operationList) {
            System.out.println(operationVO);
        }

        OperationDateDAO operationDateDAO = applicationContext.getBean(OperationDateDAO.class);

        System.out.println("__________OperationDay___________");
        for (OperationDateVO p : operationDateDAO.getListOperationDates()) {
            System.out.println(p);
        }

        System.out.println("__________Days___________");
        for (LocalDate p : operationDateDAO.getDates()) {
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
