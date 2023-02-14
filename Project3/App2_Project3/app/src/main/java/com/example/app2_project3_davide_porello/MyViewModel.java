package com.example.app2_project3_davide_porello;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyViewModel extends ViewModel {

    //item selected in the list
    private final MutableLiveData<Integer> selectedItem = new MutableLiveData<Integer>();

    //setter
    public void selectItem(Integer item) {
        selectedItem.setValue(item);
    }

    //getter
    public LiveData<Integer> getSelectedItem() {
        return selectedItem;
    }
}
