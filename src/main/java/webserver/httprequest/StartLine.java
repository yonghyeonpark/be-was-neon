package webserver.httprequest;

public class StartLine {

    private final String method;
    private final String target;
    private final String httpVersion;

    public StartLine(String startLine) {
        String[] split = startLine.split(" ");
        this.method = split[0];
        this.target = split[1];
        this.httpVersion = split[2];
    }

    public String getMethod() {
        return method;
    }

    public String getTarget() {
        return target;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
