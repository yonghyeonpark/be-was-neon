package webserver.httprequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MainHandler;

import java.io.IOException;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);

    private final StartLine startLine;
    private final Headers headers;
    private final String body;

    public HttpRequest(HttpRequestProcessor httpRequestProcessor) throws IOException {
        this.startLine = httpRequestProcessor.getStartLine();
        this.headers = httpRequestProcessor.getHeaders();
        this.body = httpRequestProcessor.getBody(headers.getHeaderValue("Content-Length"));
    }

    public void printHeaderLinesLog() {
        for (String headerName: headers.getHeaderNames()) {
            logger.debug("[header-line] {} = {}", headerName, headers.getHeaderValue(headerName));
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

    public String getBody() {
        return body;
    }

    public String getTarget() {
        return startLine.getTarget();
    }
}
