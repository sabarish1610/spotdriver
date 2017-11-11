package com.example.suriya.spotdrivers.chat.chatroomfragment.messageSupportAdapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.chat.chatroomfragment.MessageSupport.ModelChatRoom;
import com.example.suriya.spotdrivers.retrofit.support.Message;
import com.example.suriya.spotdrivers.support.SupportConstant;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by sabarish on 22/5/17.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {
    private Context context;
    //private List<Users> users;
    private List<Message> messages;
    private List<ModelChatRoom> chatRooms;
    private RecyclerViewHolder holder;
    private String userId;
    private String listType;
    private static String today;
    private int SELF = 143;
  /*  public RecyclerAdapter(Context context, List<Users> users, String listType) {
        this.context = context;
        this.users = users;
        this.listType = listType;
    }*/
   public RecyclerAdapter(List<Message> messages, Context context, String userId, String listType)
    {
        this.context = context;
        this.messages = messages;
       this.userId = userId;
        this.listType = listType;
        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }
    public RecyclerAdapter(String userId, String listType, Context context, List<ModelChatRoom> chatRoom)
    {
        this.context = context;
        this.userId = userId;
        this.listType = listType;
        this.chatRooms = chatRoom;

        Calendar calendar = Calendar.getInstance();
        today = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

  @Override
    public int getItemViewType(int position) {
       if(SupportConstant.MESSAGELIST.equals(listType)){
           Message message = messages.get(position);
           if (message.getUserId().equals(userId))
               return SELF;}
       return position;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view= null;
         if(SupportConstant.MESSAGELIST.equals(listType))
        {
            if(viewType == SELF)
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_thread,parent,false);
            else
               view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_thread_other,parent,false);
        }else if (SupportConstant.CHAT_ROOM.equals(listType))
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_room_name,parent,false);
        }
        holder = new RecyclerViewHolder(view);
        return holder;
    }
/*
set the text
 */
    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, final int position) {
         if (SupportConstant.MESSAGELIST.equals(listType)) {

            holder.forMessage.setText(messages.get(position).getMessage());
            holder.forTime.setText(getTimeStamp(messages.get(position).getDate()));
        }else if (SupportConstant.CHAT_ROOM.equals(listType))
        {
            Glide.with(context).load(chatRooms.get(position).getUser_profile_pic()).into(holder.chatUserProfilePic);
            holder.chatRoomUserName.setText(chatRooms.get(position).getName());
            holder.lastMessage.setText("");
            SharedPreferences sharedPreferences = context.getApplicationContext().getSharedPreferences("count",Context.MODE_PRIVATE);
            int count = sharedPreferences.getInt(chatRooms.get(position).getChatRoomId(),0);

//            String lastMessage =sharedPreferences.getString(chatRooms.get(position).getChatRoomId(),null);
            Log.d("chat","count in adapter chat room id"+chatRooms.get(position).getChatRoomId());
            Log.d("chat","count in adapter"+count);
            if(count>0)
            {
                //holder.count.setText(String.valueOf(chatRooms.get(position).getUnreadCount()));
                holder.count.setText(""+count);
                holder.count.setVisibility(View.VISIBLE);
            }else
            {
                holder.count.setVisibility(View.GONE);
            }
            //holder.lastMessage.setText(lastMessage);
             holder.timeStamp.setText(getTimeStamp(chatRooms.get(position).getTime().toString()));
            holder.lastMessage.setText(sharedPreferences.getString(chatRooms.get(position).getChatRoomId()+"msg",""));
        }
    }

    @Override
    public int getItemCount() {
        if (SupportConstant.MESSAGELIST.equals(listType))
            return messages.size();
        else if(SupportConstant.CHAT_ROOM.equals(listType))
            return chatRooms.size();
        return 0;
    }

    public static String getTimeStamp(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String timestamp = "";

        today = today.length() < 2 ? "0" + today : today;

        try {
            Date date = format.parse(dateStr);
            SimpleDateFormat todayFormat = new SimpleDateFormat("dd");
            String dateToday = todayFormat.format(date);
            format = dateToday.equals(today) ? new SimpleDateFormat("hh:mm a") : new SimpleDateFormat("dd LLL, hh:mm a");
            String date1 = format.format(date);
            timestamp = date1.toString();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return timestamp;
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder
    {
        private CircleImageView chatUserProfilePic;
        private TextView chatRoomUserName, lastMessage, timeStamp;
        private TextView forMessage, forTime, chatRoomName, count;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
           // forname = (TextView) itemView.findViewById(R.id.registeredusername);
            forMessage = (TextView)itemView.findViewById(R.id.textViewMessage);
            forTime = (TextView)itemView.findViewById(R.id.textViewTime);
            lastMessage = (TextView)itemView.findViewById(R.id.text_message);
           chatUserProfilePic = (CircleImageView)itemView.findViewById(R.id.chat_room_profile_pic);
            chatRoomUserName = (TextView)itemView.findViewById(R.id.chat_room_user_Name);
            timeStamp = (TextView)itemView.findViewById(R.id.timestamp);
            count = (TextView)itemView.findViewById(R.id.count);
        }
    }
}
