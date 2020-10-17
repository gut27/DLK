package com.example.dlk_user;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

import java.util.Arrays;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {
        // !--드롭이나 마스킹 하고싶은 앱이 있을경우 뒤에 패키지명 만 추가하면 원하는 기능 수행 가능
        String[] Masking = new String[5];
        Context context;
        private static android.accessibilityservice.AccessibilityService accessibilityService = null;

        @Override
        public void onCreate() {
                super.onCreate();
                Keyboardcontrol.setAccessibilityService(this,0);
                context = getApplicationContext();
                Masking[0] = PreferenceManager.getString(context, "kakaotalk");
                Masking[1] = PreferenceManager.getString(context, "mms");
                Masking[2] = PreferenceManager.getString(context, "pp");
                Masking[3] = PreferenceManager.getString(context, "word");
                Masking[4] = PreferenceManager.getString(context, "gm");
        }

         //이벤트가 발생할때마다 실행되는 부분

        @Override
        public void onAccessibilityEvent(AccessibilityEvent event) {
                String packageName = String.valueOf(event.getPackageName());//;
                Log.e("name", packageName);
                if(packageName.contains(PreferenceManager.getString(context,"camera")) || packageName.contains(PreferenceManager.getString(context,"hifirecorder"))){
                        Log.e("dfsf","d3333333333333376");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                Log.e("dfsf","11fgerdgdg");
                                Log.e("dfsf","11");
                                startForegroundService(new Intent(this, MaskingService.class));
                        } else {
                                Log.e("dfsf","11fgerdgdg");
                                Log.e("dfsf","11");

                                startService(new Intent(this, MaskingService.class));
                        }
                }
                else if(Arrays.asList(Masking).contains(packageName)){
                        PreferenceManager.setString(context, "case", "1");
                        Keyboardcontrol.setAccessibilityService(this, 1);
                } else {
                        Keyboardcontrol.setAccessibilityService(this, 0);

                        if(!packageName.contains("dlk_user")){
                                closeView();
                        }
                }
        }
        public void closeView() {
                stopService(new Intent(this, MaskingService.class));
        }
// 접근성 권한을 가지고, 연결이 되면 호출되는 함수
        public void onServiceConnected() {

                AccessibilityServiceInfo info = new AccessibilityServiceInfo();
                Log.e("name", "3252");
                info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK; // 전체 이벤트 가져오기
                info.feedbackType = AccessibilityServiceInfo.DEFAULT | AccessibilityServiceInfo.FEEDBACK_GENERIC;
                info.notificationTimeout = 100; // millisecond
                info.flags = AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
                setServiceInfo(info);
        }

        @Override
        public void onInterrupt() {
                // TODO Auto-generated method stub
                Log.e("TEST", "OnInterrupt");
                }
}
