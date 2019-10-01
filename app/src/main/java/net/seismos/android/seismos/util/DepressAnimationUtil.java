package net.seismos.android.seismos.util;

import android.view.MotionEvent;
import android.view.View;


public class DepressAnimationUtil {
    public static void setup(View button) {
        button.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.setAlpha(0.5f);
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    v.setAlpha(1);
                    v.performClick();
                    break;
                }
                case MotionEvent.ACTION_CANCEL:{
                    v.setAlpha(1);
                    break;
                }
            }
            return true;
        });
    }
    public static void setup(View v1, View v2, View v3) {
        setupInternal(v1, v2, v3);
        setupInternal(v2, v3, v1);
        setupInternal(v3, v1, v2);
    }

    private static void setupInternal(View v1, View v2, View v3) {
        v1.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    v.setAlpha(0.5f);
                    v2.setAlpha(0.5f);
                    v3.setAlpha(0.5f);
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    v.setAlpha(1);
                    v2.setAlpha(1);
                    v3.setAlpha(1);
                    v.performClick();
                    break;
                }
                case MotionEvent.ACTION_CANCEL:{
                    v.setAlpha(1);
                    v2.setAlpha(1);
                    v3.setAlpha(1);
                    break;
                }
            }
            return true;
        });
    }

}
