package com.example.project1_davide_porello;

import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import android.content.Intent;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_CODE = 1;
    private Button button2;
    private String phone;
    private boolean dialer = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button1 = (Button) findViewById(R.id.button);
        //set listener for button1: if it is clicked start the phone activity
        button1.setOnClickListener(v -> switchToPhoneActivity());

        button2 = (Button) findViewById(R.id.button2);
        //initially disable button2 because there's no valid phone number
        button2.setEnabled(false);
        //set listener for button2
        button2.setOnClickListener(v -> {
            if(dialer) {
                //if phone activity has returned a valid phone number start (one-way) dial activity
                Intent i = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone)); //implicit intent
                startActivity(i);
            }
            else
                //if phone activity has not returned a valid phone number display a toast
                Toast.makeText(getApplicationContext(), "Incorrect number: " + phone , Toast.LENGTH_LONG).show();
        });
    }

    private void switchToPhoneActivity() {

        //start (two-way) phone activity
        Intent e = new Intent(MainActivity.this, PhoneActivity.class); //explicit intent
        startActivityForResult(e, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_CODE) {
            //retrieve the phone number from intent extra
            phone = data.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
            //if phone number is not null enable button2
            button2.setEnabled(phone != null);
            //if resultCode=RESULT_OK set dialer=true to enable dial activity
            dialer = resultCode == RESULT_OK;
        }
    }
}