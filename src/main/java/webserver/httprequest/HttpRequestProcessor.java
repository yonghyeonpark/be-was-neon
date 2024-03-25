package webserver.httprequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MainHandler;

import java.io.BufferedReader;
import java.io.IOException;

public class HttpRequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);

    private final BufferedReader bufferedReader;

    public HttpRequestProcessor(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public StartLine getStartLine() throws IOException {
        return new StartLine(bufferedReader.readLine());
    }

    public Headers getHeaders() throws IOException {
        Headers headers = new Headers();
        String headerLine;
        while (true) {
            headerLine = bufferedReader.readLine();
            if (headerLine.isEmpty()) {
                break;
            }
            String[] split = headerLine.split(":", 2);
            if (split.length == 2) {
                headers.addHeader(split[0].trim(), split[1].trim());
            }
        }
        return headers;
    }

    public Body getBody(String contentLength) throws IOException {
        if (contentLength == null) {
            return null;
        }
        char[] body = new char[Integer.parseInt(contentLength)];
        bufferedReader.read(body);
        logger.debug("[body-Line] {}", new String(body));
        return new Body(new String(body));
    }
}
