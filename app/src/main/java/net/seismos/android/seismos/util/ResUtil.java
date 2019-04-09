package net.seismos.android.seismos.util;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

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
}