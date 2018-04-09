package ua.nike.project.hibernate.control;

import ua.nike.project.hibernate.model.EntityManagerFactorySingleton;
import ua.nike.project.hibernate.model.OperationBean;
import ua.nike.project.hibernate.model.OperationDateModel;
import ua.nike.project.hibernate.model.OperationModel;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
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
public class ControllerHibernateServlet extends HttpServlet {
    private static final EntityManagerFactory MANAGER_FACTORY = EntityManagerFactorySingleton.getEntityManagerFactory();
    private static final EntityManager ENTITY_MANAGER = EntityManagerFactorySingleton.getEntityManager();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        try {
            List<Date> operation_dates = OperationDateModel.getOperationDates();
            req.setAttribute("operation_dates", operation_dates);

            String reqDate = req.getParameter("date");
            if (reqDate != null && !reqDate.equals("")) {
                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate date = LocalDate.parse(reqDate, format);
                req.getSession().setAttribute("selected_date", Date.valueOf(date));
                List<OperationBean> operations = OperationModel.getResultOperation(date);
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
        }
    }

    @Override
    public void destroy() {
        ENTITY_MANAGER.close();
        MANAGER_FACTORY.close();
    }
}
