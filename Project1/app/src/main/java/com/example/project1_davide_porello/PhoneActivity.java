package com.example.project1_davide_porello;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import java.util.regex.Pattern;

public class PhoneActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);

        EditText number = (EditText) findViewById(R.id.editPhoneNumber);
        //get intent from calling activity
        Intent e = getIntent();
        //set listener for enter key
        number.setOnEditorActionListener((v, actionId, event) -> {
            if(actionId == EditorInfo.IME_NULL || actionId == EditorInfo.IME_ACTION_DONE) {
                //get the number inserted
                String phone = number.getText().toString();
                //add the number to the intent as an extra
                e.putExtra(Intent.EXTRA_PHONE_NUMBER, phone);
                //check the number with a regular expression
                boolean right = Pattern.matches("([0-9]{10})|(\\([0-9]{3}\\)\\s?[0-9]{3}-[0-9]{4})", phone);
                if(right)
                    //set the result for a valid number
                    setResult(RESULT_OK, e);
                else
                    //set the result for an invalid number
                    setResult(RESULT_CANCELED, e);
                //terminate the activity and return to the main activity
                finish();
            }
            return true;
        });
    }
}

