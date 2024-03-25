package webserver.httprequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.QueryManager;
import session.SessionManager;
import webserver.ContentType;
import webserver.MainHandler;
import webserver.httpresponse.HttpResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public class HttpRequestHandler {

    private static final String DEFAULT_PATH = "./src/main/resources/static";
    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);

    private final HttpRequest httpRequest;

    public HttpRequestHandler(HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
    }

    public HttpResponse getResponseProcessResult() {
        if (httpRequest.isPost()) {
            return getPostResult(httpRequest);
        }
        if (httpRequest.isGet()) {
            return getGetResult(httpRequest);
        }
        return null;
    }

    private HttpResponse getPostResult(HttpRequest received) {
        Body body = received.getBody();
        if (received.isMatchUri("/user/create")) {
            return getJoinResult(body);
        }

        if (received.isMatchUri("/user/login")) {
            return getLoginResult(body);
        }
        return null;
    }

    private HttpResponse getGetResult(HttpRequest received) {
        String target = received.getTarget();
        byte[] file = readFile(target);
        String contentType = getContentType(target);
        if (file == null) {
            return new HttpResponse("HTTP/1.1 404 Not Found");
        }
        return new HttpResponse("HTTP/1.1 200 OK", file, contentType);
    }

    private HttpResponse getJoinResult(Body body) {
        Map<String, String> parameters = QueryManager.parseQuery(body.getContent());
        QueryManager.userJoin(parameters);
        return new HttpResponse("HTTP/1.1 302 Found", "/index.html");
    }

    private HttpResponse getLoginResult(Body body) {
        Map<String, String> parameters = QueryManager.parseQuery(body.getContent());
        // 로그인 성공
        if (QueryManager.checkLogin(parameters)) {
            return new HttpResponse("HTTP/1.1 302 Found", "/index.html", SessionManager.generateSessionId());
        }
        // 로그인 실패
        return new HttpResponse("HTTP/1.1 302 Found", "/login/failed.html");
    }

    private byte[] readFile(String path) {
        File file = new File(DEFAULT_PATH + path);
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

    private String getContentType(String path) {
        logger.debug("path:{}", path);
        for (ContentType contentType : ContentType.values()) {
            if (path.contains(contentType.getName())) {
                return contentType.getProcess();
            }
        }
        return "text/html";
    }
}
