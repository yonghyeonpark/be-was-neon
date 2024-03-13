package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final String DEFAULT_PATH = "./src/main/resources/static";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private byte[] file;

    public void processRequest(InputStream in) {
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            // 사용자 요청 메시지의 request line 읽어들임
            String line = br.readLine();
            logger.debug("request-line : " + line);
            String url = getUrl(line);
            logger.debug("url : " + url);

            String[] split = url.split("\\?");
            String target = split[0];
            logger.debug("target : " + target);
            // 요청 url에 쿼리문이 포함돼있을 때, 동작
            if (split.length == 2) {
                String query = split[1];
                processQuery(query);
            }
            file = readFile(target);

            while (!line.isEmpty()) {
                line = br.readLine();
                logger.debug("header-line : " + line);
            }
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getUrl(String line) {
        String[] requestLine = line.split(" ");
        return requestLine[1];
    }

    private void processQuery(String query) {
        Map<String, String> parameters = parseQuery(query);
        User joinUser = new User(parameters.get("userId"),
                parameters.get("password"),
                parameters.get("name"),
                parameters.get("email"));
        Database.addUser(joinUser);
        logger.debug("joinUser : " + joinUser);
    }

    private Map<String, String> parseQuery(String query) {
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

    private byte[] readFile(String target) {
        File file = new File(DEFAULT_PATH + target);
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(bytes);
        } catch (FileNotFoundException e) {
            logger.error("target이 올바르지 않습니다.");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }

    public byte[] getFile() {
        return file;
    }
}
