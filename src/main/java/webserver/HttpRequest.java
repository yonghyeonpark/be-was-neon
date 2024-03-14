package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final List<String> headerLines;

    public HttpRequest() {
        this.headerLines = new ArrayList<>();
    }

    public String getTarget(String line) {
        String[] requestLine = line.split(" ");
        return requestLine[1];
    }

    public Map<String, String> parseQuery(String query) {
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

    public byte[] readFile(String path) {
        File file = new File(path);
        if (!file.isFile()) {
            logger.error("path가 올바르지 않습니다.");
            return new byte[0];
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

    public void addHeaderLine(String headerLine) {
        headerLines.add(headerLine);
    }

    public void printHeaderLineLog() {
        for (String headerLine : headerLines) {
            logger.debug("[header-line] " + headerLine);
        }
    }
}
