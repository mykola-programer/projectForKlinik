package ua.nike.project;

import com.sun.net.httpserver.HttpServer;
import ua.nike.project.service.JdbcStoragePatient;
import ua.nike.project.struct.Patient;

import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TestMain {
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }

    public static void main(String[] args) {

        ArrayList list = new ArrayList();
list.clone();
//        JdbcStoragePatient jdbcStoragePatient = new JdbcStoragePatient();
//        for (Patient patient : jdbcStoragePatient.getPatients()) {
//            System.out.println(patient);
//        }
//        System.out.println("---------------------");
//        {
//            Patient patient = new Patient();
////            patient.setPatient_id(null);
//            patient.setSurname("Мітрік");
//            patient.setFirstName("Олена");
//            patient.setSecondName("Іванівна");
//            patient.setSex('Ж');
//            patient.setStatus("пацієнт");
//            patient.setRelative_id(0);
//            patient.setTelephone("+380667543737");
//
//            patient.setPatient_id(jdbcStoragePatient.addPatient(patient));
//
////            System.out.println(jdbcStoragePatient.editPatient(8, patient));
//        }
//        for (Patient patient : jdbcStoragePatient.getPatients()) {
//            System.out.println(patient);
//        }
    }
}
