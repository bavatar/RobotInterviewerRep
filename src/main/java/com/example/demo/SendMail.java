package com.example.demo;

public class SendMail {
    private String from;

    private String to;

    private String cc;

    private String subject;

    private String message;

    private boolean isHtml;

    public SendMail() {
        this.to = new String();
        this.cc = new String();
    }

    public SendMail(String from, String toList, String subject, String message) {
        this();
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.to = toList;
    }

    public SendMail(String from, String toList, String ccList, String subject, String message) {
        this();
        this.from = from;
        this.subject = subject;
        this.message = message;
        this.to= toList;
        this.cc = ccList;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getCc() {
        return cc;
    }

    public void setCc(String cc) {
        this.cc = cc;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isHtml() {
        return isHtml;
    }

    public void setHtml(boolean html) {
        isHtml = html;
    }


    private String[] splitByComma(String toMultiple) {
        String[] toSplit = toMultiple.split(",");
        return toSplit;
    }


}
