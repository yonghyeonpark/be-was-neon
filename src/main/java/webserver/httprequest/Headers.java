package webserver.httprequest;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Headers {

    private final Map<String, String> headers;

    public Headers() {
        this.headers = new HashMap<>();
    }

    public void addHeader(String headerName, String headerValue) {
        headers.put(headerName, headerValue);
    }

    public Set<String> getHeaderNames() {
        return headers.keySet();
    }

    public String getHeaderValue(String headerName) {
        return headers.get(headerName);
    }
}
