package com.example.app2_project3_davide_porello;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class AttractionsActivity extends AppCompatActivity {

    private WebFragment webFragment;
    private FragmentManager fragmentManager;
    private FrameLayout listFrameLayout, webFrameLayout;
    private MyViewModel model;
    private static final int MATCH_PARENT = LinearLayout.LayoutParams.MATCH_PARENT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragments);

        //get references to the ListFragment and to the WebFragment
        listFrameLayout = (FrameLayout) findViewById(R.id.list);
        webFrameLayout = (FrameLayout) findViewById(R.id.website);

        //get a reference to the SupportFragmentManager instead of original FragmentManager
        fragmentManager = getSupportFragmentManager();

        //check if a WebFragment already exists
        webFragment = (WebFragment) fragmentManager.findFragmentByTag("web");
        if(webFragment == null)
            //if it doesn't exist create it
            webFragment = new WebFragment();

        //check if the ListFragment already exists, if it doesn't exists create it
        if(fragmentManager.findFragmentByTag("list") == null) {
            //start a new FragmentTransaction
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            //add the ListFragment to the layout
            fragmentTransaction.add(R.id.list, new ListFragment(), "list");

            //commit the FragmentTransaction
            fragmentTransaction.commit();
        }

        //create the ViewModel
        model = new ViewModelProvider(this).get(MyViewModel.class);
        //add a OnBackStackChangedListener to reset the layout when the back stack changes
        fragmentManager.addOnBackStackChangedListener(() -> setLayout(model));

        //define an observer on the item selected
        model.getSelectedItem().observe(this, item -> {
            //we have deselected an item so we don't have to do anything
            if(item < 0)
                return;

            //if the webFragment is not added to the activity add it
            if(!webFragment.isAdded()) {
                //start a new FragmentTransaction
                FragmentTransaction fragmentTransaction2 = fragmentManager.beginTransaction() ;

                //add webFragment to display
                fragmentTransaction2.add(R.id.website, webFragment, "web");

                //add this FragmentTransaction to the backstack
                fragmentTransaction2.addToBackStack(null);

                //commit the FragmentTransaction
                fragmentTransaction2.commit();

                //force Android to execute the committed FragmentTransaction
                fragmentManager.executePendingTransactions();
            }
            //if the webFragment is already added to the activity set the right layout
            else
                setLayout(model);
        });
    }

    private void setLayout(MyViewModel model) {
        int orientation = this.getResources().getConfiguration().orientation;

        //if the webFragment is not added to the activity show only the ListFragment
        if(!webFragment.isAdded()) {
            listFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(
                    MATCH_PARENT, MATCH_PARENT));
            webFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                    MATCH_PARENT));
            //update the ViewModel to communicate that we want to deselect the selected item
            model.selectItem(-1);
        } else {
            //if the webFragment is already added to the activity and the configuration is portrait show only the WebFragment
            if(orientation == Configuration.ORIENTATION_PORTRAIT) {
                listFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT));
                webFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(MATCH_PARENT,
                        MATCH_PARENT));
            }
            //if the webFragment is already added to the activity and the configuration is landscape show 1/3 ListFragment and 2/3 WebFragment
            else {
                listFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 1f));
                webFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0,
                        MATCH_PARENT, 2f));
            }
        }
    }

    //create Options Menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return true;
    }

    //process clicks on OptionsMenu items
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item:
                //start attractions activity
                MainActivity.att = true;
                Intent attractionsIntent = new Intent(this, AttractionsActivity.class);
                startActivity(attractionsIntent);
                return true;
            case R.id.item2:
                //start hotels activity
                MainActivity.att = false;
                Intent hotelsIntent = new Intent(this, HotelsActivity.class);
                startActivity(hotelsIntent);
                return true;
            default:
                return false;
        }
    }

}