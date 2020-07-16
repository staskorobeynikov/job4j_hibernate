package carbuyer.servlets;

import carbuyer.logic.Validate;
import carbuyer.logic.ValidateService;
import carbuyer.models.Account;
import carbuyer.models.User;
import com.google.gson.Gson;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginServlet extends HttpServlet {

    private final Validate validate = ValidateService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");
        Account account = new Account(login, password);
        User findUser = validate.isCredential(account);
        String json;
        if (findUser == null) {
            Map<String, String> answer = new HashMap<>();
            answer.put("answer", "User not found");
            json = new Gson().toJson(answer);
        } else {
            Map<String, User> users = new HashMap<>();
            req.getSession().setAttribute("user", findUser);
            users.put("user", findUser);
            json = new Gson().toJson(users);
        }
        resp.setContentType("json");
        resp.getWriter().write(json);
    }
}
