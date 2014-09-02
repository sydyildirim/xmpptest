package com.seyda.st010.chatsample1;

/**
 * Created by st010 on 02.09.2014.
 */
public class Im {

    private String userId;
    private String conversationId;
    private Boolean send;

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Boolean getSend() {
        return send;
    }

    public void setSend(Boolean send) {
        this.send = send;
    }
}
