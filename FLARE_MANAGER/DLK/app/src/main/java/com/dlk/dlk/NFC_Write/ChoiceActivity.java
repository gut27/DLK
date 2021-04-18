package com.dlk.dlk.NFC_Write;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.dlk.dlk.R;

public class ChoiceActivity extends AppCompatActivity {
    Intent settings;
    String setting_information = "DLK,";
    String password_information = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choice);


//        settings = new Intent(this.getIntent());
//
//        String setting[] = settings.getStringArrayExtra("setting");
//        for(int i = 0; i <setting.length; i++){
//            setting_information = setting[i] + ",";
//        }
//        setting_information += settings.getStringExtra("password");
//        password_information = settings.getStringExtra("password");

        Button entrance = findViewById(R.id.entrance);
        entrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent entrance = new Intent(ChoiceActivity.this, NFCWriteSetting.class);
                //entrance.putExtra("setting", setting_information);
                startActivity(entrance);
            }
        });

        Button exit = findViewById(R.id.exit);
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent exit = new Intent(ChoiceActivity.this, NFCWritePassword.class);
                //exit.putExtra("password", password_information);
                startActivity(exit);
            }
        });

        Button finish = findViewById(R.id.finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent start_main = new Intent(ChoiceActivity.this, MainActivity.class);
//                startActivity(start_main);
//                ChoiceActivity.this.finish();
                moveTaskToBack(true);
                // 태스크를 백그라운드로 이동
                finishAndRemoveTask();
                // 액티비티 종료 + 태스크 리스트에서 지우기
                android.os.Process.killProcess(android.os.Process.myPid());
                //프로세스를 완전히 종료시킨다
            }
        });


    }
}
