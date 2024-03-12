package webserver;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class RequestHandler implements Runnable {

    private static final String DEFAULT_PATH = "./src/main/resources/static";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());

        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            // 사용자 요청 메시지의 request line 읽어들임
            String line = br.readLine();
            logger.debug("request-line : " + line);
            String url = getUrl(line);
            logger.debug("url : " + url);

            String[] split = url.split("\\?");
            String path = split[0];
            logger.debug("path : " + path);
            // 요청 url에 쿼리문이 포함돼있을 때, 동작
            if (split.length == 2) {
                String query = split[1];

                Map<String, String> parameters = parseQuery(query);
                User joinUser = new User(parameters.get("userId"),
                        parameters.get("password"),
                        parameters.get("name"),
                        parameters.get("email"));
                Database.addUser(joinUser);
            }

            // TODO 사용자 요청에 대한 처리는 이 곳에 구현하면 된다.
            File file = new File(DEFAULT_PATH + path);
            byte[] bytes = new byte[(int) file.length()];
            try (FileInputStream inputStream = new FileInputStream(file)) {
                inputStream.read(bytes);
            }

            DataOutputStream dos = new DataOutputStream(out);
            response200Header(dos, bytes.length);
            responseBody(dos, bytes);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private String getUrl(String line) {
        String[] requestLine = line.split(" ");
        return requestLine[1];
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

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
