package service;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class QueryManager {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public static Map<String, String> parseQuery(String query) {
        Map<String, String> parameters = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return parameters;
        }
        String decodingQuery = URLDecoder.decode(query);
        String[] queryArr = decodingQuery.split("&");
        for (String keyValue : queryArr) {
            String[] split = keyValue.split("=");
            parameters.put(split[0], split[1]);
        }
        return parameters;
    }

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
