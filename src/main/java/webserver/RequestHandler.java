package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.QueryProcessor;
import webserver.httprequest.HttpRequest;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RequestHandler implements Runnable {

    private static final String DEFAULT_PATH = "./src/main/resources/static";
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);

    private final Socket connection;

    public RequestHandler(Socket connection) {
        this.connection = connection;
    }

    public void run() {
        logger.debug("New Client Connect! Connected IP : {}, Port : {}", connection.getInetAddress(),
                connection.getPort());
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            BufferedReader br = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            HttpRequest httpRequest = new HttpRequest();
            httpRequest.readStartLine(br);

            // 요청 헤더 처리
            httpRequest.readHeaderLines(br);
            Map<String, String> headers = httpRequest.getHeaders();
            httpRequest.printHeaderLinesLog();

            HttpResponse httpResponse = new HttpResponse();
            DataOutputStream dos = new DataOutputStream(out);
            String target = httpRequest.getTarget();
            // 회원가입 요청에 대한 처리
            if (httpRequest.getMethod().equalsIgnoreCase("POST") && target.equals("/user")) {
                String body = httpRequest.getBody(br, Integer.parseInt(headers.get("Content-Length")));
                Map<String, String> parameters = httpRequest.parseQuery(body);
                QueryProcessor.userJoin(parameters);
                httpResponse.response302Header(dos, "/index.html");
            }

            String contentType = httpRequest.getContentType(target);
            byte[] file = httpRequest.readFile(DEFAULT_PATH + target);
            // 해당 경로에 파일이 존재하지 않을 때
            if (file == null) {
                httpResponse.response404Header(dos, contentType);
                httpResponse.responseBody(dos);
                return;
            }
            httpResponse.response200Header(dos, file.length, contentType);
            httpResponse.responseBody(dos, file);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
