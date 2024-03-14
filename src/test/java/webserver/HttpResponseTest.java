package webserver;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class HttpResponseTest {

    HttpResponse httpResponse;

    @BeforeEach
    void setUp() {
        httpResponse = new HttpResponse();
    }

    @Test
    @DisplayName("path에 파일 확장자 .html이 있으면 content type으로 text/html;charset=utf-8을 얻습니다.")
    void getHtmlContentType() {
        String path = "/index.html";
        assertThat(httpResponse.getContentType(path)).isEqualTo("text/html;charset=utf-8");
    }

    @Test
    @DisplayName("path에 파일 확장자 .css가 있으면 content type으로 text/css;charset=utf-8을 얻습니다.")
    void getCssContentType() {
        String path = "/main.css";
        assertThat(httpResponse.getContentType(path)).isEqualTo("text/css;charset=utf-8");
    }

    @Test
    @DisplayName("path에 파일 확장자 .ico가 있으면 content type으로 image/x-icon을 얻습니다.")
    void getIcoContentType() {
        String path = "/favicon.ico";
        assertThat(httpResponse.getContentType(path)).isEqualTo("image/x-icon");
    }

    @Test
    @DisplayName("path에 파일 확장자 .png가 있으면 content type으로 image/png를 얻습니다.")
    void getPngContentType() {
        String path = "/signiture.png";
        assertThat(httpResponse.getContentType(path)).isEqualTo("image/png");
    }

    @Test
    @DisplayName("path에 파일 확장자 .svg가 있으면 content type으로 image/svg+xml을 얻습니다.")
    void getSvgContentType() {
        String path = "/bookMark.svg";
        assertThat(httpResponse.getContentType(path)).isEqualTo("image/svg+xml");
    }
}
