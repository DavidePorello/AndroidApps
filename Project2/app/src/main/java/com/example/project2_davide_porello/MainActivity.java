package com.example.project2_davide_porello;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    //array of images of animals
    private final ArrayList<Integer> images = new ArrayList<>(
            Arrays.asList(R.drawable.image1, R.drawable.image2, R.drawable.image3,
                        R.drawable.image4, R.drawable.image5, R.drawable.image6));

    //array of names of animals
    private final ArrayList<String> texts = new ArrayList<>(
            Arrays.asList("Giraffe", "Lion", "Zebra", "Rhino", "Crocodile", "Condor"));

    private MyAdapter adapter;
    private RecyclerView animalView;
    private RVClickListener listener;
    private boolean grid; //layout: true for grid and false for list

    protected static final String EXTRA_RES_ID = "POS";
    protected static final String LAYOUT = "GRID";

    @Override
    public void onSaveInstanceState(Bundle outState) {

        //save the layout for the configuration change
        outState.putBoolean(LAYOUT, grid);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        if(savedInstanceState != null)
            //retrieve the layout from the previous configuration
            grid = savedInstanceState.getBoolean(LAYOUT);
        else
            //default value grid
            grid = true;

        setContentView(R.layout.activity_main);

        animalView = (RecyclerView) findViewById(R.id.recycler_view);

        //listener used to launch web activity or full display activity
        listener = (web, position) -> {
            if(web) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://en.wikipedia.org/wiki/" + texts.get(position))); //implicit intent
                //start web page activity
                startActivity(i);
            }
            else {
                Intent intent = new Intent(MainActivity.this, ImageViewActivity.class); //explicit intent
                //add the ID of the image as an Intent Extra
                intent.putExtra(EXTRA_RES_ID, (int) images.get(position));
                //start the ImageViewActivity
                startActivity(intent);
            }
        };

        animalView.setHasFixedSize(true);
        //set the adapter of the recycler view with the layout and the listener
        adapter = new MyAdapter(texts, images, listener, grid);
        animalView.setAdapter(adapter);

        //set the layout manager depending on the layout (grid or list)
        animalView.setLayoutManager(grid ? new GridLayoutManager(this,2) : new LinearLayoutManager(this));

    }

    //create options menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    //set clicks on options menu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()) {
            case R.id.list:
                if(grid) {
                    //change from grid layout to list layout setting new adapter and layout manager
                    grid = false;
                    adapter = new MyAdapter(texts, images, listener, false);
                    animalView.setAdapter(adapter);
                    animalView.setLayoutManager(new LinearLayoutManager(this));
                }
                else
                    Toast.makeText(getApplicationContext(), "Already in the list view!", Toast.LENGTH_LONG).show();
                return true;
            case R.id.grid:
                if(!grid) {
                    //change from list layout to grid layout setting new adapter and layout manager
                    grid = true;
                    adapter = new MyAdapter(texts, images, listener, true);
                    animalView.setAdapter(adapter);
                    animalView.setLayoutManager(new GridLayoutManager(this,2));
                }
                else
                    Toast.makeText(getApplicationContext(), "Already in the grid view!", Toast.LENGTH_LONG).show();
                return true;
            default:
                return false;
        }
    }
}