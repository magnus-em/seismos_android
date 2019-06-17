package net.seismos.android.seismos.ui.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.SweepGradient;
import androidx.annotation.Nullable;
import com.google.android.material.animation.ArgbEvaluatorCompat;
import androidx.core.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;

import net.seismos.android.seismos.R;

public class SeiEarnedView extends View {

    Paint mPaintProgress1;
    Paint mPaintProgress2;
    Paint mPaintProgress3;
    Paint mPaintBackground;
    Paint mPaintDeactivated;
    float mStrokeWidth;
    int mStrokeGap;

    float mSweepAngle1;
    float mSweepAngle2;
    float mSweepAngle3;


    boolean ring2Activated = false;
    boolean ring3Activated = false;

    RectF rectRing3;
    RectF lock3;
    RectF rectRing2;
    RectF lock2;
    RectF rectRing1;
    int accentBlue = ContextCompat.getColor(getContext(), R.color.accentBlue);
    int accentGreen = ContextCompat.getColor(getContext(), R.color.accentGreen);

    int[] colors = {accentBlue, accentBlue, accentGreen}; // issue?

    Bitmap lock;





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

        rectRing1 = new RectF();
        rectRing2 = new RectF();
        lock2 = new RectF();
        rectRing3 = new RectF();
        lock3 = new RectF();

        lock = BitmapFactory.decodeResource(getResources(), R.drawable.ringlock);


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
        mPaintBackground.setColor(ContextCompat.getColor(getContext(), R.color.blueLight));

        mPaintDeactivated = new Paint();
        mPaintDeactivated.setAntiAlias(true);
        mPaintDeactivated.setStyle(Paint.Style.STROKE);
        mPaintDeactivated.setColor(ContextCompat.getColor(getContext(), R.color.blueDark));



    }


    public void updateFirstRing(double angle) {
        mSweepAngle1 = (float)angle;
        invalidate();
    }

    public void updateSecondRing(double angle) {
        if (ring2Activated) {
            mSweepAngle2 = (float)angle;
            invalidate();
        }
    }

    public void updateThirdRing(double angle) {
        if (ring3Activated) {
            mSweepAngle3 = (float)angle;
            invalidate();
        }
    }

    public void setRing2Activated(boolean active) {
        ring2Activated = active;
        invalidate();
    }

    public void setRing3Activated(boolean active) {
        ring3Activated = active;
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

        mStrokeWidth = (int) (getHeight() * 0.088);

        mPaintProgress1.setStrokeWidth(mStrokeWidth);
        mPaintProgress2.setStrokeWidth(mStrokeWidth);
        mPaintProgress3.setStrokeWidth(mStrokeWidth);

        mPaintBackground.setStrokeWidth(mStrokeWidth);
        mPaintDeactivated.setStrokeWidth(mStrokeWidth);
        mStrokeGap = (int)(mStrokeWidth*0.08);

        rectRing3.left = mStrokeWidth/2f;
        rectRing3.right = getWidth() - mStrokeWidth/2f;
        rectRing3.top = mStrokeWidth/2f;
        rectRing3.bottom = getHeight() - mStrokeWidth/2f;

        lock3.left = getWidth()/2f - mStrokeWidth/2f + mStrokeWidth/10;
        lock3.right = getWidth()/2f + mStrokeWidth/2f - mStrokeWidth/10;
        lock3.top = mStrokeWidth/10;
        lock3.bottom = mStrokeWidth - mStrokeWidth/10;

        rectRing2.left = rectRing3.left + mStrokeWidth + mStrokeGap;
        rectRing2.right = rectRing3.right - (mStrokeWidth + mStrokeGap);
        rectRing2.top = rectRing3.top + mStrokeWidth + mStrokeGap;
        rectRing2.bottom = rectRing3.bottom - (mStrokeWidth + mStrokeGap);

        lock2.left = getWidth()/2f - mStrokeWidth/2f + mStrokeWidth/10;
        lock2.right = getWidth()/2f + mStrokeWidth/2f - mStrokeWidth/10;
        lock2.top = lock3.top + mStrokeWidth + mStrokeGap;
        lock2.bottom = mStrokeWidth - mStrokeWidth/10 + (mStrokeWidth + mStrokeGap);

        rectRing1.left = rectRing2.left + mStrokeWidth + mStrokeGap;
        rectRing1.right = rectRing2.right - (mStrokeWidth + mStrokeGap);
        rectRing1.top = rectRing2.top + mStrokeWidth + mStrokeGap;
        rectRing1.bottom = rectRing2.bottom - (mStrokeWidth + mStrokeGap);



        if (ring3Activated) {
            mPaintProgress3.setShader(getSweepGradient(mSweepAngle3));
            canvas.drawArc(rectRing3, 0, 360, false,  mPaintBackground);
            canvas.drawArc(rectRing3, 270, mSweepAngle3+1, false, mPaintProgress3);
        } else {
            canvas.drawArc(rectRing3, 0, 360, false,  mPaintDeactivated);
            canvas.drawBitmap(lock, null, lock3, null);
        }

        if (ring2Activated) {
            mPaintProgress2.setShader(getSweepGradient(mSweepAngle2));
            canvas.drawArc(rectRing2, 0, 360, false, mPaintBackground);
            canvas.drawArc(rectRing2, 270, mSweepAngle2+1, false, mPaintProgress2);
        } else {
            canvas.drawArc(rectRing2, 0, 360, false, mPaintDeactivated);
            canvas.drawBitmap(lock, null, lock2, null);
        }

        mPaintProgress1.setShader(getSweepGradient(mSweepAngle1));
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
