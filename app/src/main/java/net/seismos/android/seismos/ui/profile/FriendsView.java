package net.seismos.android.seismos.ui.profile;

import android.annotation.SuppressLint;
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
import android.view.View;

import net.seismos.android.seismos.R;

public class FriendsView extends View {


    private float mStrokeWidth = 10;
    private int mGap;

    private RectF rectF;
    private Paint mRingPaint;
    LinearGradient gradient;

    private Drawable mProfileDrawable;


    private int colorStart = ContextCompat.getColor(getContext(), R.color.gradientDangerStart);
    private int colorEnd = ContextCompat.getColor(getContext(), R.color.gradientDangerEnd);


    public FriendsView(Context context) {
        super(context);
        init(null);
    }

    public FriendsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public FriendsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public FriendsView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        mGap = (int)mStrokeWidth;

        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setStrokeWidth(mStrokeWidth);
        rectF = new RectF();


        mProfileDrawable = getResources().getDrawable(R.drawable.japanglobe);



    }

    public void setDrawable(Drawable d) {
        mProfileDrawable = d;
        invalidate();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, width);



    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        rectF.left = mStrokeWidth/2f;
        rectF.right = getWidth() - mStrokeWidth/2f;
        rectF.top = mStrokeWidth/2f;
        rectF.bottom = getHeight() - mStrokeWidth/2f;

        gradient = new LinearGradient(
                rectF.left + mStrokeWidth,
                rectF.bottom - mStrokeWidth,
                rectF.right - mStrokeWidth,
                rectF.top + mStrokeWidth, colorStart, colorEnd, Shader.TileMode.CLAMP);

        mRingPaint.setShader(gradient);


        mProfileDrawable.setBounds((int)rectF.left + mGap, (int)rectF.top + mGap , (int)rectF.right - mGap, (int)rectF.bottom - mGap);
        mProfileDrawable.draw(canvas);
        canvas.drawArc(rectF, 0, 360, false, mRingPaint);
    }
}
