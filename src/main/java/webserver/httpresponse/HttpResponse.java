package webserver.httpresponse;

public class HttpResponse {

    private final String statusCode;
    private final byte[] file;
    private final String contentType;
    private final String location;
    private final String sessionId;

    public HttpResponse(String statusCode, byte[] file, String contentType) {
        this.statusCode = statusCode;
        this.file = file;
        this.contentType = contentType;
        this.location = null;
        this.sessionId = null;
    }

    public HttpResponse(String statusCode, String location, String sessionId) {
        this.statusCode = statusCode;
        this.file = null;
        this.contentType = null;
        this.location = location;
        this.sessionId = sessionId;
    }

    public HttpResponse(String statusCode, String location) {
        this.statusCode = statusCode;
        this.file = null;
        this.contentType = null;
        this.location = location;
        this.sessionId = null;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public byte[] getFile() {
        return file;
    }

    public String getContentType() {
        return contentType;
    }

    public String getLocation() {
        return location;
    }

    public String getSessionId() {
        return sessionId;
    }
}
