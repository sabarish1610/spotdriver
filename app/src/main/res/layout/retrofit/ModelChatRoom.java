package com.miziontrix.chatapp.retrofit;

import java.io.Serializable;

/**
 * Created by Suriya on 08-07-2017.
 */

public class ModelChatRoom implements Serializable {
    private String userId;
    private String chatRoomId;
    private String name;
    private int unreadCount;

    public int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getUserId() {
        return userId;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getName() {
        return name;
    }
}
