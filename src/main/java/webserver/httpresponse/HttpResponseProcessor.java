package webserver.httpresponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.MainHandler;

import java.io.DataOutputStream;
import java.io.IOException;

public class HttpResponseProcessor {

    private static final Logger logger = LoggerFactory.getLogger(MainHandler.class);
    private static final String NOT_FOUND_ERROR_MESSAGE = "<h1>File Not Found!</h1>";

    private final DataOutputStream dos;
    private final HttpResponse httpResponse;

    public HttpResponseProcessor(DataOutputStream dos, HttpResponse httpResponse) {
        this.dos = dos;
        this.httpResponse = httpResponse;
    }

    public void sendResponse() {
        byte[] file = httpResponse.getFile();
        String contentType = httpResponse.getContentType();
        String sessionId = httpResponse.getSessionId();
        String location = httpResponse.getLocation();

        if (location != null) {
            if (sessionId != null) {
                send302Response(location, sessionId);
                return;
            }
            send302Response(location);
            return;
        }
        if (file == null) {
            send404Response(contentType);
            return;
        }
        send200Response(file, contentType);
    }

    private void send200Response(byte[] file, String contentType) {
        response200Header(file.length, contentType);
        responseBody(file);
    }

    private void send404Response(String contentType) {
        response404Header(contentType);
        responseBody();
    }

    private void send302Response(String location) {
        response302Header(location);
    }

    private void send302Response(String location, String sessionId) {
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
