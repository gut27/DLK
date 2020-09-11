package com.example.dlk_user;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
// 입력 받은 값에서 각 기능에대해 마스킹을 어떻게 할지 어떤 앱이 켜지는지 어떻게 받아올지 혹은 열수있는 앱을 통제하는 식으로 할건지
    private static final int REQ_CODE_OVERLAY_PERMISSION = 1;//마스킹 뷰 실행을 위한 변수
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    String information[] = new String[5];
    Button finish;
    ImageView check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        actionBar.show();
        actionBar.setTitle("마스킹 실행");

        check = (ImageView)findViewById(R.id.check);
        finish = (Button)findViewById(R.id.go_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finish = new Intent(MainActivity.this, FinishActivity.class);
                finish.putExtra("password", information[information.length-1]);
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
            //이 앱을 키고 다른 NFCtag를 읽었을때의 예외 처리
            if(information[0] == "DLK"){
                openView();
                check.setImageResource(R.drawable.cheked);
                finish.setVisibility(VISIBLE);
            }else{
                check.setImageResource(R.drawable.uncheck);
                finish.setVisibility(View.GONE);
                //토스트 보내기
            }
        }
    }

    public void openView() {
        if (Settings.canDrawOverlays(this))//Overlay사용가능여부 체크
            startService(new Intent(this, Masking.class));
        else
            onObtainingPermissionOverlayWindow();//다른 앱위에 그리기 권한 획득 창이 나타나는 함수를 부른다.
    }

    //안드로이드 6.0 부터 화면 오버레이 권한 설정을 Manifest 입력만으로는 사용 못하게 막아서 아래 코드를 사용한다.
    public void onObtainingPermissionOverlayWindow() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        //Settings.ACTION_MANAGE_OVERLAY_PERMISSION에 현재 패키지 명을 넘겨 설정화면을 노출하게 한다.
        startActivityForResult(intent, REQ_CODE_OVERLAY_PERMISSION);
    }
}