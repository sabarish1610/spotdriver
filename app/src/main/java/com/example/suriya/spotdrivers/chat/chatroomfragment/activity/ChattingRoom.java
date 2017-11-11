package com.example.suriya.spotdrivers.chat.chatroomfragment.activity;

import android.app.ActionBar;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.chat.chatroomfragment.messageSupportAdapter.RecyclerAdapter;
import com.example.suriya.spotdrivers.retrofit.ApiClient;
import com.example.suriya.spotdrivers.retrofit.RetInterface;
import com.example.suriya.spotdrivers.retrofit.support.Message;
import com.example.suriya.spotdrivers.retrofit.support.ServerResponse;
import com.example.suriya.spotdrivers.support.SupportConstant;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Suriya on 26-07-2017.
 */

public class ChattingRoom extends AppCompatActivity implements View.OnClickListener {
    private EditText message;
    private ImageView sendMessage;
    private SharedPreferences sharedPreference, sharedPreferences1;
    private String userId,sendUserToID,chatRoomId, loginType, name, imagePath;
    private RetInterface retInterface;
    private ServerResponse serverResponse;
    private RecyclerView recyclerView;
    private List<Message> messageList;
    private RecyclerView.Adapter adapter;
    private LinearLayoutManager layoutManager;
    private BroadcastReceiver broadcastReceiver;
    SharedPreferences.Editor editor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chating_fragment);
        sharedPreference = getSharedPreferences(SupportConstant.LOGGED_IN, Context.MODE_PRIVATE);
        userId = sharedPreference.getString(SupportConstant.LOGGEDIN_USERID,null);
        sharedPreferences1 = getSharedPreferences("count",Context.MODE_PRIVATE);
        editor = sharedPreferences1.edit();
        loginType = sharedPreference.getString(SupportConstant.LOGGEDIN_TYPE, null);
        recyclerView = (RecyclerView)findViewById(R.id.chat_recyclerView);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            sendUserToID = extras.getString(SupportConstant.SEND_TO_USER_ID);
            chatRoomId = extras.getString(SupportConstant.CHAT_ROOM_ID);
            imagePath = extras.getString(SupportConstant.IMAGE_PATH);
            name = extras.getString(SupportConstant.FIRST_NAME);
            editor.putInt(chatRoomId,0);
            editor.commit();
            Log.i("loginType", loginType);
            Log.i("sendTO",sendUserToID);
            Log.i("imagepath", imagePath);
        } else {
            // handle case
        }
        android.support.v7.app.ActionBar mActionBar = getSupportActionBar();
        LayoutInflater mInflater = LayoutInflater.from(this);
        View mCustomView = mInflater.inflate(R.layout.activity_action_bar, null);
        mActionBar.setDisplayShowHomeEnabled(false);
        mActionBar.setCustomView(mCustomView);
        mActionBar.setDisplayShowCustomEnabled(true);
        mActionBar.setDisplayShowTitleEnabled(false);
       // getSupportActionBar().setTitle(name);
        final ProgressBar progressBar  = (ProgressBar) mCustomView.findViewById(R.id.profile_image_loading);
        TextView mTitleTextView = (TextView) mCustomView.findViewById(R.id.action_bar_name);
        CircleImageView mDP = (CircleImageView) mCustomView.findViewById(R.id.action_bar_profile_pic);
        mTitleTextView.setText(name);
        progressBar.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext()).load(imagePath).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                progressBar.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                progressBar.setVisibility(View.INVISIBLE);
                return false;
            }
        }).into(mDP);
        recyclerView.setHasFixedSize(true);
        message = (EditText)findViewById(R.id.textMessage);
        sendMessage = (ImageView) findViewById(R.id.send);
        sendMessage.setOnClickListener(this);
        getMessageDetails(chatRoomId);
        broadcastReceiverFunction();
    }

   /* private void actionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM
                | ActionBar.DISPLAY_SHOW_HOME);
        ImageView imageView = new ImageView(actionBar.getThemedContext());
        imageView.setScaleType(ImageView.ScaleType.CENTER);
        imageView.setImageResource(R.drawable.ic_icon);
        ActionBar.LayoutParams layoutParams = new ActionBar.LayoutParams(
                ActionBar.LayoutParams.WRAP_CONTENT,
                ActionBar.LayoutParams.WRAP_CONTENT, Gravity.RIGHT
                | Gravity.CENTER_VERTICAL);
        layoutParams.rightMargin = 40;
        imageView.setLayoutParams(layoutParams);
        actionBar.setCustomView(imageView);
    }*/

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(broadcastReceiver,
                new IntentFilter("push"));
    }

    @Override
    protected void onPause() {
        super.onPause();
        editor.putInt(chatRoomId,0);
        editor.commit();
    }

    private void broadcastReceiverFunction() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals("push"))
                  getMessageDetails(chatRoomId);
                handlePushNotification(intent);
            }
        };
    }

    private void handlePushNotification(Intent intent) {
            if (adapter.getItemCount() > 1) {
                recyclerView.getLayoutManager().smoothScrollToPosition(recyclerView, null, adapter.getItemCount() - 1);
            }

    }

    private void getMessageDetails(String chatRoomId) {
        retInterface = ApiClient.getApiClient().create(RetInterface.class);
        Call<List<Message>> call = retInterface.getMessage(chatRoomId);
        call.enqueue(new Callback<List<Message>>(){
            @Override
            public void onResponse(Call<List<Message>> call, Response<List<Message>> response) {
                messageList = response.body();
                adapter = new RecyclerAdapter(messageList,getApplicationContext(),userId, SupportConstant.MESSAGELIST);
                recyclerView.setAdapter(adapter);
                recyclerView.scrollToPosition(adapter.getItemCount()-1);
            }

            @Override
            public void onFailure(Call<List<Message>> call, Throwable t) {

            }
        });
    }
    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.send:
                String strMessage = message.getText().toString().trim();
                if(strMessage.length()==0) {
                    Toast.makeText(this, "message shouldn't be empty", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendMessageFunction(sendUserToID,userId,strMessage, chatRoomId, loginType);
                sendMessage.setVisibility(View.INVISIBLE);
                Toast.makeText(ChattingRoom.this, sendUserToID, Toast.LENGTH_SHORT).show();
        }
    }

    private void sendMessageFunction(final String send , String userId, final String strMessage, String passchatRoomId, String loginType) {
        retInterface = ApiClient.getApiClient().create(RetInterface.class);
         Call<ServerResponse> call = retInterface.sendMessage(send ,userId, strMessage, passchatRoomId, loginType);
        call.enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                serverResponse = response.body();
                if(SupportConstant.SUCCESS.equals(serverResponse.getResult()))
                {
                    SharedPreferences sharedPreferences = getSharedPreferences("count", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(chatRoomId+"msg",strMessage);
                    editor.commit();
                    sendMessage.setVisibility(View.VISIBLE);
                    adapter.notifyDataSetChanged();
                    message.setText("");
                    getMessageDetails(chatRoomId);
                    Toast.makeText(getApplicationContext(), serverResponse.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                sendMessage.setVisibility(View.VISIBLE);
            }
        });
    }
}
