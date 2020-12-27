package com.example.flare_beta;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.flare_beta.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import static android.view.View.VISIBLE;

public class MainActivity extends AppCompatActivity {

    private static final int REQ_CODE_OVERLAY_PERMISSION = 1;
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;
    TextView check;
    Context context;
    Timer timer;
    Button button_start;
    Handler handler_perimission, handler;
    Integer check_close = 0;
    private Timer timer_flare;
    private Handler handler_permission, handler_sv;
//    private CustomRunnable customRnnable;
    Integer tag =1;
    Integer tag2 = 0;
    Timer timer_sf;
    TimerTask timerTask_sf;
    int timerCount_sf;
    Handler handler_sf;
    TextView tvTimer;
    TextToSpeech tts;
    Runnable runnable_scp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();

        button_start = (Button) findViewById(R.id.button_start);
        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup();
            }
        });
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status != TextToSpeech.ERROR) {
                    // 언어를 선택한다.
                    tts.setLanguage(Locale.KOREAN);

                }
            }
        });


//
        check = (TextView) findViewById(R.id.check);



    }

    public void popup()
    {
        Intent intent = new Intent(getApplicationContext(), PopopActivity.class);
        startActivityForResult(intent, 1);
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

        }
    }


    public void start_permissions() {

        if(!checkAccessibilityPermissions()) {
            setAccessibilityPermissions();
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if(!Settings.canDrawOverlays(MainActivity.this)) {
                    onObtainingPermissionOverlayWindow();
                }
            }

        }, 7000);

                    // 2초간 멈추게 하고싶다면
            Handler handler2 = new Handler();
            handler2.postDelayed(new Runnable() {
                public void run() {
                    tts.speak("앱이 실행되었습니다.", TextToSpeech.QUEUE_FLUSH, null);
                    button_start.setVisibility(View.INVISIBLE);
                    check.setBackgroundResource(R.drawable.image_cheked);
                    String conversionTime = "000100";
                    // 카운트 다운 시작
                    countDown(conversionTime);
                    start_flare();
                    }

            }, 9000);

    }


    public void start_flare(){

        handler_sf = new Handler();
        timer_sf = new Timer();
        runnable_scp = new Runnable() {
            @Override
            public void run() {
                if(!checkAccessibilityPermissions()&&!Settings.canDrawOverlays(MainActivity.this))
                    tts.speak("권한을 앱 실행 도중 종료하셨습니다. 처음부터 다시 시작하여 주세요.", TextToSpeech.QUEUE_FLUSH, null);

                    button_start.setVisibility(VISIBLE);
                    check.setBackgroundResource(R.drawable.image_uncheck);
                    teskkill();

            }
        };

        timerTask_sf = new TimerTask() {
            @Override
            public void run() {
                handler_sf.post(runnable_scp);
            }
        };

        timer_sf.schedule(timerTask_sf, 12000,10000);
//검사시간


    }




    public void countDown(String time) {

        long conversionTime = 0;

        // 1000 단위가 1초
        // 60000 단위가 1분
        // 60000 * 3600 = 1시간

        String getHour = time.substring(0, 2);
        String getMin = time.substring(2, 4);
        String getSecond = time.substring(4, 6);

        // "00"이 아니고, 첫번째 자리가 0 이면 제거
        if (getHour.substring(0, 1) == "0") {
            getHour = getHour.substring(1, 2);
        }

        if (getMin.substring(0, 1) == "0") {
            getMin = getMin.substring(1, 2);
        }

        if (getSecond.substring(0, 1) == "0") {
            getSecond = getSecond.substring(1, 2);
        }

        // 변환시간
        conversionTime = Long.valueOf(getHour) * 1000 * 3600 + Long.valueOf(getMin) * 60 * 1000 + Long.valueOf(getSecond) * 1000;

        // 첫번쨰 인자 : 원하는 시간 (예를들어 30초면 30 x 1000(주기))
        // 두번쨰 인자 : 주기( 1000 = 1초)
        new CountDownTimer(conversionTime, 1000) {

            // 특정 시간마다 뷰 변경
            public void onTick(long millisUntilFinished) {

                // 분단위
                long getMin = millisUntilFinished - (millisUntilFinished / (60 * 60 * 1000)) ;
                String min = String.valueOf(getMin / (60 * 1000)); // 몫

                // 초단위
                String second = String.valueOf((getMin % (60 * 1000)) / 1000); // 나머지

                // 밀리세컨드 단위
                //String millis = String.valueOf((getMin % (60 * 1000)) % 1000); // 몫

                // 분이 한자리면 0을 붙인다
                if (min.length() == 1) {
                    min = "0" + min;
                }

                // 초가 한자리면 0을 붙인다
                if (second.length() == 1) {
                    second = "0" + second;
                }

                check.setText(min + ":" + second);
            }

            // 제한시간 종료시
            public void onFinish() {

                // 변경 후
//                check.setText("테스크 종료");
//                button_start.setVisibility(VISIBLE);
//                check.setBackgroundResource(R.drawable.image_uncheck);
//                teskkill();
                // TODO : 타이머가 모두 종료될때 어떤 이벤트를 진행할지

            }
        }.start();

    }
//허용안하면 꺼지게 하기
    //안드로이드 6.0 부터 화면 오버레이 권한 설정을 Manifest 입력만으로는 사용 못하게 막아서 아래 코드를 사용한다.
    public void onObtainingPermissionOverlayWindow() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
        //Settings.ACTION_MANAGE_OVERLAY_PERMISSION에 현재 패키지 명을 넘겨 설정화면을 노출하게 한다.
        startActivityForResult(intent, REQ_CODE_OVERLAY_PERMISSION);
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
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                return;
            }
        }).create().show();
    }

    public void teskkill(){
        timer_sf.cancel();
        handler_sf.removeCallbacks(runnable_scp);
        close();

    }
    public void close(){

        if(!close_checkAccessibilityPermissions()) {
            close_setAccessibilityPermissions();
        }
        Intent intent_result = new Intent(MainActivity.this, TestResultActivity.class);
        startActivity(intent_result);

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
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                return;
            }
        }).create().show();
    }

}