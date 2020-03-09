package todolist.servlets;

import todolist.logic.Validate;
import todolist.logic.ValidateService;
import todolist.models.Item;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UpdateItemServlet extends HttpServlet {

    private final Validate validate = ValidateService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String id = req.getParameter("id");
        String done = req.getParameter("done");
        Item item = new Item();
        item.setId(Integer.parseInt(id));
        item.setDone(Boolean.parseBoolean(done));
        validate.updateItem(item);
    }
}
