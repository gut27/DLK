package com.example.dlk;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class PasswordActivity extends AppCompatActivity {

    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("비밀번호 설정");

        Intent main_setting = new Intent(this.getIntent());

        final Intent intent_nfcwrite = new Intent(PasswordActivity.this, NFCWrite.class);
        intent_nfcwrite.putExtra("setting", main_setting.getStringArrayExtra("setting"));

        password = findViewById(R.id.password);

        Button input = findViewById(R.id.input_password);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass_num = String.valueOf(password.getText());
                // 패스워드 개수확인 빈곳인지도 확인if(pass_num)
                intent_nfcwrite.putExtra("password", pass_num);
                startActivity(intent_nfcwrite);
            }
        });





    }
}
