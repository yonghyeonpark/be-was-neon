package service;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.util.Map;

public class QueryProcessor {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static void userJoin(Map<String, String> parameters) {
        User joinUser = new User(parameters.get("userId"),
                parameters.get("password"),
                parameters.get("name"),
                parameters.get("email"));
        Database.addUser(joinUser);
        logger.debug("joinUser : {}", joinUser);
    }

    public static boolean checkLogin(Map<String, String> parameters) {
        String loginId = parameters.get("userId");
        String password = parameters.get("password");
        User findUser = Database.findUserById(loginId);
        if (findUser == null) {
            return false;
        }
        return findUser.getPassword().equals(password);
    }
}
