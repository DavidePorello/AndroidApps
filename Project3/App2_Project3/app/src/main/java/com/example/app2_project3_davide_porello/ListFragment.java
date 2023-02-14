package com.example.app2_project3_davide_porello;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;

public class ListFragment extends androidx.fragment.app.ListFragment {

    private int currIdx = -1;
    private MyViewModel model;
    private String[] list;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //prevent fragment from getting deleted when a configuration change occurs
        setRetainInstance(true);
    }

    @Override
    public void onViewCreated (@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //get the attractions/hotels depending on the flag in the main activity
        list = getResources().getStringArray(MainActivity.att ? R.array.Attractions : R.array.Hotels);
        //set the list choice mode to allow only one selection at a time
        getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        //set the list adapter for the ListView
        setListAdapter(new ArrayAdapter<String>(getActivity(), R.layout.list_item, list));

        //get the ViewModel
        model = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        //define an observer on the item selected
        model.getSelectedItem().observe(getViewLifecycleOwner(), item -> {
            //update the UI
            if(item < 0)
                //deselect the selected item
                getListView().setItemChecked(currIdx, false);
            else {
                //select the item
                currIdx = item;
                getListView().setItemChecked(currIdx, true);
            }
        });
    }

    //called when the user selects an item from the List, highlight that item and update the item in the ViewModel
    @Override
    public void onListItemClick(@NonNull ListView listView, @NonNull View view, int pos, long id) {
        currIdx = pos;
        model.selectItem(pos);
        listView.setItemChecked(currIdx, true);
    }

}
