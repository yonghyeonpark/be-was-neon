package webserver.httprequest;

import service.QueryManager;

import java.util.Map;

public class HttpRequestHandler {

    public static HttpRequest processUri(HttpRequest httpRequest) {
        final HttpRequest received = httpRequest;
        String body = received.getBody();
        if (received.isMatchUri("/user/create")) {
            Map<String, String> parameters = QueryManager.parseQuery(body);
            QueryManager.userJoin(parameters);
            received.setLocation("/index.html");
            return received;
        }

        if (httpRequest.isMatchUri("/user/login")) {
            Map<String, String> parameters = QueryManager.parseQuery(body);
            // 로그인 성공
            if (QueryManager.checkLogin(parameters)) {
                received.setLocation("/index.html");
                return received;
            }
            // 로그인 실패
            received.setLocation("/login/failed.html");
            return received;
        }
        return null;
    }
}
