package todolist.servlets;

import com.google.gson.Gson;
import todolist.logic.Validate;
import todolist.logic.ValidateService;
import todolist.models.Item;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class FilterItemsServlet extends HttpServlet {

    private final Validate validate = ValidateService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        List<Item> list = validate.showFilterItems();
        String json = new Gson().toJson(list);
        resp.setContentType("json");
        resp.getWriter().write(json);
    }
}
