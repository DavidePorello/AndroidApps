package com.example.app2_project3_davide_porello;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

public class WebFragment extends Fragment {

    private WebView website = null;
    private int currIdx = -1;
    private String[] websites;
    private MyViewModel model;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //prevent fragment from getting deleted when a configuration change occurs
        setRetainInstance(true);
    }

    //called to create the content view for this Fragment
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        //inflate the layout defined in web_site.xml
        return inflater.inflate(R.layout.web_site, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        website = (WebView) getActivity().findViewById(R.id.web);
        //get the attractions/hotels websites depending on the flag in the main activity
        websites = getResources().getStringArray(MainActivity.att ? R.array.AttractionsWebsites : R.array.HotelsWebsites);

        //get the ViewModel
        model = new ViewModelProvider(requireActivity()).get(MyViewModel.class);
        //define an observer on the item selected
        model.getSelectedItem().observe(getViewLifecycleOwner(), item -> {
            //update the UI
            if(item < 0)
                return;

            currIdx = item;
            website.loadUrl(websites[currIdx]);
        });
    }

}