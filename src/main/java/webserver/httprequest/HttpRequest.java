package webserver.httprequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ContentType;
import webserver.RequestHandler;

import java.io.*;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private StartLine startLine;
    private Map<String, String> headers;

    public HttpRequest() {
        this.headers = new HashMap<>();
    }

    public void readStartLine(BufferedReader br) throws IOException {
        startLine = new StartLine(br.readLine());
    }

    public void readHeaderLines(BufferedReader br) throws IOException {
        String headerLine;
        while (true) {
            headerLine = br.readLine();
            if (headerLine.isEmpty()) {
                break;
            }
            String[] split = headerLine.split(":", 2);
            if (split.length == 2) {
                headers.put(split[0].trim(), split[1].trim());
            }
        }
    }

    public String getBody(BufferedReader br, int contentLength) throws IOException {
        char[] body = new char[contentLength];
        br.read(body);
        logger.debug("[body-Line] {}", new String(body));
        return new String(body);
    }

    public Map<String, String> parseQuery(String query) {
        Map<String, String> parameters = new HashMap<>();
        if (query == null || query.isEmpty()) {
            return parameters;
        }
        String decodingQuery = URLDecoder.decode(query);
        String[] queryArr = decodingQuery.split("&");
        for (String keyValue : queryArr) {
            String[] split = keyValue.split("=");
            parameters.put(split[0], split[1]);
        }
        return parameters;
    }

    public byte[] readFile(String path) {
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

    public void printHeaderLinesLog() {
        for (Map.Entry<String, String> headerLine: headers.entrySet()) {
            logger.debug("[header-line] {} = {}", headerLine.getKey(), headerLine.getValue());
        }
    }

    public String getContentType(String path) {
        logger.debug("path:{}", path);
        for (ContentType contentType : ContentType.values()) {
            if (path.contains(contentType.getName())) {
                return contentType.getProcess();
            }
        }
        return "text/html";
    }

    public String getMethod() {
        return startLine.getMethod();
    }

    public String getTarget() {
        return startLine.getTarget();
    }

    public Map<String, String> getHeaders() {
        return headers;
    }
}
