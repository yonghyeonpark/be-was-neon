package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static final String NOT_FOUND_ERROR_MESSAGE = "<h1>File Not Found!</h1>";

    private final DataOutputStream dos;

    public HttpResponse(DataOutputStream dos) {
        this.dos = dos;
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

    public void send200Response(byte[] file, String contentType) {
        response200Header(file.length, contentType);
        responseBody(file);
    }

    public void send404Response(String contentType) {
        response404Header(contentType);
        responseBody();
    }

    public void send302Response(String location) {
        response302Header(location);
    }

    public void send302Response(String location, String sessionId) {
        response302Header(location, sessionId);
    }

    private void response200Header(int lengthOfBodyContent, String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: "+ contentType + "\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response404Header(String contentType) {
        try {
            dos.writeBytes("HTTP/1.1 404 Not Found\r\n");
            dos.writeBytes("Content-Type: "+ contentType + "\r\n");
            dos.writeBytes("Content-Length: " + NOT_FOUND_ERROR_MESSAGE.length() + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(String location) {
        try {
            logger.debug("location:{}", location);
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Location: " + location);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void response302Header(String location, String sessionId) {
        try {
            logger.debug("location:{}", location);
            dos.writeBytes("HTTP/1.1 302 Found\r\n");
            dos.writeBytes("Set-Cookie: " + "sid=" + sessionId + "; Path=/\r\n");
            dos.writeBytes("Location: " + location);
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody() {
        try {
            dos.write(NOT_FOUND_ERROR_MESSAGE.getBytes(), 0, NOT_FOUND_ERROR_MESSAGE.length());
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
