package com.example.suriya.spotdrivers.chat.chatroomfragment.MessageSupport;

import java.io.Serializable;

/**
 * Created by Suriya on 08-07-2017.
 */

public class ModelChatRoom implements Serializable {
    private String user_id;//mobile nummber
    private String chatRoomId;
    private String name;
    private String user_profile_pic;
    private String time;
    private String lastMessage;
    private  int unreadCount = 0;

    public  int getUnreadCount() {
        return unreadCount;
    }

    public void setUnreadCount(int unreadCount) {
        this.unreadCount = unreadCount;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getChatRoomId() {
        return chatRoomId;
    }

    public String getName() {
        return name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getUser_profile_pic() {
        return user_profile_pic;
    }

    public String getTime() {
        return time;
    }
}
