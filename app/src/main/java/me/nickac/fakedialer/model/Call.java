package me.nickac.fakedialer.model;

public class Call {

    private Contact contact;
    private boolean hdCall, workProfileCall, spamCall;
    private State state = State.NONE;

    public Call(Contact contact) {
        this.contact = contact;
    }

    public Call(Contact contact, boolean hdCall, boolean workProfileCall, boolean spamCall, State state) {
        this.contact = contact;
        this.hdCall = hdCall;
        this.workProfileCall = workProfileCall;
        this.spamCall = spamCall;
        this.state = state;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
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

    public boolean isSpamCall() {
        return spamCall;
    }

    public void setSpamCall(boolean spamCall) {
        this.spamCall = spamCall;
    }

    public enum State {
        NONE,
        /**
         * For when we do the call
         */
        OUTGOING,
        /**
         * For when the call comes to us.
         */
        INCOMING,
        /**
         *
         */
        HANG_UP
    }
}
