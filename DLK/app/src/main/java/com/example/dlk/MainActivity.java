package com.example.dlk;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent main = new Intent(this.getIntent());
        String value = main.getStringExtra("where");

        Button start_setting = findViewById(R.id.start_setting);
        start_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_setting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent_setting);
            }
        });

        /*
        Button tag_write = findViewById(R.id.tag_write);
        tag_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_nfcwrite = new Intent(MainActivity.this, NFCWrite.class);
                startActivity(intent_nfcwrite);
            }
        });

        Button restart_setting = findViewById(R.id.restart_setting);
        start_setting.setOnClickListener(new View.OnClickListener() {
            @restart_setting
            public void onClick(View view) {
                Intent intent_resetting = new Intent(MainActivity.this, SettingActivity.class);
                startActivity(intent_resetting);
            }
        });

         */

        if(value == "splash"){
            start_setting.setEnabled(true);
        }else{
            start_setting.setEnabled(false);
        }
    }
}