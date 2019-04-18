package net.seismos.android.seismos.ui.home;

import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AnticipateInterpolator;
import android.view.animation.BounceInterpolator;
import android.webkit.WebView;
import android.widget.HorizontalScrollView;
import android.widget.TextView;

import net.seismos.android.seismos.R;

import java.util.Timer;
import java.util.TimerTask;


public class HomeFragment extends Fragment implements HomeContract.View {

    private static final String TAG = "HomeFragment";

    private HomeContract.Presenter mPresenter;

    private WebView web;

    SeiEarnedView seiEarnedView;

    double progressAngle;

    public HomeFragment() {
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

//        web = root.findViewById(R.id.web);
//        web.getSettings().setBuiltInZoomControls(false);
//        web.getSettings().setUseWideViewPort(true);
//        web.getSettings().setJavaScriptEnabled(true);
//        web.getSettings().setLoadWithOverviewMode(true);
//        web.setHorizontalScrollBarEnabled(false);
//        web.setVerticalScrollBarEnabled(false);
//        web.loadUrl("file:///android_asset/home_placeholder1.png");

        seiEarnedView = root.findViewById(R.id.seiEarnedView);
        HorizontalScrollView scrollView = root.findViewById(R.id.globeScrollView);
        scrollView.setHorizontalScrollBarEnabled(false);
        root.findViewById(R.id.homeSeiCount).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValueAnimator animator = ValueAnimator.ofInt(0, 1000);
                animator.setDuration(25000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        seiEarnedView.update(animation.getAnimatedFraction()*(360));
                    }
                });
                animator.setInterpolator(new BounceInterpolator());
                animator.start();

            }
        });

        final TextView seiCount = root.findViewById(R.id.homeSeiCount);

        root.findViewById(R.id.homePlayPause).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueAnimator animator = ValueAnimator.ofInt(0, 1000);
                animator.setDuration(25000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        seiEarnedView.update(animation.getAnimatedFraction()*(360));
                    }
                });
                animator.setInterpolator(new BounceInterpolator());
                animator.start();


            }
        });

        return root;
    }


    @Override
    public void showSomething() {
        // demo method that can be called by whatever has access to this through HomeContract.View

    }

    @Override
    public void setPresenter(HomeContract.Presenter presenter) {
        mPresenter = presenter;
    }
}