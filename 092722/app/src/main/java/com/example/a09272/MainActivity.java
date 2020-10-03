package com.example.a09272;

import androidx.appcompat.app.AppCompatActivity;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.accessibility.AccessibilityManager;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int REQ_CODE_OVERLAY_PERMISSION = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Rect(0, 0 - 160, 160); boundsInScreen: Rect(880, 2610 - 1040, 2770)
        // 접근성 권한이 없으면 접근성 권한 설정하는 다이얼로그 띄워주는 부분
        if(!checkAccessibilityPermissions()) {
            setAccessibilityPermissions();
        }

        if (Settings.canDrawOverlays(this))//Overlay사용가능여부 체크
            {}
        else
            onObtainingPermissionOverlayWindow();//다른 앱위에 그리기 권한 획득 창이 나타나는 함수를 부른다.
    }





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
        gsDialog.setMessage("접근성 권한을 필요로 합니다");
        gsDialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // 설정화면으로 보내는 부분
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
                return;
            }
        }).create().show();
    }
}