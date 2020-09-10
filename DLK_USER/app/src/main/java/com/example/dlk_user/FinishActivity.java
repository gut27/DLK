package com.example.dlk_user;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;


public class FinishActivity extends AppCompatActivity {
    // 입력 받은 값에서 각 기능에대해 마스킹을 어떻게 할지 어떤 앱이 켜지는지 어떻게 받아올지 혹은 열수있는 앱을 통제하는 식으로 할건지
    private static final int REQ_CODE_OVERLAY_PERMISSION = 1;//마스킹 뷰 실행을 위한 변수
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    String information[] = new String[5];
    Intent intent_password = new Intent(getIntent());
    String password = intent_password.getStringExtra("password");
    EditText edit_password = (EditText)findViewById(R.id.password_finish);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("앱 종료");


        Button input_password = (Button)findViewById(R.id.input_password);

        input_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (String.valueOf(edit_password.getText()) ==  password){
                    closeView();
                    finishAffinity();
                    System.runFinalization();
                    System.exit(0);
                }
                //비밀번호가 입력안되어 있을경우 토스트와 비밀번호가 틀릴경우 토스트 보내기
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch( this, pendingIntent, null, null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(nfcAdapter != null) {
            nfcAdapter.enableForegroundDispatch( this, pendingIntent, null, null);
        }
    }
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter. EXTRA_NDEF_MESSAGES );
        if(rawMsgs != null) {
            NdefMessage msgs =(NdefMessage) rawMsgs[0];
            NdefRecord[] rec = msgs.getRecords();
            byte[] bt = rec[0].getPayload();
            String text = new String(bt);
            if(text == password){
                closeView();
                finishAffinity();
                System.runFinalization();
                System.exit(0);
                //크로즈 후 앱을 어떻게 할지도 의논 우선 앱을 종료시키겠음
            }
        }
    }
    public void closeView() {
        stopService(new Intent(this, Masking.class));
    }
}
