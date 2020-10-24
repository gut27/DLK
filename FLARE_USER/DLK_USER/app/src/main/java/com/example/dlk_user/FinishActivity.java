package com.example.dlk_user;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import static android.view.View.VISIBLE;


public class FinishActivity extends AppCompatActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    String information[] = new String[2];
    EditText edit_password;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish);
        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("앱 종료");

        context = getApplicationContext();
        edit_password = (EditText)findViewById(R.id.password_finish);
        Button input_password = (Button)findViewById(R.id.input_password);

        input_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("fdf", "fdsf44d");
                String tp2 = String.valueOf(edit_password.getText());
                String tp3 = PreferenceManager.getString(context, "password");
                Log.e("d", tp2 + "3"+tp3);

                if (tp2.equals(tp3)){
                    Log.e("fdf", "fdsf77777d");
                    Log.e("fdf", "fdsfsd");
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    intent1.putExtra("close", "close");
                    startActivity(intent1);
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
            information = text.split(",");
            if(information[0] == "FLARE_Exit") {
                if (information[1] == PreferenceManager.getString(context, "password")) {
                    Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                    intent1.putExtra("close", "close");
                    startActivity(intent1);
                }
            }
        }
    }


}
