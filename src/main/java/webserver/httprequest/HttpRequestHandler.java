package webserver.httprequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.QueryManager;
import session.SessionManager;
import webserver.MainHandler;
import webserver.httpresponse.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class HttpRequestHandler {

    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);

    public static HttpResponse processUri(HttpRequest httpRequest) {
        final HttpRequest received = httpRequest;
        if (received.isPost()) {
            return processPost(received);
        }
        if (received.isGet()) {
            // 파일 존재 확인
        }
        return null;
    }
    
    private static HttpResponse processPost(HttpRequest received) {
        String body = received.getBody();
        if (received.isMatchUri("/user/create")) {
            Map<String, String> parameters = QueryManager.parseQuery(body);
            QueryManager.userJoin(parameters);
            return new HttpResponse("HTTP/1.1 302 Found", "/index.html");
        }

        if (received.isMatchUri("/user/login")) {
            Map<String, String> parameters = QueryManager.parseQuery(body);
            // 로그인 성공
            if (QueryManager.checkLogin(parameters)) {
                received.setLocation("/index.html");
                return new HttpResponse("HTTP/1.1 302 Found", "/index.html", SessionManager.generateSessionId());
            }
            // 로그인 실패
            received.setLocation("/login/failed.html");
            return new HttpResponse("HTTP/1.1 302 Found", "/login/failed.html");
        }
        return null;
    }

    public static byte[] readFile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            logger.error("path가 올바르지 않습니다.");
            return null;
        }
        byte[] bytes = new byte[(int) file.length()];
        try (FileInputStream inputStream = new FileInputStream(file)) {
            inputStream.read(bytes);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }
        return bytes;
    }
}
