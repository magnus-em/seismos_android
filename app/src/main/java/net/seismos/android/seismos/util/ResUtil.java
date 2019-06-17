package net.seismos.android.seismos.util;


import android.content.Context;
import android.graphics.drawable.Drawable;
import androidx.annotation.DrawableRes;
import androidx.annotation.StringRes;
import android.util.DisplayMetrics;

public class ResUtil {
    private static ResUtil mInstance;

    private Context mContext;

    private ResUtil(Context context) {
        mContext = context;
    }

    public static void init(Context context) {
        mInstance = new ResUtil(context.getApplicationContext());
    }

    public static ResUtil getInstance() {
        return mInstance;
    }

    public String getString(@StringRes int id) {
        return mContext.getString(id);
    }

    public Drawable getDrawable(@DrawableRes int id) {
        return mContext.getDrawable(id);
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = mContext.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }
}