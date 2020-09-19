package com.example.dlk.NFC_Write;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dlk.MainActivity;
import com.example.dlk.R;

public class ChoiceActivity extends AppCompatActivity {
    Intent settings;
    String setting_information = "DLK,";
    String password_information = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("NFC태그 만들기");

        settings = new Intent(this.getIntent());

        String setting[] = settings.getStringArrayExtra("setting");
        for(int i = 0; i <setting.length; i++){
            setting_information = setting[i] + ",";
        }
        setting_information += settings.getStringExtra("password");
        password_information = settings.getStringExtra("password");

        Button entrance = findViewById(R.id.entrance);
        entrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent entrance = new Intent(ChoiceActivity.this, NFCWriteSetting.class);
                entrance.putExtra("setting", setting_information);
                startActivity(entrance);
            }
        });

        Button exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent exit = new Intent(ChoiceActivity.this, NFCWritePassword.class);
                exit.putExtra("password", password_information);
                startActivity(exit);
            }
        });

        Button finish = findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent start_main = new Intent(ChoiceActivity.this, MainActivity.class);
                startActivity(start_main);
                ChoiceActivity.this.finish();
            }
        });


    }
}
