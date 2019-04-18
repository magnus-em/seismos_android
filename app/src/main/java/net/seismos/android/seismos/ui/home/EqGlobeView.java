package net.seismos.android.seismos.ui.home;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import net.seismos.android.seismos.R;

public class EqGlobeView extends View {

    private float mSweepAngle = 360;
    private float mStartAngle = 0;
    private float mStrokeWidth = 13;

    private RectF rectF;
    private Paint mRingPaint;
    private Paint textPaint;

    private int textHeight;

    private int colorStart = ContextCompat.getColor(getContext(), R.color.eq8Start);
    private int colorEnd = ContextCompat.getColor(getContext(), R.color.eq8End);


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
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);
        rectF = new RectF();

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.WHITE);
        textPaint.setSubpixelText(true);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(40);

        textHeight = (int)textPaint.measureText("yY");


    }




    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);



        setMeasuredDimension(width, width);

//        mStrokeWidth = (int)(getWidth() * 0.05);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);



        rectF.left = mStrokeWidth/2f;
        rectF.right = getWidth() - mStrokeWidth/2f;
        rectF.top = mStrokeWidth/2f;
        rectF.bottom = getWidth() - mStrokeWidth/2f;

        float radius = getHeight()/2f;
        float x = (float)(radius*Math.cos(45));
        float y = (float)(radius*Math.sin(45));


        LinearGradient gradient = new LinearGradient(
                rectF.left + mStrokeWidth,
                rectF.bottom - mStrokeWidth,
                rectF.right - mStrokeWidth,
                rectF.top + mStrokeWidth, colorStart, colorEnd, Shader.TileMode.CLAMP);

        mRingPaint.setShader(gradient);

        Drawable d = getResources().getDrawable(R.drawable.japanglobe);
        d.setBounds((int)rectF.left + 20, (int)rectF.top + 20 , (int)rectF.right - 20, (int)rectF.bottom - 20);

        d.draw(canvas);

        canvas.drawArc(rectF, mStartAngle, mSweepAngle, false, mRingPaint);


        //canvas.drawText("M6.2 USA", getWidth()/2, getHeight() - textHeight , textPaint);
    }
}
