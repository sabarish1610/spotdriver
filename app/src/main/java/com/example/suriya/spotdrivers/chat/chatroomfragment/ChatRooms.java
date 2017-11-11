package com.example.suriya.spotdrivers.chat.chatroomfragment;

import android.annotation.TargetApi;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.IAmDriverActivity;
import com.example.suriya.spotdrivers.activity.needdriver.NeedDriverActivity;
import com.example.suriya.spotdrivers.adapter.RecyclerItemClickListener;
import com.example.suriya.spotdrivers.chat.chatroomfragment.MessageSupport.ModelChatRoom;
import com.example.suriya.spotdrivers.chat.chatroomfragment.activity.*;
import com.example.suriya.spotdrivers.chat.chatroomfragment.messageSupportAdapter.RecyclerAdapter;
import com.example.suriya.spotdrivers.services.MyFireBaseMessageService;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.support.SimpleDividerItemDecoration;
import com.example.suriya.spotdrivers.support.Singleton;
import com.example.suriya.spotdrivers.support.SupportConstant;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suriya on 25-07-2017.
 */

public class ChatRooms extends Fragment {
    private RetInterface retInterface;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private BroadcastReceiver broadcastReceiver;
    private String userId, loginType;
    private List<ModelChatRoom> chatRoom;
    private ProgressDialog progressDialog;
    private FrameLayout framentLayout;
    @Nullable
    @Override
    public View onCreateView( LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        framentLayout = (FrameLayout)inflater.inflate(R.layout.chat_room, null);
        sharedPreferences = getActivity().getSharedPreferences(SupportConstant.LOGGED_IN, Context.MODE_PRIVATE);
        userId = sharedPreferences.getString(SupportConstant.LOGGEDIN_USERID,null);
        loginType = sharedPreferences.getString(SupportConstant.LOGGEDIN_TYPE,null);
        recyclerView = (RecyclerView)framentLayout.findViewById(R.id.recycler_chat_room);
         Toolbar toolbar = (Toolbar)framentLayout.findViewById(R.id.chat_room_toolbar);
        toolbar.setTitle("Chat Room");
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        progressDialog = Singleton.getInstance().getProgressDialog(getActivity());
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.addItemDecoration(new SimpleDividerItemDecoration(getActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getActivity(), ChattingRoom.class);
                intent.putExtra(SupportConstant.FIRST_NAME,chatRoom.get(position).getName());
                intent.putExtra(SupportConstant.SEND_TO_USER_ID,chatRoom.get(position).getUser_id());
                intent.putExtra(SupportConstant.CHAT_ROOM_ID,chatRoom.get(position).getChatRoomId());
                intent.putExtra(SupportConstant.IMAGE_PATH, chatRoom.get(position).getUser_profile_pic());
                getActivity().startActivity(intent);
            }
        }));
            fetchingChatRoom(userId, loginType);
        broadcastReceiverfuncrion();
        if (loginType.contentEquals(SupportConstant.DRIVER))
        ((IAmDriverActivity)getActivity()).progressBar(progressDialog);
        else if(loginType.contentEquals(SupportConstant.USER))
            Singleton.getInstance().progressBar(progressDialog);
           //((NeedDriverActivity)getActivity()).progressBar(progressDialog);
       // Toast.makeText(getActivity(), userId +" "+ loginType, Toast.LENGTH_SHORT).show();
        progressDialog.show();
        return framentLayout;
    }

    private void broadcastReceiverfuncrion() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("push"))
                    //Toast.makeText(context, "recieved new message", Toast.LENGTH_SHORT).show();
                    handleBackground(intent);
            }
        };
    }

    private void handleBackground(Intent intent) {
       // Message message = (Message) intent.getSerializableExtra("message");
       fetchingChatRoom(userId, loginType);
        String chatRoomId = MyFireBaseMessageService.chatRoomId;
       String lastMesage = MyFireBaseMessageService.message;
       Log.i("last_message", lastMesage);
        if(chatRoomId!=null)
        {
            updateRow(chatRoomId);

        }
        Toast.makeText(getActivity(), "recived new Message"+ chatRoomId, Toast.LENGTH_SHORT).show();
    }

    private void updateRow(String chatRoomId) {
       SharedPreferences sharedPreferences = getActivity().getSharedPreferences("count",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        for(ModelChatRoom cr: chatRoom)
        {
            if (cr.getChatRoomId().equals(chatRoomId))
            {
                int index = chatRoom.indexOf(cr);
                // cr.setLastMessage(message.getMessage());
               // cr.setUnreadCount(cr.getUnreadCount() + 1);

                cr.setUnreadCount(sharedPreferences.getInt(cr.getChatRoomId(),0));
                Log.d("chat","chat room id "+cr.getChatRoomId());
                Log.d("chat","chat count "+cr.getUnreadCount());
                editor.putInt(cr.getChatRoomId(),cr.getUnreadCount());
                editor.commit();

                chatRoom.remove(index);
                chatRoom.add(index, cr);
                break;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(broadcastReceiver,new IntentFilter("push"));
       // getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
       // getActivity().startActivity(new Intent(getActivity(), IAmDriverActivity.class));
        fetchingChatRoom(userId, loginType);
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    private void fetchingChatRoom(final String userId, String loginType)
    {
        Toast.makeText(getActivity(), userId, Toast.LENGTH_SHORT).show();
        retInterface = ApiClient.getApiClient().create(RetInterface.class);
        Call<List<ModelChatRoom>> call = retInterface.getRoomDetails(userId, loginType);
        call.enqueue(new Callback<List<ModelChatRoom>>() {
            @Override
            public void onResponse(Call<List<ModelChatRoom>> call, Response<List<ModelChatRoom>> response) {
                //Toast.makeText(getActivity(), "Response", Toast.LENGTH_SHORT).show();
                chatRoom = response.body();
                progressDialog.dismiss();
                adapter = new RecyclerAdapter(userId, SupportConstant.CHAT_ROOM,getActivity(),chatRoom);
                //Toast.makeText(getActivity(), chatRoom.get(0).getChatRoomId(), Toast.LENGTH_SHORT).show();
                if (!(adapter.getItemCount() > 0))
                {
                    LayoutInflater inflater = null;
                    try {
                        if(inflater == null)
                        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    } catch (NullPointerException e)
                    {
                        inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    }
                    framentLayout.removeAllViews();
                    inflater.inflate(R.layout.no_chat, framentLayout);
                    TextView click = (TextView) getView().findViewById(R.id.starts_chat_room_click);
                    if(sharedPreferences.getString(SupportConstant.LOGGEDIN_TYPE,null).contentEquals(SupportConstant.DRIVER))
                        click.setText("");
                    click.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((NeedDriverActivity)getActivity()).navigation.setSelectedItemId(R.id.drivers);
                        }
                    });

                }else
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<ModelChatRoom>> call, Throwable t) {
                Toast.makeText(getActivity(), "failure", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
