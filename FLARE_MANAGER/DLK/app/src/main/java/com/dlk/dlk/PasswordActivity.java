package com.dlk.dlk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.dlk.dlk.NFC_Write.ChoiceActivity;

import java.util.regex.Pattern;

public class PasswordActivity extends AppCompatActivity {

    EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);




        final Intent intent_nfcwrite = new Intent(PasswordActivity.this, ChoiceActivity.class);

        final Context context = getApplicationContext();
        password = findViewById(R.id.password);

        Button input = findViewById(R.id.input_password);
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pass_num = String.valueOf(password.getText());
                if (pass_num.length() < 6 || pass_num.length() >10){
                    Toast.makeText(getApplicationContext(), "패스워드 길이는 6~10 사이여야 합니다. 다시 입력하세요", Toast.LENGTH_LONG).show();
                }else if(!Pattern.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[$@$!%*#&]).{6,10}.$", pass_num)) {
                    Toast.makeText(getApplicationContext(), "비밀번호 형식을 지켜주세요.", Toast.LENGTH_LONG).show();
                }else{
                    PreferenceManager.setString(context, "password", pass_num);
                    //intent_nfcwrite.putExtra("password", pass_num);
                    startActivity(intent_nfcwrite);
                }
            }
        });





    }
}
