package com.example.flare_beta;

import android.app.PendingIntent;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Timer;

public class TestResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tresult);


        Context context = getApplicationContext();
        TextView textView_camera = (TextView)findViewById(R.id.textView4);
        TextView textView_record = (TextView)findViewById(R.id.textView5);

        textView_camera.setText(PreferenceManager.getString(context, "Camera"));
        textView_record.setText(PreferenceManager.getString(context,"Record"));
    }
}