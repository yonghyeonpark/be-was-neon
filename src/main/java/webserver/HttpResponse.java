package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class HttpResponse {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    public void processResponse(OutputStream out, byte[] bytes) {
        DataOutputStream dos = new DataOutputStream(out);
        response200Header(dos, bytes.length);
        responseBody(dos, bytes);
        closeOutputStream(dos);
    }

    private void response200Header(DataOutputStream dos, int lengthOfBodyContent) {
        try {
            dos.writeBytes("HTTP/1.1 200 OK \r\n");
            dos.writeBytes("Content-Type: text/html;charset=utf-8\r\n");
            dos.writeBytes("Content-Length: " + lengthOfBodyContent + "\r\n");
            dos.writeBytes("\r\n");
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void responseBody(DataOutputStream dos, byte[] body) {
        try {
            dos.write(body, 0, body.length);
            dos.flush();
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }

    private void closeOutputStream(DataOutputStream dos) {
        if (dos != null) {
            try {
                dos.close();
            } catch (IOException e) {
                logger.error(e.getMessage());
            }
        }
    }
}
