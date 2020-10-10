package com.example.dlk_user;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class KeyboardCheckActivity extends AppCompatActivity implements KeyboardHeightObserver {

    private final static String TAG = "sample_MainActivity";
    private KeyboardHeightProvider keyboardHeightProvider;

    /**
     * {@inheritDoc}
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

        keyboardHeightProvider = new KeyboardHeightProvider(this);

        // make sure to start the keyboard height provider after the onResume
        // of this activity. This is because a popup window must be initialised
        // and attached to the activity root view.
        View view = findViewById(R.id.activitylayout);
        view.post(new Runnable() {
            public void run() {
                keyboardHeightProvider.start();
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onPause() {
        super.onPause();
        keyboardHeightProvider.setKeyboardHeightObserver(null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onResume() {
        super.onResume();
        keyboardHeightProvider.setKeyboardHeightObserver(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        keyboardHeightProvider.close();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onKeyboardHeightChanged(int height, int orientation) {
        String orientationLabel = orientation == Configuration.ORIENTATION_PORTRAIT ? "portrait" : "landscape";
        Intent intent = new Intent();
        intent.putExtra("height", height);
        setResult(RESULT_OK, intent);

        // color the keyboard height view, this will remain visible when you close the keyboard
        if (height > 0) {
            View view = findViewById(R.id.keyboard);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams)view .getLayoutParams();
            params.height = height;
            view.setLayoutParams(params);
        }
        finish();
    }
}
