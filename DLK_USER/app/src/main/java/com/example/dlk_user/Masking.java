package com.example.dlk_user;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;


public class Masking extends Service {
//앱이 종료된 후에도 항상 마스킹 기능은 켜져 있어야 해서 sevice를 상속받았다.
//마스킹디자인 정해야 함
    //이거 더 나은 코드 있는지 확인하기
    //다른 그림 위에 그리기 맨 위에 그리기

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
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_SYSTEM_ALERT,//최상위에 노출되게 해준다.
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL|//FLAG_NOT_TOUCH_MODAL 값을 설정한다. 해당 플래그는 윈도우 영역 밖의 다른 윈도우도 이벤트를 받을 수 있도록 허용하는 플래그
                        WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH|//앱에 외부의 모든 터치를 감시하도록 지시합니다.
                        WindowManager.LayoutParams.FLAG_SECURE,//캡처방지코드
                PixelFormat.TRANSLUCENT);//투명배경

        //params.softInputMode = WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_IS_FORWARD_NAVIGATION;

        params.gravity = Gravity.LEFT | Gravity.BOTTOM;//창의 위치 왼쪽 하단으로

      /*  //화면 높이의 차이로 키패드 동작인지 dp()를 사용하여 값을 구해낸다.
        onTopView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                int viewheight = onTopView.getRootView().getHeight();
                int wrapperheight = onTopView.getHeight();
                int diff =viewheight - wrapperheight;
                if(diff>dp(50)){
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 700);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(400, 700);
                    onTopView.setLayoutParams(lp);

                }
            }
        });
*/
        manager = (WindowManager) getSystemService(WINDOW_SERVICE);//윈도우 매니저
        manager.addView(onTopView, params);//윈도우에 뷰를 추가 하였다.

        startForeground(1, new Notification());
        /*마스킹이 지속적으로 실행되어야 하므로 서비스가 죽지 않게 하기 위해 onstartCommand()함수에 startForeground 함수를 작성한 것입니다. */


//클로즈 버튼 클릭시 마스킹 기능 종료
     /*   LayoutInflater inflater2 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        close = inflater2.inflate(R.layout.activity_main, null);
        ImageButton closeBtn = close.findViewById(R.id.end_the_masking);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                manager.removeView(onTopView);
                onTopView = null;
                stopSelf();
            }
        });*/
    }
    /*public float dp(float valueInDp){
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);

    }*/

    //서비스 종료시 뷰 제거
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (onTopView != null) {
            manager.removeView(onTopView);
            onTopView = null;
        }
    }

}
