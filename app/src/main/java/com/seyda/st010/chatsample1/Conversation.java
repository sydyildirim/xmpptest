package com.seyda.st010.chatsample1;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by st010 on 02.09.2014.
 */
public class Conversation {
    private long conversationId;
    private String conversationName;
    private long userId;
    private Date last_seen;
    private long groupId;

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public long getGroupId() {
        return groupId;
    }

    public void setGroupId(long groupId) {
        this.groupId = groupId;
    }

    public String getConversationName() {
        return conversationName;
    }

    public void setConversationName(String conversationName) {
        this.conversationName = conversationName;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getLast_seen() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

    public void setLast_seen(Date last_seen) {
        this.last_seen = last_seen;
    }
}
