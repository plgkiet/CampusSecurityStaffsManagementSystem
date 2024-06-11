package model;

public class Request {
    private String content;
    private String sender;
    private STATE state;

    public static enum STATE {
        ACCEPTED, DENIED, WAITING;
    }

    public Request(String content, String sender) {
        this.content = content;
        this.sender = sender;
        this.state = STATE.WAITING;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public STATE getState() {
        return state;
    }

    public void setState(STATE state) {
        this.state = state;
    }

}
