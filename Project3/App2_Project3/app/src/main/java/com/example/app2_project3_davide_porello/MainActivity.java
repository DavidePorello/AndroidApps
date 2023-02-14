package com.example.app2_project3_davide_porello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity {

    private static final String FALL_PERMISSION =
            "edu.uic.cs478.fall22.mp3";
    private static final String ATTRACTIONS_INTENT =
            "edu.uic.cs478.fall22.showAttractions";
    private static final String HOTELS_INTENT =
            "edu.uic.cs478.fall22.showHotels";

    private final BroadcastReceiver receiver = new MyReceiver();
    public static boolean att; //flag used to define different behaviour if we have to display attractions or hotels

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //register receiver that responds to both intent actions and specify that the sender must have the FALL_PERMISSION
        IntentFilter filter = new IntentFilter(ATTRACTIONS_INTENT);
        filter.addAction(HOTELS_INTENT);
        registerReceiver(receiver, filter, FALL_PERMISSION, null);
    }

    public void onDestroy() {
        super.onDestroy();

        //unregister receiver
        unregisterReceiver(receiver);
    }

    //create Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    //process clicks on Options Menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item:
                //start attractions activity
                att = true;
                Intent attractionsIntent = new Intent(this, AttractionsActivity.class);
                startActivity(attractionsIntent);
                return true;
            case R.id.item2:
                //start hotels activity
                att = false;
                Intent hotelsIntent = new Intent(this, HotelsActivity.class);
                startActivity(hotelsIntent);
                return true;
            default:
                return false;
        }
    }

}
