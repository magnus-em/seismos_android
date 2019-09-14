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
}
