package carbuyer.servlets;

import carbuyer.logic.Validate;
import carbuyer.logic.ValidateService;
import carbuyer.models.Advert;
import carbuyer.models.User;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ShowUserAdvertsServlet extends HttpServlet {

    private final Validate validate = ValidateService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        User user = (User) req.getSession().getAttribute("user");
        List<Advert> list = validate.getAdvertsUser(user);
        String json = new Gson().toJson(list);
        resp.setContentType("json");
        resp.getWriter().write(json);
    }
}
