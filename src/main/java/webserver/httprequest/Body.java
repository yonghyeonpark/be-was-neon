package webserver.httprequest;

public class Body {

    private final String content;

    public Body(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
