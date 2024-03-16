package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.QueryProcessor;

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
            // 사용자 요청 메시지의 request line을 읽어들임
            HttpRequest httpRequest = new HttpRequest();
            String line = br.readLine();
            String target = httpRequest.getTarget(line);

            String[] splitTarget = target.split("\\?");
            String path = splitTarget[0];
            if (splitTarget.length == 2) {
                String query = splitTarget[1];
                Map<String, String> parameters = httpRequest.parseQuery(query);
                QueryProcessor.userJoin(parameters);
            }

            // 요청 헤더 처리
            while (true) {
                line = br.readLine();
                if (line.isEmpty()) {
                    break;
                }
                httpRequest.addHeaderLine(line);
            }
            httpRequest.printHeaderLineLog();

            String contentType = httpRequest.getContentType(path);
            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse = new HttpResponse();
            byte[] file = httpRequest.readFile(DEFAULT_PATH + path);
            // 해당 경로에 파일이 존재하지 않을 때
            if (file.length == 0) {
                byte[] bytes = "<h1>File Not Found!</h1>".getBytes();
                httpResponse.response404Header(dos, bytes.length, contentType);
                httpResponse.responseBody(dos, bytes);
                return;
            }

            httpResponse.response200Header(dos, file.length, contentType);
            httpResponse.responseBody(dos, file);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
