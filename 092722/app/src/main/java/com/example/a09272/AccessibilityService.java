package com.example.a09272;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.lang.reflect.Array;
import java.util.Arrays;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {
        private static final String TAG = "AccessibilityService";
        private static final int REQ_CODE_OVERLAY_PERMISSION = 1;//마스킹 뷰 실행을 위한 변수
        String[] Drop = {"com.lge.hifirecorder", "com.android.incallui", "com.android.contacts"};
        String[] Masking = {"com.kakao.talk", "com.android.chrome", "com.microsoft.office.powerpoint", "com.microsoft.office.word", "com.android.mms", "com.google.android.gm", "com.android.vending"};//sns 패키지 명도 추가
// 이벤트가 발생할때마다 실행되는 부분
@Override
public void onAccessibilityEvent(AccessibilityEvent event) {
        String packageName = String.valueOf(event.getPackageName());//;
        if(Arrays.asList(Drop).contains(packageName) || packageName.contains("camera")){

        }
        else if(Arrays.asList(Masking).contains(packageName)){//com.android.chrome com.microsoft.office.powerpoint com.microsoft.office.word  com.android.mms com.google.android.gm    com.android.vending          wooribank
                Integer num = event.getEventType();
                Log.e(TAG, String.valueOf(num));
                if(num == 32 || num == 8192){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startForegroundService(new Intent(this, Masking.class));
                        } else {
                                startService(new Intent(this, Masking.class));
                        }
                }
        }
        Log.e(TAG, "Catch Event Package Name : " + event.getPackageName());
        Log.e(TAG, "Catch Event TEXT : " + event.getText());
        Log.e(TAG, "Catch Event ContentDescription : " + event.getContentDescription());
        Log.e(TAG, "Catch Event getSource : " + event.getSource());
        Log.e(TAG, "=========================================================================");
}

// 접근성 권한을 가지고, 연결이 되면 호출되는 함수
public void onServiceConnected() {
        AccessibilityServiceInfo info = new AccessibilityServiceInfo();

        info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK; // 전체 이벤트 가져오기
        info.feedbackType = AccessibilityServiceInfo.DEFAULT | AccessibilityServiceInfo.FEEDBACK_HAPTIC;
        info.notificationTimeout = 100; // millisecond

        setServiceInfo(info);
        }

@Override
public void onInterrupt() {
        // TODO Auto-generated method stub
        Log.e("TEST", "OnInterrupt");
        }

}
