package com.example.clipserver_project5_davide_porello;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.util.ArrayList;
import java.util.Arrays;

import com.example.project5_davide_porello.ClipPlayer;

public class ClipServer extends Service {

    private static final int NOTIFICATION_ID = 1;
    private static final String CHANNEL_ID = "Music player style";
    private static final String ON_COMPLETION_INTENT = "edu.uic.cs478.fall22.onCompletion";

    private MediaPlayer player = null;
    private boolean started = false;
    private final ArrayList<Integer> clips = new ArrayList<>(
            Arrays.asList(R.raw.sample1, R.raw.sample2, R.raw.sample3,
                    R.raw.sample4, R.raw.sample5));

    @Override
    public void onCreate() {

        super.onCreate();

        //create a notification area so the user can get back to the this service
        this.createNotificationChannel();

        final Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_IMMUTABLE);

        Notification notification = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_media_play)
            .setOngoing(true).setContentTitle("Music Player")
            .setContentText("Click to access Clip Server")
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.ic_launcher_foreground, "Show service", pendingIntent)
            .build();

        //put this Service in a foreground state, so it won't readily be killed by the system
        startForeground(NOTIFICATION_ID, notification);
    }

    private void createNotificationChannel() {
        //create the NotificationChannel
        NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "Music player notification", NotificationManager.IMPORTANCE_DEFAULT);
        channel.setDescription("The channel for music player notifications");
        // Register the channel with the system
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        started = true;
        return START_NOT_STICKY;
    }

    //implement the Stub for this ClipPlayer object
    private final ClipPlayer.Stub binder = new ClipPlayer.Stub() {

        //implement the remote methods

        //start playing a clip and define listener when the clip has finished that sends a broadcast to the client
        //if the same clip is already playing start from the beginning
        public void play(int n) {
            if(player != null) {
                player.stop();
            }
            player = MediaPlayer.create(getApplicationContext(), clips.get(n-1));
            player.setLooping(false);
            player.setOnCompletionListener(mp -> {
                Intent i = new Intent(ON_COMPLETION_INTENT);
                sendBroadcast(i);
            });
            player.start();
        }

        //pause the clip
        public void pause() {
            if(player != null && player.isPlaying())
                player.pause();
        }

        //resume the clip
        public void resume() {
            if(player != null && !player.isPlaying())
                player.start();
        }

        //stop the clip
        public void stop() {
            if(player != null)
                player.stop();
        }

        //return if the service is started or only bounded
        public boolean checkStarted() {
            return started;
        }
    };

    //return the Stub defined above
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //release the resources
        if(player != null) {
            player.stop();
            player.release();
        }
    }
}