package com.til.prime.timesSubscription.dto.external;

public class PaymentsResponseObject {
    /** contains the same HTTP Status code returned by the server */
    String status;

    /** application specific error code */
    int statusCode;

    /** message describing the error */
    String message;

    /** extra information that might useful for developers */
    protected String description;

    protected Object data;

    protected String stringData;

    protected String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStringData() {
        return stringData;
    }

    public void setStringData(String stringData) {
        this.stringData = stringData;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("PaymentsResponseObject{");
        sb.append("status='").append(status).append('\'');
        sb.append(", statusCode=").append(statusCode);
        sb.append(", message='").append(message).append('\'');
        sb.append(", description='").append(description).append('\'');
        sb.append(", data=").append(data);
        sb.append(", stringData='").append(stringData).append('\'');
        sb.append(", type='").append(type).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
