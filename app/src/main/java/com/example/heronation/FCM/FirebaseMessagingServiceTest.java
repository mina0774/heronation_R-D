package com.example.heronation.FCM;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.heronation.R;
import com.example.heronation.main.MainActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.io.IOException;
import java.net.URL;

// 메시지를 수신하여 알림으로 보여주는 Class
public class FirebaseMessagingServiceTest extends FirebaseMessagingService {

    private static final String TAG = "FirebaseMsgServiceTest";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "Refreshed token : " + token);
    }

    // 받은 메시지에서 title과 body 추출
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Title : " + remoteMessage.getNotification().getTitle());
            Log.d(TAG, "Message Notification Body : " + remoteMessage.getNotification().getBody());

            String msgTitle = remoteMessage.getNotification().getTitle();
            String msgBody = remoteMessage.getNotification().getBody();
            String click_action = remoteMessage.getData().get("click_action"); // 푸시를 보낼 때, 어디로 이동할지
            String image = remoteMessage.getData().get("image"); // 푸시에 나타낼 이모티콘

            Intent intent;

            intent = new Intent(this, MainActivity.class);
            intent.putExtra("click_action", click_action);
            intent.putExtra("image", image);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);


            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);
            String channelId = "Channel ID";
            Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channelId)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle(msgTitle)
                            .setContentText(msgBody)
                            .setAutoCancel(true)
                            .setSound(defaultSoundUri)
                            .setContentIntent(pendingIntent);
/*
            Bitmap bigIcon = null;
            try {
                URL url = new URL(image);
                //아이콘 처리
                bigIcon = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                notificationBuilder.setStyle(
                        new NotificationCompat.BigPictureStyle()
                                .bigPicture(bigIcon)
                                .setBigContentTitle(msgTitle)
                                .setSummaryText(msgBody));
            } catch (IOException e) {
                e.printStackTrace();
            }

*/
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                String channelName = "Channel Name";
                NotificationChannel channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0, notificationBuilder.build());
        }
    }
}