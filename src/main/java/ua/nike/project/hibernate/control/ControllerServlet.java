package ua.nike.project.hibernate.control;

import org.hibernate.SessionFactory;
import org.hibernate.*;
import org.hibernate.cfg.Configuration;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@WebServlet("/test_hibernate")
public class ControllerServlet extends HttpServlet {

    private static final SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory();
    private static final Session session = sessionFactory.openSession();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {



//        List<Date> dates;
//        BeanDate date = new BeanDate();
//        List<OperationBean> beanResultTableList;
//        try {
//            String reqDate = req.getParameter("date");
//            if (reqDate != null && !reqDate.equals("")) {
//                DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//                date.setDate(java.sql.Date.valueOf((LocalDate.parse(reqDate, format))));
//
//            } else {
//                req.setAttribute("Massage", "Введіть обовязково дату!");
//            }
//            req.getSession().setAttribute("date", date.getDate());
////            req.setAttribute("date", date.getDate());
//
//            dates = modelDate.getOperationDates();
//            req.setAttribute("dates", dates);
//
//            beanResultTableList = modelDate.getResultOperation(date.getDate());
//            req.setAttribute("list_of_operation", beanResultTableList);
//
//        } catch (Exception e) {
//            req.setAttribute("ErrorMassage", e.getMessage());
//            e.printStackTrace();
//        } finally {
//            RequestDispatcher requestDispatcher = req.getRequestDispatcher("/mvc/operations_report.jsp");
//            // Will need to write "try-catch" construction for ServletException.
//            requestDispatcher.forward(req, resp);
//        }
//
//        {
//            System.out.println(req.getAttribute("date"));
//            System.out.println(req.getSession().getAttribute("date"));
//        }
    }

    @Override
    public void destroy() {
        session.close();
        sessionFactory.close();    }
}
