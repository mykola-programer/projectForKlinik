package ua.nike.project.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.web.context.support.XmlWebApplicationContext;
import ua.nike.project.hibernate.entity.Operation;
import ua.nike.project.hibernate.entity.OperationDay;
import ua.nike.project.hibernate.entity.Patient;
import ua.nike.project.spring.dao.OperationDAO;
import ua.nike.project.spring.dao.OperationDayDAO;
import ua.nike.project.spring.dao.PatientDAO;

import java.sql.Date;
import java.util.List;

public class MainSpring {

    public static void main(String[] args) {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("/spring/applicationContext.xml");

        PatientDAO patientDAO = applicationContext.getBean(PatientDAO.class);

        System.out.println("__________Patient___________");
        for (Patient p : patientDAO.listPatients()) {
            System.out.println(p);
        }


        OperationDAO operationDAO = applicationContext.getBean(OperationDAO.class);

        System.out.println("__________Operation___________");
        List<Operation> operationList = operationDAO.listOperations();
        for (Operation operation : operationList) {
            System.out.println(operation);
        }

        OperationDayDAO operationDayDAO = applicationContext.getBean(OperationDayDAO.class);

        System.out.println("__________OperationDay___________");
        for (OperationDay p : operationDayDAO.listOperationDays()) {
            System.out.println(p);
        }

        System.out.println("__________Days___________");
        for (Date p : operationDayDAO.getOperationDates()) {
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
