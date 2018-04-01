package ua.nike.project.mvc;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/test_mvc")
public class ControllerServlet extends HttpServlet {

    private ModelDate modelDate = new ModelDate();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        List<Date> dates;
        BeanDate date = new BeanDate();
        List<BeanResultTable> beanResultTableList;
        try {
            String reqDate = req.getParameter("date");
            if (reqDate != null && !reqDate.equals("")) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                date.setDate(java.sql.Date.valueOf((LocalDate.parse(reqDate, format))));

            } else {
                req.setAttribute("Massage", "Введіть обовязково дату!");
            }
            req.setAttribute("date", date.getDate());

            dates = modelDate.getOperationDates();
            req.setAttribute("dates", dates);

            beanResultTableList = modelDate.getResultOperation(date.getDate());
            req.setAttribute("list_of_operation", beanResultTableList);

        } catch (Exception e) {
            req.setAttribute("ErrorMassage", e.getMessage());
            e.printStackTrace();
        } finally {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/mvc/report_operations.jsp");
            // Will need to write "try-catch" construction for ServletException.
            requestDispatcher.forward(req, resp);
        }

    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

    }


}
