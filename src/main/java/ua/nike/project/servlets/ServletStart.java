package ua.nike.project.servlets;

import ua.nike.project.struct.Human;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Clock;
import java.time.LocalDate;
import java.util.Date;
import java.util.TreeSet;

public class ServletStart extends HttpServlet{

    Date date = new Date();

    static String http_out_inf =
            "<!DOCTYPE HTML>\n" +
                    "<html>\n" +
                    "   <head>\n" +
                    "       <meta charset=\"UTF-8\">\n" +
                    "       <title>Заїзд на операції</title>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "   <h3>Операційний день : 15.02.2018 р.<operationDate></h3>\n" +
                    "</body>\n" +
                    "</html>\n";

    private TreeSet<Human> humanTreeSet;

    public TreeSet<Human> getHumanTreeSet() {
        return humanTreeSet;
    }

    public void setHumanTreeSet(TreeSet<Human> humanTreeSet) {
        this.humanTreeSet = humanTreeSet;
    }

    @Override
    protected void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws ServletException, IOException {
        String responseContentType = "text/html;charset=UTF-8";
        httpServletResponse.setContentType(responseContentType);

        httpServletResponse.getWriter().write(date.getDate()+"."+(date.getMonth()+1)+"."+(1900+date.getYear())+" р."+"/n");
        httpServletResponse.getWriter().write(http_out_inf);
    }

}
