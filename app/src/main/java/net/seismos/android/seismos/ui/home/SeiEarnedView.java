package net.seismos.android.seismos.ui.home;

import android.animation.ArgbEvaluator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.support.design.animation.ArgbEvaluatorCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;

import net.seismos.android.seismos.R;

public class SeiEarnedView extends View {

    float mSweepAngle;
    int mStartAngle;

    Paint mPaintProgress1;
    Paint mPaintProgress2;
    Paint mPaintProgress3;


    Paint mPaintProgress;
    Paint mPaintBackground;
    float mStrokeWidth;
    int mStrokeGap;

    float mSweepAngle1;
    float mSweepAngle2;
    float mSweepAngle3;

    RectF rectRing3;
    RectF rectRing2;
    RectF rectRing1;
    int accentBlue = ContextCompat.getColor(getContext(), R.color.accentBlue);
    int accentGreen = ContextCompat.getColor(getContext(), R.color.accentGreen);

    int[] colors = {accentBlue, accentBlue, accentGreen,};


    Paint mPaint;
    RectF mRect;
    int mSquareColor;
    int mPadding = 0;


    public SeiEarnedView(Context context) {
        super(context);
        init(null);
    }

    public SeiEarnedView(Context context,  @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SeiEarnedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    public SeiEarnedView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet set) {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mRect = new RectF();
        rectRing1 = new RectF();
        rectRing2 = new RectF();
        rectRing3 = new RectF();



        mSweepAngle = 0;
        mStartAngle = 270;
        mPaintProgress = new Paint();
        mPaintProgress.setAntiAlias(true);
        mPaintProgress.setStyle(Paint.Style.STROKE);
        mPaintProgress.setStrokeCap(Paint.Cap.ROUND);

        mPaintProgress1 = new Paint();
        mPaintProgress1.setAntiAlias(true);
        mPaintProgress1.setStyle(Paint.Style.STROKE);
        mPaintProgress1.setStrokeCap(Paint.Cap.ROUND);
        mPaintProgress2 = new Paint();
        mPaintProgress2.setAntiAlias(true);
        mPaintProgress2.setStyle(Paint.Style.STROKE);
        mPaintProgress2.setStrokeCap(Paint.Cap.ROUND);
        mPaintProgress3 = new Paint();
        mPaintProgress3.setAntiAlias(true);
        mPaintProgress3.setStyle(Paint.Style.STROKE);
        mPaintProgress3.setStrokeCap(Paint.Cap.ROUND);



        mPaintBackground = new Paint();
        mPaintBackground.setAntiAlias(true);
        mPaintBackground.setStyle(Paint.Style.STROKE);
        mPaintBackground.setColor(ContextCompat.getColor(getContext(), R.color.blueDark));

        if (set == null) {
            return;
        }

        TypedArray ta = getContext().obtainStyledAttributes(set, R.styleable.SeiEarnedView);
        mSquareColor = ta.getColor(R.styleable.SeiEarnedView_square_color, Color.GREEN);
        mPaint.setColor(mSquareColor);
        ta.recycle();


    }

    public void update(double angle) {
        mSweepAngle = (float)angle;

        invalidate();
    }

    public void updateFirstRing(double angle) {
        mSweepAngle1 = (float)angle;
        invalidate();
    }

    public void updateSecondRing(double angle) {
        mSweepAngle2 = (float)angle;
        invalidate();
    }

    public void updateThirdRing(double angle) {
        mSweepAngle3 = (float)angle;
        invalidate();
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int size = width > height ? height : width;
        setMeasuredDimension(size, size);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mStrokeWidth = (int) (getHeight() * 0.095);
        mPaintProgress.setStrokeWidth(mStrokeWidth);
        mPaintProgress1.setStrokeWidth(mStrokeWidth);
        mPaintProgress2.setStrokeWidth(mStrokeWidth);
        mPaintProgress3.setStrokeWidth(mStrokeWidth);

        mPaintBackground.setStrokeWidth(mStrokeWidth);
        mStrokeGap = (int)(mStrokeWidth*0.08);

        rectRing3.left = mStrokeWidth/2f;
        rectRing3.right = getWidth() - mStrokeWidth/2f;
        rectRing3.top = mStrokeWidth/2f;
        rectRing3.bottom = getHeight() - mStrokeWidth/2f;

        rectRing2.left = rectRing3.left + mStrokeWidth + mStrokeGap;
        rectRing2.right = rectRing3.right - (mStrokeWidth + mStrokeGap);
        rectRing2.top = rectRing3.top + mStrokeWidth + mStrokeGap;
        rectRing2.bottom = rectRing3.bottom - (mStrokeWidth + mStrokeGap);

        rectRing1.left = rectRing2.left + mStrokeWidth + mStrokeGap;
        rectRing1.right = rectRing2.right - (mStrokeWidth + mStrokeGap);
        rectRing1.top = rectRing2.top + mStrokeWidth + mStrokeGap;
        rectRing1.bottom = rectRing2.bottom - (mStrokeWidth + mStrokeGap);

//        float[] positions = {0, 20/360f, 1};
//        float[] positions2 = {0, 1};
//        int[] colors2 = {accentBlue, accentGreen};
//
//        SweepGradient progressGradient;
//
//        if (mSweepAngle < 31) {
//             positions[2] = 60/360f;
//        } else {
//            positions[2] = (mSweepAngle+20)/360f;
//        }
//        if (mSweepAngle>300 && mSweepAngle < 330) {
//
//
//            int color = ArgbEvaluatorCompat
//                    .getInstance()
//                    .evaluate((-(300-mSweepAngle)/30), accentBlue, accentGreen);
//
//            colors2[0] = color;
//            progressGradient = new SweepGradient(getWidth()/2f, getHeight()/2f,
//                    colors2, positions2);
//
//        } else if (mSweepAngle>=330) {
//            progressGradient = null;
//            mPaintProgress.setColor(accentGreen);
//        } else {
//            progressGradient = new SweepGradient(getWidth()/2f, getHeight()/2f,
//                    colors, positions);
//        }
//
//        if (!(mSweepAngle>=330)) {
//            Matrix gradientMatrix = new Matrix();
//            gradientMatrix.preRotate(250, getWidth()/2f,getHeight()/2f);
//            progressGradient.setLocalMatrix(gradientMatrix);
//        }

        mPaintProgress.setShader(getSweepGradient(mSweepAngle));
        mPaintProgress1.setShader(getSweepGradient(mSweepAngle1));
        mPaintProgress2.setShader(getSweepGradient(mSweepAngle2));
        mPaintProgress3.setShader(getSweepGradient(mSweepAngle3));


        canvas.drawArc(rectRing3, 0, 360, false,  mPaintBackground);
        canvas.drawArc(rectRing3, 270, mSweepAngle3+1, false, mPaintProgress3);
        canvas.drawArc(rectRing2, 0, 360, false, mPaintBackground);
        canvas.drawArc(rectRing2, 270, mSweepAngle2+1, false, mPaintProgress2);
        canvas.drawArc(rectRing1, 0, 360, false, mPaintBackground);
        canvas.drawArc(rectRing1, 270, mSweepAngle1+1, false, mPaintProgress1);
    }

    private SweepGradient getSweepGradient(float mSweepAngle) {
        SweepGradient progressGradient;
        float[] positions = {0, 20/360f, 1};
        float[] positions2 = {0, 1};
        int[] colors2 = {accentBlue, accentGreen};

        if (mSweepAngle < 31) {
            positions[2] = 60/360f;
        } else {
            positions[2] = (mSweepAngle+20)/360f;
        }
        if (mSweepAngle>300 && mSweepAngle < 330) {


            int color = ArgbEvaluatorCompat
                    .getInstance()
                    .evaluate((-(300-mSweepAngle)/30), accentBlue, accentGreen);

            colors2[0] = color;
            progressGradient = new SweepGradient(getWidth()/2f, getHeight()/2f,
                    colors2, positions2);

        } else if (mSweepAngle>=330) {
            progressGradient = new SweepGradient(getWidth()/2f, getHeight()/2f,
                    accentGreen, accentGreen);
        } else {
            progressGradient = new SweepGradient(getWidth()/2f, getHeight()/2f,
                    colors, positions);
        }

        if (!(mSweepAngle>=330)) {
            Matrix gradientMatrix = new Matrix();
            gradientMatrix.preRotate(250, getWidth()/2f,getHeight()/2f);
            progressGradient.setLocalMatrix(gradientMatrix);
        }

        return progressGradient;
    }
}
