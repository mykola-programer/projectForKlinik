package ua.nike.project.klinika.jsp.controller;

import ua.nike.project.klinika.jsp.model.OperationDateModel;
import ua.nike.project.klinika.jsp.model.OperationBean;
import ua.nike.project.klinika.jsp.model.OperationModel;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/test_mvc")
public class ControllerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

        try {
            String reqDate = req.getParameter("date");
            if (reqDate != null && !reqDate.equals("")) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(reqDate, format);
                req.getSession().setAttribute("selected_date", java.sql.Date.valueOf(date));
//              req.setAttribute("date", date.getDate());
                List<OperationBean> operations = OperationModel.getResultOperation(date);
                req.setAttribute("operations", operations);

            } else {
                req.setAttribute("Massage", "Введіть обовязково дату!");
            }

            List<LocalDate> operation_dates = OperationDateModel.getOperationDates();
            req.setAttribute("operation_dates", operation_dates);
        } catch (Exception e) {
            req.setAttribute("ErrorMassage", e.getMessage());
            e.printStackTrace();
        } finally {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/klinika/jsp/operations_report.jsp");
            // Will need to write "try-catch" construction for ServletException.
            requestDispatcher.forward(req, resp);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {

    }


}
