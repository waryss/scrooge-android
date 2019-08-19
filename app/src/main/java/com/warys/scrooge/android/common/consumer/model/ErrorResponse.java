package com.warys.scrooge.android.common.consumer.model;

import java.io.Serializable;
import java.util.Date;

public class ErrorResponse implements Serializable {
    private Date timestamp;
    private String message;
    private String exception;
    private String path;

    public ErrorResponse(Date timestamp, String message, String path, String exception) {
        super();
        this.timestamp = timestamp;
        this.message = message;
        this.exception = exception;
        this.path = path;
    }

    public String getException() {
        return exception;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getPath() {
        return path;
    }

    @Override
    public String toString() {
        return "ErrorResponse{" +
                "timestamp=" + timestamp +
                ", message='" + message + '\'' +
                ", exception='" + exception + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
