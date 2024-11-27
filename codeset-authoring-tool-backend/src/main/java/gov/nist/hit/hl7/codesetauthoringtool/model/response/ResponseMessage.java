package gov.nist.hit.hl7.codesetauthoringtool.model.response;

import java.util.Date;

public class ResponseMessage<T> {
    private Status status;
    private String type;
    private String text;
    private String resourceId;
    private boolean hide;
    private Date date;
    private T data;
    public enum Status {
        SUCCESS, WARNING, INFO, FAILED;
    }

    public ResponseMessage(Status status, String text) {
        this.status = status;
        this.text = text;
    }
    public ResponseMessage(Status status, String type, String text, String resourceId, boolean hide, Date date, T data) {
        this.status = status;
        this.type = type;
        this.text = text;
        this.resourceId = resourceId;
        this.hide = hide;
        this.date = date;
        this.data = data;
    }

    public ResponseMessage(Status status, String message, String id, Date date) {
        this.status = status;
        this.text = message;
        this.resourceId = id;
        this.date = date;

    }
    public ResponseMessage(Status status, String message, String id, Date date, T data) {
        this.status = status;
        this.text = message;
        this.resourceId = id;
        this.date = date;
        this.data = data;

    }
    public ResponseMessage() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getResourceId() {
        return resourceId;
    }

    public void setResourceId(String resourceId) {
        this.resourceId = resourceId;
    }

    public boolean isHide() {
        return hide;
    }

    public void setHide(boolean hide) {
        this.hide = hide;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
