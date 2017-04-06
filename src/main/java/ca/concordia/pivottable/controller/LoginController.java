package ca.concordia.pivottable.controller;

import ca.concordia.pivottable.entities.ApplicationUser;
import ca.concordia.pivottable.servicelayer.UserManagementService;
import ca.concordia.pivottable.utils.DependenciesContainer;
import ca.concordia.pivottable.utils.ErrorResponse;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Session;

import java.util.HashMap;
import java.util.Map;

public class LoginController extends Controller {

    UserManagementService userService;

    public LoginController(DependenciesContainer container) {
        super(container);
        // TODO
        userService = container.get("userManagementService");
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        final String username = request.queryParams("username");
        final String pwd = request.queryParams("password");
        final ApplicationUser possibleUser = new ApplicationUser(username, pwd);
        userService.validateLogin(possibleUser);
        response.header("Content-Type", "application/json");
        Gson gson = new Gson();
        // success logging in
        Session sess = request.session(true);
        log.info("User " + username + " logged in. Creating session");
        sess.attribute("username", possibleUser.getUsername());
        Map<String, Object> resp = new HashMap<>();
        resp.put("status", 200);
        resp.put("username", username);
        return gson.toJson(resp);
    }
}
