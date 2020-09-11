package com.example.dlk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    String[] setting = {"true","true"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("마스킹 설정");

        final Intent intent_password = new Intent(SettingActivity.this, PasswordActivity.class);

        CheckBox camera = (CheckBox) findViewById(R.id.check_camera);

        if(camera.isChecked() == false) setting[0] = "false";

        intent_password.putExtra("setting", setting);


        Button setup = findViewById(R.id.setup);
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent_password);
            }
        });
    }
}
