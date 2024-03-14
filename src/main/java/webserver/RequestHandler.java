package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class RequestHandler implements Runnable {

    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private Socket connection;

    public RequestHandler(Socket connectionSocket) {
        this.connection = connectionSocket;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream(); OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            // 사용자 요청 메시지의 request line을 읽어들임
            String line = br.readLine();
            logger.debug("request Line : " + line);
            HttpRequest httpRequest = new HttpRequest();
            String target = httpRequest.getTarget(line);
            String[] splitTarget = target.split("\\?");
            String path = splitTarget[0];
            if (splitTarget.length == 2) {
                String query = splitTarget[1];
                httpRequest.processJoin(httpRequest.parseQuery(query));
            }
            byte[] file = httpRequest.readFile(path);

            while (true) {
                line = br.readLine();
                if (line.isEmpty()) {
                    break;
                }
                httpRequest.addHeaderLine(line);
            }
            httpRequest.printHeaderLineLog();

            HttpResponse httpResponse = new HttpResponse();
            httpResponse.processResponse(out, file);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
