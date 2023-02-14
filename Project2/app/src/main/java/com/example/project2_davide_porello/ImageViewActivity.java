package com.example.project2_davide_porello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //get the intent used to start this activity
        Intent intent = getIntent();
        //make a new ImageView
        ImageView imageView = new ImageView(getApplicationContext());
        //get the ID of the image to display from the intent and set it as the image for this ImageView
        imageView.setImageResource(intent.getIntExtra(MainActivity.EXTRA_RES_ID, 0));
        //define the content of the activity
        setContentView(imageView);
    }
}