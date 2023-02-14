package com.example.app1_project3_davide_porello;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String FALL_PERMISSION =
            "edu.uic.cs478.fall22.mp3" ;
    private static final String ATTRACTIONS_INTENT =
            "edu.uic.cs478.fall22.showAttractions";
    private static final String HOTELS_INTENT =
            "edu.uic.cs478.fall22.showHotels";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button attractions = findViewById(R.id.button);
        //set attractions button listener
        attractions.setOnClickListener(
                (view) ->  checkPermissionAndBroadcast(view) );
        Button hotels = findViewById(R.id.button2);
        //set hotels button listener
        hotels.setOnClickListener(
                (view) ->  checkPermissionAndBroadcast(view) );
    }

    //check permission
    private void checkPermissionAndBroadcast(View view) {
        if(ActivityCompat.checkSelfPermission(this, FALL_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            //if permission granted and clicked attractions button send attractions intent
            if(view.getId() == R.id.button) {
                Toast.makeText(this, "Launching attractions intent!", Toast.LENGTH_LONG).show();
                Intent attractionsIntent = new Intent(ATTRACTIONS_INTENT);
                sendBroadcast(attractionsIntent);
            }
            //if permission granted and clicked hotels button send hotels intent
            if(view.getId() == R.id.button2) {
                Toast.makeText(this, "Launching hotels intent!", Toast.LENGTH_LONG).show();
                Intent hotelsIntent = new Intent(HOTELS_INTENT);
                sendBroadcast(hotelsIntent);
            }
        }
        //if permission not granted ask permission to user
        else {
            if(view.getId() == R.id.button) {
                ActivityCompat.requestPermissions(this, new String[]{FALL_PERMISSION}, 0);
            }
            if(view.getId() == R.id.button2) {
                ActivityCompat.requestPermissions(this, new String[]{FALL_PERMISSION}, 1);
            }

        }
    }

    //handle permission response
    public void onRequestPermissionsResult(int code, @NonNull String[] permissions, @NonNull int[] results) {
        super.onRequestPermissionsResult(code, permissions, results);
        if(results.length > 0) {
            if(results[0] == PackageManager.PERMISSION_GRANTED) {
                //if permission granted and clicked attractions button send attractions intent
                if(code == 0) {
                    Toast.makeText(this, "Launching attractions intent!", Toast.LENGTH_LONG).show();
                    Intent attractionsIntent = new Intent(ATTRACTIONS_INTENT);
                    sendBroadcast(attractionsIntent);
                }
                //if permission granted and clicked hotels button send hotels intent
                if(code == 1) {
                    Toast.makeText(this, "Launching hotels intent!", Toast.LENGTH_LONG).show();
                    Intent hotelsIntent = new Intent(HOTELS_INTENT);
                    sendBroadcast(hotelsIntent);
                }
            }
            //if permission not granted show toast message
            else {
                Toast.makeText(this, "Bummer: No permission", Toast.LENGTH_SHORT).show();
            }
        }
    }

}