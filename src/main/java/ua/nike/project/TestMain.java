package ua.nike.project;

import ua.nike.project.service.JdbcStorage;
import ua.nike.project.struct.Patient;

import java.io.FileNotFoundException;

public class TestMain {
    public static void main(String[] args) throws FileNotFoundException, ClassNotFoundException {

        JdbcStorage jdbcStorage = new JdbcStorage();
        for (Patient patient :jdbcStorage.getHumans()){
            System.out.println(patient);
        }
        System.out.println("---------------------");
        jdbcStorage.editHuman(1, new Patient(null, "Мельник", "", "", 'Ж', "супроводжуючий", "+380638326767"));
        for (Patient patient :jdbcStorage.getHumans()){
            System.out.println(patient);
        }



//        final String URL = Settings.getSettings().value("jdbc.url");
//        final String username = Settings.getSettings().value("jdbc.username");
//        final String password = Settings.getSettings().value("jdbc.password");
//        Class.forName((String) Settings.getSettings().value("jdbc.driver_class"));
        /*try (Connection connection = DriverManager.getConnection(URL, username, password);
             Statement statement = connection.createStatement()) {
            System.out.println("All right !");
            ResultSet resultSet = statement.executeQuery("select * from ?");

            // Як дізнатись скільки колонок в моєму resultSet.

            System.out.println(resultSet);
            while (resultSet.next()) {
                System.out.println(resultSet.getObject(1));
                System.out.println(resultSet.getObject(2));
                System.out.println(resultSet.getObject(3));
                System.out.println(resultSet.getObject(4));
                System.out.println(resultSet.getObject(5));
                System.out.println(resultSet.getObject(6));
                System.out.println(resultSet.getObject(7));
                System.out.println("__________");
            }


        } catch (SQLException e) {
            System.out.println("Wrong parameters to connection. Please, check your file 'my.properties' !");
            e.printStackTrace();
        }*/
//

//        Patient human6 = new Patient(null, "Мітрік", "", "", 'Ж', "супроводжуючий", "+380991235767");
  //      human6.setUid(jdbcStorage.addHuman(human6));
    //    System.out.println(jdbcStorage.getPatient("uid", human6.getUid().toString()));
    }
}
