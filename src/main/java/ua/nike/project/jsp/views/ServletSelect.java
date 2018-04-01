package ua.nike.project.jsp.views;

import ua.nike.project.jsp.models.DatesModel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.List;

@WebServlet("/select_date")
public class ServletSelect extends HttpServlet {

    DatesModel datesModel = new DatesModel();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       resp.getWriter().write(getOptions());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.getWriter().write(getOptions());
    }

    private String getOptions() {
        StringBuilder result = new StringBuilder();

        try {
            result.append("<option disabled selected value='#'>Виберіть дату...</option>\n");
            List<Date> dateList = datesModel.getOperationDates();
            for (Date date : dateList) {
                result.append(String.format("<option value='%s'>%s</option>\n", date, date));
            }
        }catch (SQLException e){
            result.append("<option disabled selected value=Помилка бази. Спробуйте пізніше...>Помилка бази. Спробуйте пізніше...</option>\n");
        }
        return result.toString();
    }


}
