package com.dlp.dlk_user;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.Arrays;

public class AccessibilityService extends android.accessibilityservice.AccessibilityService {
        // !--드롭이나 마스킹 하고싶은 앱이 있을경우 뒤에 패키지명 만 추가하면 원하는 기능 수행 가능
        String[] K_bloock = new String[4];
        Context context;
        private static android.accessibilityservice.AccessibilityService accessibilityService = null;

        @Override
        public void onCreate() {
                super.onCreate();
                Keyboardcontrol.setAccessibilityService(this,0);
                context = getApplicationContext();

                K_bloock[0] = PreferenceManager.getString(context, "kakaotalk");
                K_bloock[1] = PreferenceManager.getString(context, "chrome");
                K_bloock[2] = PreferenceManager.getString(context, "mms");
                K_bloock[3] = PreferenceManager.getString(context, "gm");
        }

         //이벤트가 발생할때마다 실행되는 부분

        @Override
        public void onAccessibilityEvent(AccessibilityEvent event) {
                if (!Settings.canDrawOverlays(this)){
                        Intent change = new Intent(this, MainActivity.class);
                        change.putExtra("acc_result", "true");
                }

                String packageName = String.valueOf(event.getPackageName());//;
                packageName.toLowerCase();
                Log.e("TAG", "Catch Event Package Name : " + event.getPackageName());
//                Log.e("TAG", "Catch Event TEXT : " + event.getText());
//                Log.e("TAG", "Catch Event ContentDescription : " + event.getContentDescription());
//                Log.e("TAG", "Catch Event getSource : " + event.getSource());
//                Log.e("TAG", "=========================================================================");
//

                if(packageName.contains(PreferenceManager.getString(context,"camera")) || packageName.contains(PreferenceManager.getString(context,"hifirecorder"))){
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                startForegroundService(new Intent(this, MaskingService.class));
                        } else {
                                startService(new Intent(this, MaskingService.class));
                        }
                }
                else if(Arrays.asList(K_bloock).contains(packageName)){
                        PreferenceManager.setString(context, "case", "1");
                        Keyboardcontrol.setAccessibilityService(this, 1);
                } else {
                        Keyboardcontrol.setAccessibilityService(this, 0);
                        if(packageName.contains("dlk_user") || packageName.contains("com.samsung.android.app.cocktailbarservice")|| packageName.contains("com.android.systemui")){
                                Log.e("TAG2", "ssssssss");
                        }else{
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
                info.eventTypes = AccessibilityEvent.TYPES_ALL_MASK; // 전체 이벤트 가져오기
                info.feedbackType = AccessibilityServiceInfo.DEFAULT | AccessibilityServiceInfo.FEEDBACK_GENERIC;
                info.notificationTimeout = 100; // millisecond
                info.flags = AccessibilityServiceInfo.FLAG_REQUEST_FILTER_KEY_EVENTS;
                setServiceInfo(info);
        }

        @Override
        public void onSystemActionsChanged() {
                super.onSystemActionsChanged();
        }

        @Override
        public void onTaskRemoved(Intent rootIntent) { //핸들링 하는 부분
                Toast.makeText(this, "onTaskRemoved ", Toast.LENGTH_SHORT).show();
                stopSelf(); //서비스 종료
        }

        @Override
        public void onInterrupt() {
                // TODO Auto-generated method stub
                Log.e("TEST", "OnInterrupt");
                }

        @Override
        public boolean onUnbind(Intent intent) {
                Intent onunbind = new Intent(this, MainActivity.class);
                onunbind.putExtra("onunbind", "onunbind");
                startActivity(onunbind);
                return super.onUnbind(intent);

        }


}
