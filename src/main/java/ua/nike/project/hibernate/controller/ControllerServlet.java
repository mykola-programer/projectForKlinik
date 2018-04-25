package ua.nike.project.hibernate.controller;

import ua.nike.project.hibernate.model.EntityManagerFactorySingleton;
import ua.nike.project.hibernate.model.OperationBean;
import ua.nike.project.hibernate.model.OperationDateModel;
import ua.nike.project.hibernate.model.OperationModel;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@WebServlet("/test_hibernate")
public class ControllerServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        EntityManager entityManager = EntityManagerFactorySingleton.getEntityManagerFactory().createEntityManager();
        try {
            List<Date> operation_dates = OperationDateModel.getOperationDates(entityManager);
            req.setAttribute("operation_dates", operation_dates);

            String reqDate = req.getParameter("date");
            if (reqDate != null && !reqDate.equals("")) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(reqDate, format);
                req.getSession().setAttribute("selected_date", Date.valueOf(date));
                List<OperationBean> operations = OperationModel.getResultOperation(date, entityManager);
                req.setAttribute("operations", operations);

            } else {
                req.setAttribute("Massage", "Введіть обовязково дату!");
            }

        } catch (Exception e) {
            req.setAttribute("ErrorMassage", e.getMessage());
//            System.out.println(e.getMessage());
//            e.printStackTrace();
        } finally {
            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/klinika/hibernate/operations_report.jsp");
            // Will need to write "try-catch" construction for ServletException.
            requestDispatcher.forward(req, resp);
            entityManager.close();
        }
    }

    @Override
    public void destroy() {
        EntityManagerFactorySingleton.getEntityManagerFactory().close();
    }
}
