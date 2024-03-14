package webserver;

public enum ContentType {

    HTML("html", "text/html;charset=utf-8"),
    CSS("css", "text/css;charset=utf-8"),
    JS("js", "application/javascript;charset=utf-8"),
    ICO("ico", "image/x-icon"),
    PNG("png", "image/png"),
    JPG("jpg", "image/jpeg"),
    SVG("svg", "image/svg+xml");

    private final String name;
    private final String process;

    ContentType(String name, String process) {
        this.name = name;
        this.process = process;
    }

    public String getName() {
        return name;
    }

    public String getProcess() {
        return process;
    }
}
