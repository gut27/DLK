package com.dlp.dlk_user;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {
    private static final int RESULT_OK = 1;
    private static final int REQ_CODE_ACCESSIBILITY_PERMISSION = 3;
    private static final int REQ_CODE_ACCESSIBILITY_PERMISSION_END = 4;
// 입력 받은 값에서 각 기능에대해 마스킹을 어떻게 할지 어떤 앱이 켜지는지 어떻게 받아올지 혹은 열수있는 앱을 통제하는 식으로 할건지
    private static final int REQ_CODE_OVERLAY_PERMISSION = 2;//마스킹 뷰 실행을 위한 변수
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    String information[] = new String[10];
    Button finish;
    TextView check;
    Context context;
    Handler handler;
    Timer timer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        check = (TextView)findViewById(R.id.check);

        nfcAdapter = NfcAdapter. getDefaultAdapter (this);
        Intent intent = new Intent(this, getClass()).addFlags(Intent. FLAG_ACTIVITY_SINGLE_TOP );
        pendingIntent = PendingIntent. getActivity (this, 0, intent, 0);

        finish = (Button)findViewById(R.id.go_finish);
        finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent finish = new Intent(MainActivity.this, FinishActivity.class);
                startActivity(finish);
            }
        });

        Button temp_start;
        temp_start= (Button)findViewById(R.id.temp_start);
        temp_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PreferenceManager.setString(context,"camera","camera");
                PreferenceManager.setString(context, "hifirecorder", "hifirecorder");
                PreferenceManager.setString(context,"kakaotalk", "com.kakao.talk");
                PreferenceManager.setString(context, "chrome", "com.android.chrome");
                PreferenceManager.setString(context,"mms","com.android.mms");
                PreferenceManager.setString(context,"gm", "com.google.android.gm");
                PreferenceManager.setString(context,"password", "1234");
                popup();


            }
        });

        handler = new Handler();

        Intent end_intent = getIntent();
        String tmp = end_intent.getStringExtra("close");
        if(tmp == "close"){
            close();

            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    teskkill();
                }
            };
            timer = new Timer();
            timer.schedule(timerTask, 5000);

//앱완전죽이기
        }

        Intent onunbind = getIntent();
        if (onunbind.getStringExtra("onunbind") == "onunbind"){
            check.setBackgroundResource(R.drawable.image_uncheck);
            finish.setVisibility(INVISIBLE);
        }
        Intent connected = getIntent();
        if (connected.getStringExtra("connected") == "connected"){
            check.setBackgroundResource(R.drawable.image_cheked);
            finish.setVisibility(VISIBLE);
        }
//
//        settings();
//        popup();


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
            if(information[0].contains("FLARE_Entrance")){
                settings();
                popup();

                //openView();
            }else if(information[0].contains("FLARE_Exit")){
                String tp = PreferenceManager.getString(context, "password");
                if(information[1].equals(tp))
                {
                    close();
                    check.setBackgroundResource(R.drawable.image_uncheck);
                    finish.setVisibility(INVISIBLE);
                    TimerTask timerTask = new TimerTask() {
                        @Override
                        public void run() {

                            teskkill();
                        }
                    };
                    timer = new Timer();
                    timer.schedule(timerTask, 5000);

                }else{
                    Toast.makeText(context,"입구용 NFC를 먼저 입력해 주십시오.", Toast.LENGTH_LONG).show();
                }
            }
            else{
                check.setBackgroundResource(R.drawable.image_uncheck);
                finish.setVisibility(View.GONE);
                //토스트 보내기
            }
        }
    }

    public void popup()
    {
        Intent intent = new Intent(getApplicationContext(), PopopActivity.class);
        startActivityForResult(intent, RESULT_OK);


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                    start_permissions();
            } else {   // RESULT_CANCEL
                ///////////////////////////////////
                moveTaskToBack(true);
                // 태스크를 백그라운드로 이동
                finishAndRemoveTask();
                // 액티비티 종료 + 태스크 리스트에서 지우기
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }else if(requestCode == 2) {
            if (resultCode == 0) {
                start_permissions2();
            } else {   // RESULT_CANCEL
                ///////////////////////////////////
                moveTaskToBack(true);
                // 태스크를 백그라운드로 이동
                finishAndRemoveTask();
                // 액티비티 종료 + 태스크 리스트에서 지우기
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }else if(requestCode == 3) {
            if (resultCode == 0) {
                check.setBackgroundResource(R.drawable.image_cheked);
                finish.setVisibility(VISIBLE);
            } else {   // RESULT_CANCEL
                check.setBackgroundResource(R.drawable.image_uncheck);
                finish.setVisibility(INVISIBLE);
                ///////////////////////////////////
                moveTaskToBack(true);
                // 태스크를 백그라운드로 이동
                finishAndRemoveTask();
                // 액티비티 종료 + 태스크 리스트에서 지우기
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }else if(requestCode == 4) {
            if (resultCode == 0) {
                check.setBackgroundResource(R.drawable.image_uncheck);
                finish.setVisibility(INVISIBLE);
            } else {   // RESULT_CANCEL
                close();
            }
        }else{
        }
    }
    public void start_permissions() {

        if (Settings.canDrawOverlays(this))//Overlay사용가능여부 체크

        {
            start_permissions2();
        } else
            onObtainingPermissionOverlayWindow();//다른 앱위에 그리기 권한 획득 창이 나타나는 함수를 부른다.

    }

    public void start_permissions2() {

        if (!checkAccessibilityPermissions()) {
            setAccessibilityPermissions();
        }

    }


    public void settings(){
//        PreferenceManager.setString(context,"camera","camera");
//        PreferenceManager.setString(context, "hifirecorder", "false");
//        PreferenceManager.setString(context,"kakaotalk", "com.kakao.talk");
//        PreferenceManager.setString(context, "chrome", "false");
//        PreferenceManager.setString(context,"mms","kakaotalk");
//        PreferenceManager.setString(context,"gm", "false");
//        PreferenceManager.setString(context,"password", "1234");


        PreferenceManager.setString(context,"camera",information[1]);
        PreferenceManager.setString(context, "hifirecorder", information[2]);
        PreferenceManager.setString(context,"kakaotalk", information[3]);
        PreferenceManager.setString(context, "chrome", information[4]);
        PreferenceManager.setString(context,"mms",information[5]);
        PreferenceManager.setString(context,"gm", information[6]);
        PreferenceManager.setString(context,"password", information[7]);
    }

//허용안하면 꺼지게 하기
    //안드로이드 6.0 부터 화면 오버레이 권한 설정을 Manifest 입력만으로는 사용 못하게 막아서 아래 코드를 사용한다.
    public void onObtainingPermissionOverlayWindow() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        //Settings.ACTION_MANAGE_OVERLAY_PERMISSION에 현재 패키지 명을 넘겨 설정화면을 노출하게 한다.
        startActivityForResult(intent, REQ_CODE_OVERLAY_PERMISSION);
//        if (REQ_CODE_OVERLAY_PERMISSION != 2)
//        {
//            teskkill();
//        }else{
//            checkAccessibilityPermissions();
//        }

    }
    // 접근성 권한이 있는지 없는지 확인하는 부분
    // 있으면 true, 없으면 false
    public boolean checkAccessibilityPermissions() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);

        // getEnabledAccessibilityServiceList는 현재 접근성 권한을 가진 리스트를 가져오게 된다
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.DEFAULT);

        for (int i = 0; i < list.size(); i++) {
            AccessibilityServiceInfo info = list.get(i);

            // 접근성 권한을 가진 앱의 패키지 네임과 패키지 네임이 같으면 현재앱이 접근성 권한을 가지고 있다고 판단함
            if (info.getResolveInfo().serviceInfo.packageName.equals(getApplication().getPackageName())) {
                return true;
            }
        }
        return false;
    }

    // 접근성 설정화면으로 넘겨주는 부분
    public void setAccessibilityPermissions() {
        AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
        gsDialog.setTitle("접근성 권한 설정");
        gsDialog.setMessage("접근성 권한을 켜주세요");
        gsDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 설정화면으로 보내는 부분
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                //Settings.ACTION_MANAGE_OVERLAY_PERMISSION에 현재 패키지 명을 넘겨 설정화면을 노출하게 한다.
                startActivityForResult(intent, REQ_CODE_ACCESSIBILITY_PERMISSION);
                return;
            }
        }).create().show();
    }

    public void teskkill(){
        if(checkAccessibilityPermissions() == false) {
            Message msg = handler.obtainMessage();
            handler.sendMessage(msg);
            //핸들러를 이용하여 메인화면의 UI를 바꾼다.
            //그렇지 않으면 스레드 문제로 에러가 발생한다.
            timer.cancel();
            moveTaskToBack(true);
            // 태스크를 백그라운드로 이동
            finishAndRemoveTask();
            // 액티비티 종료 + 태스크 리스트에서 지우기
            android.os.Process.killProcess(android.os.Process.myPid());
            //프로세스를 완전히 종료시킨다
        }
    }
    public void close(){
        if(!close_checkAccessibilityPermissions()) {
            close_setAccessibilityPermissions();
        }
    }
    public boolean close_checkAccessibilityPermissions() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);

        // getEnabledAccessibilityServiceList는 현재 접근성 권한을 가진 리스트를 가져오게 된다
        List<AccessibilityServiceInfo> list = accessibilityManager.getEnabledAccessibilityServiceList(AccessibilityServiceInfo.DEFAULT);

        for (int i = 0; i < list.size(); i++) {
            AccessibilityServiceInfo info = list.get(i);

            // 접근성 권한을 가진 앱의 패키지 네임과 패키지 네임이 같으면 현재앱이 접근성 권한을 가지고 있다고 판단함
            if (info.getResolveInfo().serviceInfo.packageName.equals(getApplication().getPackageName())) {
                return false;
            }
        }
        return true;
    }

    // 접근성 설정화면으로 넘겨주는 부분
    public void close_setAccessibilityPermissions() {
        AlertDialog.Builder gsDialog = new AlertDialog.Builder(this);
        gsDialog.setTitle("접근성 권한 설정");
        gsDialog.setMessage("접근성 권한을 꺼주세요");
        gsDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 설정화면으로 보내는 부분
                Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                //Settings.ACTION_MANAGE_OVERLAY_PERMISSION에 현재 패키지 명을 넘겨 설정화면을 노출하게 한다.
                startActivityForResult(intent, REQ_CODE_ACCESSIBILITY_PERMISSION_END);
                return;
            }
        }).create().show();
    }

}