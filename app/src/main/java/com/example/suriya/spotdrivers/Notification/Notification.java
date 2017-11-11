package com.example.suriya.spotdrivers.Notification;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.suriya.spotdrivers.R;
import com.example.suriya.spotdrivers.activity.driver.IAmDriverActivity;
import com.example.suriya.spotdrivers.activity.needdriver.NeedDriverActivity;
import com.example.suriya.spotdrivers.support.SupportConstant;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Suriya on 30-06-2017.
 */

public class Notification {
    private Context context;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    public Notification(Context context) {
        this.context = context;
    }
    public void pushNotification(String title, String message)
    {
        sharedPreferences = context.getSharedPreferences(SupportConstant.LOGGED_IN,Context.MODE_PRIVATE);
        String logginType = sharedPreferences.getString(SupportConstant.LOGGEDIN_TYPE, null);
        editor = sharedPreferences.edit();
        Intent intent = null;
        if (logginType.contentEquals(SupportConstant.USER))
            intent = new Intent(context, NeedDriverActivity.class);
        else if (logginType.contentEquals(SupportConstant.DRIVER))
            intent = new Intent(context, IAmDriverActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("notif", "notif");
        /*
        * */
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        editor.putString(SupportConstant.MESSAGE_RECIEVED, message);
        editor.apply();
        String oldNotification = sharedPreferences.getString(SupportConstant.MESSAGE_RECIEVED, null);
        List<String> messages = Arrays.asList(oldNotification.split("\\|"));
        for (int i = messages.size() - 1; i >= 0; i--) {
            inboxStyle.addLine(messages.get(i));
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle(title);
        builder.setContentText(message);
        builder.setSound(alarmSound);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentIntent(pendingIntent).setStyle(inboxStyle);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0,builder.build());
    }
    public static boolean isAppIsBackground(Context context)
    {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)
        {
            List<ActivityManager.RunningAppProcessInfo> appProcessInfos = am.getRunningAppProcesses();
            for(ActivityManager.RunningAppProcessInfo processInfo : appProcessInfos)
            {
                if(processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND)
                {
                    for (String activeProcess : processInfo.pkgList)
                    {
                        if (activeProcess.equals(context.getPackageName()))
                            isInBackground = false;
                    }
                }
            }
        }else {
            List<ActivityManager.RunningTaskInfo> taskInfos = am.getRunningTasks(1);
            ComponentName componentName = taskInfos.get(0).topActivity;
            if(componentName.getPackageName().equals(context.getPackageName()))
                isInBackground = false;
        }
        return isInBackground;
    }
}
