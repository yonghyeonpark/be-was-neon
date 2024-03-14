package webserver;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Map;

public class HttpRequestTest {

    HttpRequest httpRequest;

    @BeforeEach
    void setUp() {
        httpRequest = new HttpRequest();
    }

    @Test
    @DisplayName("request line에서 요청 target을 얻습니다.")
    void getRequestTarget() {
        String requestLine = "GET /registration/index.html HTTP/1.1";
        Assertions.assertThat(httpRequest.getTarget(requestLine)).isEqualTo("/registration/index.html");
    }

    @Test
    @DisplayName("쿼리를 파싱한 결과를 얻습니다.")
    void getQueryParsingResult() {
        String query = "userId=kkk12&name=park&password=1234&email=kkk12@naver.com";
        Map<String, String> parameters = httpRequest.parseQuery(query);
        Assertions.assertThat(parameters.get("userId")).isEqualTo("kkk12");
        Assertions.assertThat(parameters.get("name")).isEqualTo("park");
        Assertions.assertThat(parameters.get("password")).isEqualTo("1234");
        Assertions.assertThat(parameters.get("email")).isEqualTo("kkk12@naver.com");
    }
}
