package com.example.app2_project3_davide_porello;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {

    private static final String ATTRACTIONS_INTENT =
            "edu.uic.cs478.fall22.showAttractions";
    private static final String HOTELS_INTENT =
            "edu.uic.cs478.fall22.showHotels";

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(ATTRACTIONS_INTENT)) {
            //start attractions activity
            MainActivity.att = true;
            Intent attractionsIntent = new Intent(context, AttractionsActivity.class);
            context.startActivity(attractionsIntent);
        }
        if(intent.getAction().equals(HOTELS_INTENT)) {
            //start hotels activity
            MainActivity.att = false;
            Intent hotelsIntent = new Intent(context, HotelsActivity.class);
            context.startActivity(hotelsIntent);
        }
    }
}