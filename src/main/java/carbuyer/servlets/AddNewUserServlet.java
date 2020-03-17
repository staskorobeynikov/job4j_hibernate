package carbuyer.servlets;

import carbuyer.logic.Validate;
import carbuyer.logic.ValidateService;
import carbuyer.models.Account;
import carbuyer.models.User;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AddNewUserServlet extends HttpServlet {

    private final Validate validate = ValidateService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String name = req.getParameter("name");
        String phone = req.getParameter("phone");
        String address = req.getParameter("address");
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        Account account = new Account();
        account.setLogin(login);
        account.setPassword(password);

        User user = new User();
        user.setName(name);
        user.setPhone(phone);
        user.setAddress(address);

        User addUser = validate.addUser(user, account);

        resp.sendRedirect(String.format("%s/adverts.html?user=%s", req.getContextPath(), addUser.getId()));
    }
}
