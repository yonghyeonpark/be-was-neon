package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.httprequest.HttpRequest;
import webserver.httprequest.HttpRequestHandler;
import webserver.httprequest.HttpRequestProcessor;
import webserver.httpresponse.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

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
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));

            // 새로운 코드ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ
            HttpRequest httpRequest = new HttpRequest(new HttpRequestProcessor(bufferedReader));
            httpRequest.printHeaderLinesLog();

            HttpRequest processedRequest = HttpRequestHandler.uriProcess(httpRequest);
            //ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ


            /*// 요청 헤더 처리
            httpRequest.readHeaderLines(bufferedReader);
            Map<String, String> headers = httpRequest.getHeaders();
            httpRequest.printHeaderLinesLog();
            String target = httpRequest.getTarget();*/

            DataOutputStream dos = new DataOutputStream(out);
            HttpResponse httpResponse = new HttpResponse(dos);

             // post 요청에 대한 처리
            /*if (httpRequest.getMethod().equalsIgnoreCase("POST")) {
                // 회원 가입 처리
                if (target.equals("/user/create")) {
                    String body = httpRequest.getBody(bufferedReader, Integer.parseInt(headers.get("Content-Length")));
                    Map<String, String> parameters = httpRequest.parseQuery(body);
                    QueryManager.userJoin(parameters);
                    httpResponse.send302Response("/index.html");
                    return;
                }
                // 로그인 처리
                if (target.equals("/user/login")) {
                    String body = httpRequest.getBody(bufferedReader, Integer.parseInt(headers.get("Content-Length")));
                    Map<String, String> parameters = httpRequest.parseQuery(body);

                    if (QueryManager.checkLogin(parameters)) {
                        // 성공 페이지 리다이렉션
                        httpResponse.send302Response("/index.html", SessionManager.generateSessionId());
                        return;
                    }
                    // 실패 페이지로 리다이렉션
                    httpResponse.send302Response("/login/failed.html");
                }
            }*/

            String contentType = httpRequest.getContentType(target);
            byte[] file = httpResponse.readFile(DEFAULT_PATH + target);
            // 해당 경로에 파일이 존재하지 않을 때
            if (file == null) {
                httpResponse.send404Response(contentType);
                return;
            }
            httpResponse.send200Response(file, contentType);
        } catch (IOException e) {
            logger.error(e.getMessage());
        }
    }
}
