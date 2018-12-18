package me.nickac.fakedialer.model;

public class Call {
    private Contact contact;
    private boolean hdCall, workProfileCall, forwardedCall, spamCall;
    private String forwardedNumber;


    public Call(Contact contact) {
        this.contact = contact;
        this.forwardedNumber = "";
    }

    public Call(Contact contact, boolean hdCall, boolean workProfileCall, boolean forwardedCall, boolean spamCall, String forwardedNumber) {
        this.contact = contact;
        this.hdCall = hdCall;
        this.workProfileCall = workProfileCall;
        this.forwardedCall = forwardedCall;
        this.spamCall = spamCall;
        this.forwardedNumber = forwardedNumber;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public boolean isHdCall() {
        return hdCall;
    }

    public void setHdCall(boolean hdCall) {
        this.hdCall = hdCall;
    }

    public boolean isWorkProfileCall() {
        return workProfileCall;
    }

    public void setWorkProfileCall(boolean workProfileCall) {
        this.workProfileCall = workProfileCall;
    }

    public boolean isForwardedCall() {
        return forwardedCall;
    }

    public void setForwardedCall(boolean forwardedCall) {
        this.forwardedCall = forwardedCall;
    }

    public boolean isSpamCall() {
        return spamCall;
    }

    public void setSpamCall(boolean spamCall) {
        this.spamCall = spamCall;
    }

    public String getForwardedNumber() {
        return forwardedNumber;
    }

    public void setForwardedNumber(String forwardedNumber) {
        this.forwardedNumber = forwardedNumber;
    }
}
