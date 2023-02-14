package com.example.audioclient_project5_davide_porello;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.project5_davide_porello.ClipPlayer;

public class MainActivity extends AppCompatActivity {

    private static final String ON_COMPLETION_INTENT = "edu.uic.cs478.fall22.onCompletion";

    private ClipPlayer clipPlayer;
    private boolean isBound = false;
    int n = 0;
    private Intent i;
    private Button startService, play, pause, resume, stop, stopService;

    //create ServiceConnection passed to bind and unbind the service
    private final ServiceConnection connection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className, IBinder iService) {
            //set the ClipPlayer object so that the client can use the functions exposed by the server
            clipPlayer = ClipPlayer.Stub.asInterface(iService);
            //executed when the user click on "play" button and the service is not bounded yet
            //not executed when the service is bounded after a configuration change
            if(!isBound) {
                isBound = true;
                try {
                    //after bounded to the service check if the service is started and in the case start the service
                    if(!clipPlayer.checkStarted())
                        startForegroundService(i);
                    clipPlayer.play(n);
                } catch (RemoteException e) {
                    Log.e("AudioClient", e.toString());
                }
            }
        }

        public void onServiceDisconnected(ComponentName className) {
            //reset all to the initial configuration if the service is forced to stop
            clipPlayer = null;
            isBound = false;
            startService.setEnabled(true);
            play.setEnabled(false);
            pause.setEnabled(false);
            resume.setEnabled(false);
            stop.setEnabled(false);
            stopService.setEnabled(false);
        }
    };

    //define the receiver used to unbind from the service when a clip is finished
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(ON_COMPLETION_INTENT) && isBound) {
                unbindService(connection);
                isBound = false;
                Toast.makeText(getApplicationContext(), "Clip finished", Toast.LENGTH_LONG).show();
                pause.setEnabled(false);
                stop.setEnabled(false);
            }
        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {

        //save the layout for the configuration change
        outState.putBoolean("startService", startService.isEnabled());
        outState.putBoolean("play", play.isEnabled());
        outState.putBoolean("pause", pause.isEnabled());
        outState.putBoolean("resume", resume.isEnabled());
        outState.putBoolean("stop", stop.isEnabled());
        outState.putBoolean("stopService", stopService.isEnabled());
        outState.putBoolean("isBound", isBound);
        super.onSaveInstanceState(outState);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startService = findViewById(R.id.button);
        play = findViewById(R.id.button2);
        pause = findViewById(R.id.button3);
        resume = findViewById(R.id.button4);
        stop = findViewById(R.id.button5);
        stopService = findViewById(R.id.button6);

        //explicit intent to start and bind the service
        i = new Intent(ClipPlayer.class.getName());
        ResolveInfo info = getPackageManager().resolveService(i, PackageManager.MATCH_ALL);
        i.setComponent(new ComponentName(info.serviceInfo.packageName, info.serviceInfo.name));

        if(savedInstanceState != null) {
            //retrieve the layout from the previous configuration
            startService.setEnabled(savedInstanceState.getBoolean("startService"));
            play.setEnabled(savedInstanceState.getBoolean("play"));
            pause.setEnabled(savedInstanceState.getBoolean("pause"));
            resume.setEnabled(savedInstanceState.getBoolean("resume"));
            stop.setEnabled(savedInstanceState.getBoolean("stop"));
            stopService.setEnabled(savedInstanceState.getBoolean("stopService"));
            isBound = savedInstanceState.getBoolean("isBound");
            if(isBound)
                bindService(i, connection, Context.BIND_AUTO_CREATE);
        }

        //register the receiver
        IntentFilter filter = new IntentFilter(ON_COMPLETION_INTENT);
        registerReceiver(receiver, filter);

        //define listener for "start service" button, start the foreground service
        startService.setOnClickListener(v -> {
            if(!isBound) {
                startForegroundService(i);
                startService.setEnabled(false);
                play.setEnabled(true);
                stopService.setEnabled(true);
            }
        });

        //take the instance of Spinner and apply OnItemSelectedListener on it which tells which item of spinner is clicked
        Spinner spin = findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                n = i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {}
        });

        Integer[] clips = {1, 2, 3, 4, 5};
        //create the instance of ArrayAdapter having the list of clips
        ArrayAdapter ad = new ArrayAdapter(this, android.R.layout.simple_spinner_item, clips);
        //set simple layout resource file for each item of spinner
        ad.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //set the ArrayAdapter (ad) data on the spinner which binds data to spinner
        spin.setAdapter(ad);

        //define listener for "play" button, bind the service if it not bounded yet
        play.setOnClickListener(v -> {
            if(!isBound) {
                bindService(i, connection, Context.BIND_AUTO_CREATE);
            }
            else {
                try {
                    clipPlayer.play(n);
                } catch (RemoteException e) {
                    Log.e("AudioClient", e.toString());
                }
            }
            pause.setEnabled(true);
            stop.setEnabled(true);
            resume.setEnabled(false);
        });

        //define listener for "pause" button
        pause.setOnClickListener(v -> {
            if(isBound) {
                try {
                    clipPlayer.pause();
                } catch (RemoteException e) {
                    Log.e("AudioClient", e.toString());
                }
                pause.setEnabled(false);
                resume.setEnabled(true);
            }
        });

        //define listener for "resume" button
        resume.setOnClickListener(v -> {
            if(isBound) {
                try {
                    clipPlayer.resume();
                } catch (RemoteException e) {
                    Log.e("AudioClient", e.toString());
                }
                resume.setEnabled(false);
                pause.setEnabled(true);
            }
        });

        //define listener for "stop" button, unbind the service
        stop.setOnClickListener(v -> {
            if(isBound) {
                try {
                    clipPlayer.stop();
                } catch (RemoteException e) {
                    Log.e("AudioClient", e.toString());
                }
                unbindService(connection);
                isBound = false;
                stop.setEnabled(false);
                pause.setEnabled(false);
                resume.setEnabled(false);
            }
        });

        //create alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //setting message and title manually and performing action on button click
        builder.setMessage("Do you want to stop the service?")
            .setTitle("AudioClient alert")
            .setCancelable(false)
            .setPositiveButton("YES", (dialog, id) -> {
                //action on "YES" button: stop the player, unbind the service and stop the service if necessary
                try {
                    if(isBound) {
                        clipPlayer.stop();
                        unbindService(connection);
                        isBound = false;
                    }
                    if(clipPlayer.checkStarted())
                        stopService(i);
                    else
                        Toast.makeText(getApplicationContext(),"Service already stopped", Toast.LENGTH_SHORT).show();

                } catch (RemoteException e) {
                    Log.e("AudioClient", e.toString());
                }

                stopService.setEnabled(false);
                startService.setEnabled(true);
                play.setEnabled(false);
                pause.setEnabled(false);
                resume.setEnabled(false);
                stop.setEnabled(false);
            })
            .setNegativeButton("NO", (dialog, id) -> {
                //action for "NO" button: cancel dialog
                dialog.cancel();
            });

        //define listener for "stop service" button
        stopService.setOnClickListener(v -> {
            //creating dialog box
            AlertDialog alert = builder.create();
            alert.show();
        });
    }

    protected void onDestroy() {
        super.onDestroy();

        //unbind the service and unregister the receiver if necessary
        if(isBound)
            unbindService(connection);
        if(receiver != null)
            unregisterReceiver(receiver);
    }

}