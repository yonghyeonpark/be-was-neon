package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequest {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public String getStartLine(BufferedReader br) throws IOException {
        return br.readLine();
    }

    public List<String> getHeaderLines(BufferedReader br) throws IOException {
        List<String> headerLines = new ArrayList<>();
        String headerLine;
        while (true) {
            headerLine = br.readLine();
            if (headerLine.isEmpty()) {
                break;
            }
            headerLines.add(headerLine);
        }
        return headerLines;
    }

    public String[] getTarget(String line) {
        String[] requestLine = line.split(" ");
        return requestLine[1].split("\\?");
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

    public void printHeaderLinesLog(List<String> headerLines) {
        for (String headerLine : headerLines) {
            logger.debug("[header-line] {}", headerLine);
        }
    }

    public String getContentType(String path) {
        logger.debug("Path : {}", path);
        for (ContentType contentType : ContentType.values()) {
            if (path.contains(contentType.getName())) {
                return contentType.getProcess();
            }
        }
        return "text/html";
    }

    public boolean isExistQuery(String[] target) {
        return target.length == 2;
    }
}
