package ua.nike.project.servlets;

import ua.nike.project.service.JdbcStoragePatient;
import ua.nike.project.struct.Patient;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ServletStart extends HttpServlet {

    private String httpOutStart =
            "<!DOCTYPE HTML>\n" +
                    "<html>\n" +
                    "   <head>\n" +
                    "       <meta charset=\"UTF-8\">\n" +
                    "       <title>Заїзд на операції</title>\n" +
                    "</head>\n" +
                    "<body>\n";
    private String httpOutBody;
    private String httpOutEnd = "" +
            "</body>\n" +
            "</html>\n";

    private List<Patient> patientSet;


    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String responseContentType = "text/html;charset=UTF-8";
        httpServletResponse.setContentType(responseContentType);


        httpServletResponse.getWriter().write(httpOutStart+getHttpOutBody()+httpOutEnd);
    }

    private String getHttpOutBody() {
        JdbcStoragePatient jdbcStoragePatient = new JdbcStoragePatient();
        patientSet = jdbcStoragePatient.getPatients();
        httpOutBody = "<ol>\n" ;

        for (Patient patient: patientSet){
            httpOutBody += "<li>" + patient + "</li>\n";
        }
        httpOutBody += "</ol>\n";
        return httpOutBody;
    }
}
