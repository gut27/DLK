package com.example.a09272;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;


public class Masking extends Service {
//앱이 종료된 후에도 항상 마스킹 기능은 켜져 있어야 해서 sevice를 상속받았다.
//마스킹디자인 정해야 함
    //이거 더 나은 코드 있는지 확인하기
    //다른 그림 위에 그리기 맨 위에 그리기
    String rect;
    private View onTopView;//항상 보이게 할 뷰
    private WindowManager manager;//스크린에 직접 뷰를 띄우기 위해 Windoe Manager사용

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    /*  Service 객체와 (화면단 Activity 사이에서)통신(데이터를 주고받을) 때 사용하는 메서드 우선, 데이터를 전달 받을 필요가 없다고 써 놓았다. return null;*/
    @Override
    public void onCreate() {
        super.onCreate();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        onTopView = inflater.inflate(R.layout.masking, null);


        //윈도우 레이아웃 파라미터 생성 및 설정 넣기 위한 설정
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.O?
                        WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY : WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,
                // Android O 이상인 경우 TYPE_APPLICATION_OVERLAY 로 설정
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|//FLAG_NOT_TOUCH_MODAL 값을 설정한다. 해당 플래그는 윈도우 영역 밖의 다른 윈도우도 이벤트를 받을 수 있도록 허용하는 플래그
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH|//앱에 외부의 모든 터치를 감시하도록 지시합니다.
                        WindowManager.LayoutParams.FLAG_SECURE,//캡처방지코드
                PixelFormat.TRANSLUCENT);//투명배경

        params.gravity = Gravity.LEFT | Gravity.BOTTOM;//창의 위치 왼쪽 하단으로

        manager = (WindowManager) getSystemService(WINDOW_SERVICE);//윈도우 매니저
        manager.addView(onTopView, params);//윈도우에 뷰를 추가 하였다.

        startForeground(1, new Notification());

    }

    //서비스 종료시 뷰 제거
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (onTopView != null) {
            manager.removeView(onTopView);
            onTopView = null;
        }
    }
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        // 여기서 처리 한다. 시스템 특성상 아래 특징 확인
//        if(intent == null){
//            return Service.START_STICKY;
//            // 끈적끈적하게 계쏙 서비스가 종료되더라도 자동으로 실행되도록한다.
//        }else{          // Null이 아닐경우
//            processCommand(intent); // 메소드를 분리하는게 보기가 좋다. 여기서 처리한다.
//        }
//
//        return super.onStartCommand(intent, flags, startId);
//
//    }
//    private void processCommand(Intent intent){
//        // 전달 받은 데이터 찍기 위함.
//        rect = intent.getStringExtra("rect"); // command 는 구분 하기 위한것
//
//    }

}
