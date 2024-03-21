package webserver.httprequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.IOException;
import java.util.Map;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final StartLine startLine;
    private final Map<String, String> headers;
    private final String body;

    public HttpRequest(HttpRequestProcessor httpRequestProcessor) throws IOException {
        this.startLine = httpRequestProcessor.getStartLine();
        this.headers = httpRequestProcessor.getHeaderLines();
        this.body = httpRequestProcessor.getBody(Integer.parseInt(headers.get("Content-Length")));
    }

    public void printHeaderLinesLog() {
        for (Map.Entry<String, String> headerLine: headers.entrySet()) {
            logger.debug("[header-line] {} = {}", headerLine.getKey(), headerLine.getValue());
        }
    }

    public boolean isPost() {
        return startLine.getMethod().equalsIgnoreCase("POST");
    }

    public boolean isGet() {
        return startLine.getMethod().equalsIgnoreCase("GET");
    }

    public boolean isMatchUri(String uri) {
        return startLine.getTarget().equals(uri);
    }

    /*public boolean isExistHeaderType(String headerType) {
        return headers.containsKey(headerType);
    }

    public boolean isExistFile() {

    }*/

    public void setLocation(String location) {
        headers.put("Location", location);
    }

    public String getBody() {
        return body;
    }
}
