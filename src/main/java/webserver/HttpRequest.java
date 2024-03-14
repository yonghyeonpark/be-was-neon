package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private static final String DEFAULT_PATH = "./src/main/resources/static";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final List<String> headerLines;

    public HttpRequest() {
        this.headerLines = new ArrayList<>();
    }

    public String getTarget(String line) {
        String[] requestLine = line.split(" ");
        return requestLine[1];
    }

    public Map<String, String> parseQuery(String query) {
        Map<String, String> parameters = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return parameters;
        }
        String[] queryArr = query.split("&");
        for (String keyValue : queryArr) {
            String[] split = keyValue.split("=");
            parameters.put(split[0], split[1]);
        }
        return parameters;
    }

    public void processJoin(Map<String, String> parameters) {
        User joinUser = new User(parameters.get("userId"),
                parameters.get("password"),
                parameters.get("name"),
                parameters.get("email"));
        Database.addUser(joinUser);
        logger.debug("joinUser : " + joinUser);
    }

    public byte[] readFile(String path) {
        File file = new File(DEFAULT_PATH + path);
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(bytes);
        } catch (FileNotFoundException e) {
            logger.error("path가 올바르지 않습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public void addHeaderLine(String headerLine) {
        headerLines.add(headerLine);
    }

    public void printHeaderLineLog() {
        for (String headerLine : headerLines) {
            logger.debug("[header-line] " + headerLine);
        }
    }
}
