package request;

public class RequestResponse {
    private int responseCode;
    private int contentLength;
    private String location;
    public RequestResponse(int responseCode, int contentLength, String location) {
        this.responseCode = responseCode;
        this.contentLength = contentLength;
        this.location = location;

    }
    public boolean shouldRedirect() {
        return responseCode >= 300 && responseCode <= 399;
    }

    public String getRedirectLocation() {
        return location;
    }
}
