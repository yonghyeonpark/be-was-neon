package webserver.httprequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MainHandler;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpRequestProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);

    private final BufferedReader bufferedReader;

    public HttpRequestProcessor(BufferedReader bufferedReader) {
        this.bufferedReader = bufferedReader;
    }

    public StartLine getStartLine() throws IOException {
        return new StartLine(bufferedReader.readLine());
    }

    public Map<String, String> getHeaderLines() throws IOException {
        Map<String, String> headers = new HashMap<>();
        String headerLine;
        while (true) {
            headerLine = bufferedReader.readLine();
            if (headerLine.isEmpty()) {
                break;
            }
            String[] split = headerLine.split(":", 2);
            if (split.length == 2) {
                headers.put(split[0].trim(), split[1].trim());
            }
        }
        return headers;
    }

    public String getBody(String contentLength) throws IOException {
        if (contentLength == null) {
            return null;
        }
        char[] body = new char[Integer.parseInt(contentLength)];
        bufferedReader.read(body);
        logger.debug("[body-Line] {}", new String(body));
        return new String(body);
    }
}
