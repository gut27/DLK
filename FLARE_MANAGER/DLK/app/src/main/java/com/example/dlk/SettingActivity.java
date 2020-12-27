package com.example.dlk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("마스킹 설정");

        final Intent intent_password = new Intent(SettingActivity.this, PasswordActivity.class);
        final Context context = getApplicationContext();
        final CheckBox camera = (CheckBox) findViewById(R.id.check_camera);
        final CheckBox hifirecorder = (CheckBox) findViewById(R.id.check_hifirecorder);
        final CheckBox kakaotalk = (CheckBox) findViewById(R.id.check_kakaotalk);
        final CheckBox chrome = (CheckBox) findViewById(R.id.check_chrome);
        final CheckBox mms = (CheckBox) findViewById(R.id.check_mms);
        final CheckBox pp = (CheckBox) findViewById(R.id.check_pp);
        final CheckBox word = (CheckBox) findViewById(R.id.check_word);
        final CheckBox gm = (CheckBox) findViewById(R.id.check_gm);



        Button setup = findViewById(R.id.setup);
        setup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(camera.isChecked() == true){
                    PreferenceManager.setString(context, "camera", "camera");
                } else
                {PreferenceManager.setString(context, "camera", "false");}
                if(hifirecorder.isChecked() == true) {
                    PreferenceManager.setString(context, "hifirecorder", "hifirecorder");
                }
                else {PreferenceManager.setString(context, "hifirecorder", "false");}
                if(kakaotalk.isChecked() == true) PreferenceManager.setString(context, "kakaotalk", "com.kakao.talk");
                else PreferenceManager.setString(context, "kakaotalk", "false");
                if(chrome.isChecked() == true) PreferenceManager.setString(context, "chrome", "com.android.chrome");
                else PreferenceManager.setString(context, "chrome", "false");
                if(mms.isChecked() == true) PreferenceManager.setString(context, "mms", "com.android.mms");
                else PreferenceManager.setString(context, "mms", "false");
                if(pp.isChecked() == true) PreferenceManager.setString(context, "pp", "com.microsoft.office.powerpoint");
                else PreferenceManager.setString(context, "pp", "false");
                if(word.isChecked() == true) PreferenceManager.setString(context, "word", "com.microsoft.office.word");
                else PreferenceManager.setString(context, "word", "false");
                if(gm.isChecked() == true) PreferenceManager.setString(context, "gm", "com.google.android.gm");
                else PreferenceManager.setString(context, "gm", "false");
                startActivity(intent_password);
            }
        });
    }
}
