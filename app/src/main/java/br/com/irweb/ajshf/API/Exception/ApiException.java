package br.com.irweb.ajshf.API.Exception;

/**
 * Created by Igor on 22/05/2017.
 */

public class ApiException extends Exception {

    private String message;
    private int statusCode;

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
