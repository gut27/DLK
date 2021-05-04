package com.dlp.dlk_user;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;

public class Keyboardcontrol {

    private static AccessibilityService accessibilityService = null;
    public static void setAccessibilityService(AccessibilityService service, Integer integer) {
        synchronized (Keyboardcontrol.class)
        {
            if (service != null && accessibilityService == null)
            {
                accessibilityService = service;
            }
        }
        if(integer == 1){
            closeKeyBoard();

        }else{
            open();
        }


    }

    public static void closeKeyBoard() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            if (accessibilityService != null)
            {
                AccessibilityService.SoftKeyboardController softKeyboardController
                        = accessibilityService.getSoftKeyboardController();
                int mode = softKeyboardController.getShowMode();
                if (mode == 0)
                {
                    softKeyboardController.setShowMode(
                            AccessibilityService.SHOW_MODE_HIDDEN);
                }
            }
        }
    }
    public static void open() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        {
            if (accessibilityService != null)
            {
                AccessibilityService.SoftKeyboardController softKeyboardController = accessibilityService.getSoftKeyboardController();
                int mode = softKeyboardController.getShowMode();
                if (mode == 1)
                {
                    softKeyboardController.setShowMode(AccessibilityService.SHOW_MODE_AUTO);
                }
            }
        }
    }






}
