package com.seyda.st010.chatsample1;

/**
 * Created by st010 on 04.09.2014.
 */
public class Group {
    private long conversationId;
    private long groupMemberUserId;

    public long getConversationId() {
        return conversationId;
    }

    public void setConversationId(long conversationId) {
        this.conversationId = conversationId;
    }

    public long getGroupMemberUserId() {
        return groupMemberUserId;
    }

    public void setGroupMemberUserId(long groupMemberUserId) {
        this.groupMemberUserId = groupMemberUserId;
    }
}
