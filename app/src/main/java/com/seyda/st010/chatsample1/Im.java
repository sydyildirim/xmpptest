package com.seyda.st010.chatsample1;

/**
 * Created by st010 on 02.09.2014.
 */
public class Im {

    private long imId;
    private long sender_userId;
    private long receiver_userId;
    private long conversationId;
    private String msgText;

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

    private Boolean send;

    public long getReceiver_userId() {
        return receiver_userId;
    }

    public void setReceiver_userId(long receiver_userId) {
        this.receiver_userId = receiver_userId;
    }

    public long getImId() {
        return imId;
    }

    public void setImId(long imId) {
        this.imId = imId;
    }

    public long getSender_userId() {
        return sender_userId;
    }

    public void setSender_userId(long sender_userId) {
        this.sender_userId = sender_userId;
    }

    public long getConversationId() {
        return conversationId;

    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public long getUserId() {
        return sender_userId;
    }

    public void setUserId(long userId) {
        this.sender_userId = sender_userId;
    }

    public Boolean getSend() {
        return send;
    }

    public void setSend(Boolean send) {
        this.send = send;
    }
}
