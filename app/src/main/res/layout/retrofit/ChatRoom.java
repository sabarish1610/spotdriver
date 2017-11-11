package com.miziontrix.chatapp.retrofit;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.miziontrix.chatapp.ChattingRoom;
import com.miziontrix.chatapp.R;
import com.miziontrix.chatapp.RegisteredUser;
import com.miziontrix.chatapp.adapter.RecyclerAdapter;
import com.miziontrix.chatapp.adapter.RecyclerItemClickListener;
import com.miziontrix.chatapp.firebaseNotification.MyFireBaseMessageService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by sabarish on 22/6/17.
 */

public class ChatRoom extends Fragment {
    private com.miziontrix.chatapp.retrofit.RetInterface retInterface;
    private FloatingActionButton floatingActionButton;
    private SharedPreferences sharedPreferences;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private BroadcastReceiver broadcastReceiver;
    private Fragment fragment;
    private List<com.miziontrix.chatapp.retrofit.ModelChatRoom> chatRoom;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.chat_room,container,false);
        sharedPreferences = getActivity().getSharedPreferences(com.miziontrix.chatapp.retrofit.Constant.LOGGED_IN, Context.MODE_PRIVATE);
        String userId = sharedPreferences.getString(com.miziontrix.chatapp.retrofit.Constant.USER_ID,null);
        recyclerView = (RecyclerView)view.findViewById(R.id.recycler_chat_room);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                fragment = new ChattingRoom();
                Bundle bundle = new Bundle();
                bundle.putString(com.miziontrix.chatapp.retrofit.Constant.SEND_TO_USER_ID, chatRoom.get(position).getUserId());
                bundle.putString(com.miziontrix.chatapp.retrofit.Constant.CHAT_ROOM_ID,chatRoom.get(position).getChatRoomId());
                fragment.setArguments(bundle);
                FragmentTransaction ft = getActivity().getFragmentManager().beginTransaction();
                ft.replace(R.id.register,fragment);
                ft.commit();
            }
        }));
        floatingActionButton = (FloatingActionButton)view.findViewById(R.id.floatingUserButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = new RegisteredUser();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.register,fragment);
                ft.commit();
            }
        });
        fetchingChatRoom(userId);
        broadcastReceiverfuncrion();
        Toast.makeText(getActivity(), userId, Toast.LENGTH_SHORT).show();
        return view;
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
        String chatRoomId = MyFireBaseMessageService.chatRoomId;
        if(chatRoomId!=null)
        {
            updateRow(chatRoomId);
        }
        Toast.makeText(getActivity(), "recived new Message"+ chatRoomId, Toast.LENGTH_SHORT).show();
    }

    private void updateRow(String chatRoomId) {
        for(com.miziontrix.chatapp.retrofit.ModelChatRoom cr: chatRoom)
        {
            if (cr.getChatRoomId().equals(chatRoomId))
            {
                int index = chatRoom.indexOf(cr);
               // cr.setLastMessage(message.getMessage());
                cr.setUnreadCount(cr.getUnreadCount() + 1);
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
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(broadcastReceiver);
    }

    private void fetchingChatRoom(final String userId)
    {
        retInterface = ApiClient.getApiClient().create(com.miziontrix.chatapp.retrofit.RetInterface.class);
        Call<List<com.miziontrix.chatapp.retrofit.ModelChatRoom>> call = retInterface.getRoomDetails(userId);
        call.enqueue(new Callback<List<com.miziontrix.chatapp.retrofit.ModelChatRoom>>() {
            @Override
            public void onResponse(Call<List<com.miziontrix.chatapp.retrofit.ModelChatRoom>> call, Response<List<com.miziontrix.chatapp.retrofit.ModelChatRoom>> response) {
                chatRoom = response.body();
                adapter = new RecyclerAdapter(userId, com.miziontrix.chatapp.retrofit.Constant.CHAT_ROOM,getActivity(),chatRoom);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onFailure(Call<List<com.miziontrix.chatapp.retrofit.ModelChatRoom>> call, Throwable t) {

            }
        });
    }
}
