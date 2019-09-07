package net.seismos.android.seismos.ui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Earthquake;
import net.seismos.android.seismos.util.ResUtil;

public class EqGlobeView extends View {
    private static final String TAG = "EqGlobeView";


    private float mStrokeWidth = 10;
    private int mGap;

    private RectF rectF;
    private Paint mRingPaint;
    LinearGradient gradient;

    private Drawable mMapDrawable;
    private Earthquake mEarthquake;

    private int colorStart = ContextCompat.getColor(getContext(), R.color.eq8GradientStart);
    private int colorEnd = ContextCompat.getColor(getContext(), R.color.eq8GradientEnd);


    public EqGlobeView(Context context) {
        super(context);
        init(null);
    }

    public EqGlobeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public EqGlobeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public EqGlobeView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {

        mStrokeWidth = ResUtil.getInstance().dpToPx(3);

        mGap = ResUtil.getInstance().dpToPx(6);

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);
        rectF = new RectF();

        rectF.left = mStrokeWidth/2f;
        rectF.right = getMeasuredWidth() - mStrokeWidth/2f;
        rectF.top = mStrokeWidth/2f;
        rectF.bottom = getMeasuredHeight() - mStrokeWidth/2f;

        mMapDrawable = getResources().getDrawable(R.drawable.japanglobe);

        gradient = new LinearGradient(
                rectF.left + mStrokeWidth,
                rectF.bottom - mStrokeWidth,
                rectF.right - mStrokeWidth,
                rectF.top + mStrokeWidth, colorStart, colorEnd, Shader.TileMode.CLAMP);


//        if (mEarthquake != null) {
//            createGradient();
//            createDrawable();
//        }


    }

    public void setDrawable(Drawable d) {
        mMapDrawable = d;
    }

    public void setEarthquake(Earthquake earthquake) {
        mEarthquake = earthquake;
        createFilename();
        createGradient();
        invalidate();
    }

    private void createGradient() {
        double magnitude = mEarthquake.getMagnitude();
        if (magnitude < 5) {
            colorStart = ContextCompat.getColor(getContext(), R.color.eq49GradientStart);
            colorEnd = ContextCompat.getColor(getContext(), R.color.eq49GradientEnd);
        } else if (magnitude < 6) {
            colorStart = ContextCompat.getColor(getContext(), R.color.eq59GradientStart);
            colorEnd = ContextCompat.getColor(getContext(), R.color.eq59GradientEnd);
        } else if (magnitude < 6.5) {
            colorStart = ContextCompat.getColor(getContext(), R.color.eq64GradientStart);
            colorEnd = ContextCompat.getColor(getContext(), R.color.eq64GradientEnd);
        } else if (magnitude < 7) {
            colorStart = ContextCompat.getColor(getContext(), R.color.eq69GradientStart);
            colorEnd = ContextCompat.getColor(getContext(), R.color.eq69GradientEnd);
        } else if (magnitude < 7.5) {
            colorStart = ContextCompat.getColor(getContext(), R.color.eq74GradientStart);
            colorEnd = ContextCompat.getColor(getContext(), R.color.eq74GradientEnd);
        } else if (magnitude < 8) {
            colorStart = ContextCompat.getColor(getContext(), R.color.eq79GradientStart);
            colorEnd = ContextCompat.getColor(getContext(), R.color.eq79GradientEnd);
        } else {
            colorStart = ContextCompat.getColor(getContext(), R.color.eq8GradientStart);
            colorEnd = ContextCompat.getColor(getContext(), R.color.eq8GradientEnd);
        }

        gradient = new LinearGradient(
                rectF.left + mStrokeWidth,
                rectF.bottom - mStrokeWidth,
                rectF.right - mStrokeWidth,
                rectF.top + mStrokeWidth, colorStart, colorEnd, Shader.TileMode.CLAMP);

    }

    private void createDrawable() {
        LatLng position = new LatLng(mEarthquake.getLatitude(), mEarthquake.getLongitude());
        if ((int)(position.latitude)%4 == 0) {
            mMapDrawable = getResources().getDrawable(R.drawable.peruglobe);
        } else if  ((int)(position.latitude)%3 == 0){
            mMapDrawable = getResources().getDrawable(R.drawable.usaglobe);
        } else if ((int)(position.latitude)%2 == 0) {
            mMapDrawable = getResources().getDrawable(R.drawable.japanglobe);
        } else {
            mMapDrawable = getResources().getDrawable(R.drawable.peruglobe);
        }






    }

    private void createFilename() {
        LatLng position = new LatLng(mEarthquake.getLatitude(), mEarthquake.getLongitude());
        double lat = position.latitude;
        double lon = position.longitude;
        boolean negLat = false;
        boolean negLon = false;
        boolean latIsNeg = false;
        boolean lonIsNeg = false;



        if (lon >-20 && lon <= 0) {
            negLon = true;
        }

        if (lat < 0) {
            negLat = true;
        }
        if (lon < 0) {
            negLon = true;
        }


        if (lon <= -20) {
            lonIsNeg = true;
        }



        String lonString = "";

        int lonInt = (int)(lon - (lon%20));



        if (negLon) {
            lonString += "_";
        }



        if (lonIsNeg) {
            lonString += Integer.toString(-lonInt);
        } else {
            lonString += Integer.toString(lonInt);
        }

        String string = "";

        if (lat < 90 && lat >= 80) {
            string = "80";
        } else if (lat < 80 && lat >= 70) {
            string = "70";
        } else if (lat < 70 && lat >= 60) {
            string = "60";
        } else if (lat < 60 && lat >= 50) {
            string = "50";
        } else if (lat < 50 && lat >= 40) {
            string = "40";
        } else if (lat < 40 && lat >= 20) {
            string = "20";
        } else if (lat < 20 && lat >= 0) {
            string = "0";
        } else if (lat > -20) {
            string = "_20";
        } else if (lat > -40) {
            string = "_40";
        } else if (lat > -50) {
            string = "_50";
        } else if (lat > -60) {
            string = "_60";
        } else if (lat > -70) {
            string = "_70";
        } else if (lat > -80) {
            string = "_80";
        } else if (lat > -90) {
            string = "_90";
        }



        String filename = "g" + lonString + "d" + string;
        Log.d(TAG, "filename: " + filename);
        int mapID = getResources().getIdentifier("net.seismos.android.seismos:drawable/" + filename,
                                                    null, null);
        int altId = getResources().getIdentifier("net.seismos.android.seismos:drawable/g_60d60",
                null, null);

        mMapDrawable = getResources().getDrawable(mapID);
    }

    public void setDrawable(int i) {
        if ((int)(i)%3 == 0) {
            mMapDrawable = getResources().getDrawable(R.drawable.peruglobe);
        } else if  ((int)(i)%2 == 0){
            mMapDrawable = getResources().getDrawable(R.drawable.usaglobe);
        } else {
            mMapDrawable = getResources().getDrawable(R.drawable.japanglobe);
        }
    }



    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, width);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        mRingPaint.setShader(gradient);

        rectF.left = mStrokeWidth/2f;
        rectF.right = getWidth() - mStrokeWidth/2f;
        rectF.top = mStrokeWidth/2f;
        rectF.bottom = getHeight() - mStrokeWidth/2f;






        mMapDrawable.setBounds((int)rectF.left + mGap, (int)rectF.top + mGap , (int)rectF.right - mGap, (int)rectF.bottom - mGap);

        mMapDrawable.draw(canvas);

        canvas.drawArc(rectF, 0, 360, false, mRingPaint);


        //canvas.drawText("M6.2 USA", getWidth()/2, getHeight() - textHeight , textPaint);
    }
}
