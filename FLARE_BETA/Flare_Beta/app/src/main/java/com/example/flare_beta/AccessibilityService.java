package com.example.flare_beta;

import android.accessibilityservice.AccessibilityServiceInfo;
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
        Integer count[] = {0,0,0,0,0};
        private static android.accessibilityservice.AccessibilityService accessibilityService = null;
        Integer count1 = 0;
        Integer count2 = 0;
        @Override
        public void onCreate() {
                super.onCreate();
                Keyboardcontrol.setAccessibilityService(this,0);
                context = getApplicationContext();
                Masking[0] = "camera";
                Masking[1] = "hifirecorder";
                Masking[2] = "com.kakao";
                Masking[3] = "";
                Masking[4] = "";
        }

         //이벤트가 발생할때마다 실행되는 부분

        @Override
        public void onAccessibilityEvent(AccessibilityEvent event) {

                String packageName = String.valueOf(event.getPackageName());//;
               Log.e("TAG", "Catch Event getSource : " + event.getSource());
                Log.e("TAG", "Catch Event getSource : " + packageName);

                Log.e("TAG", "=========================================================================");


                if(packageName.contains(Masking[0])){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startForegroundService(new Intent(this, MaskingService.class));
                        } else {
                                startService(new Intent(this, MaskingService.class));
                        }
                        if (count1 == 0){
                                count[0]++;
                                PreferenceManager.setString(context, "Camera", String.valueOf(count[0]));
                        }
                        count1 ++;

                }
                else if(packageName.contains(Masking[1])){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startForegroundService(new Intent(this, MaskingService.class));
                        } else {
                                startService(new Intent(this, MaskingService.class));
                        }
                        if (count1 == 0){
                                count[1]++;
                                PreferenceManager.setString(context, "Record", String.valueOf(count[0]));
                        }
                        count1 ++;

                }
               // else if(Arrays.asList(Masking).contains(packageName)){
                else if(packageName.contains(Masking[2])){
                        PreferenceManager.setString(context, "case", "1");
                        Keyboardcontrol.setAccessibilityService(this, 1);
                        Log.d("000_001","dhglsdjfds");
                } else {
                        Keyboardcontrol.setAccessibilityService(this, 0);

                        if(!packageName.contains("flare_beta")&&!packageName.contains("dlk_user")){
                                closeView();
                        }else{
                                count1 = 0;
                        }
                }
        }
        public void closeView() {
                stopService(new Intent(this, MaskingService.class));
        }
// 접근성 권한을 가지고, 연결이 되면 호출되는 함수
        public void onServiceConnected() {

                AccessibilityServiceInfo info = new AccessibilityServiceInfo();
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
