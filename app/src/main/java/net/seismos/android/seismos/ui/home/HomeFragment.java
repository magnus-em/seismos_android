package net.seismos.android.seismos.ui.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import net.seismos.android.seismos.R;



public class HomeFragment extends Fragment implements HomeContract.View {

    private static final String TAG = "HomeFragment";

    private HomeContract.Presenter mPresenter;

    private WebView web;

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
        View root = inflater.inflate(R.layout.fragment_placeholder, container, false);

        web = root.findViewById(R.id.web);
        web.getSettings().setBuiltInZoomControls(false);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.setHorizontalScrollBarEnabled(false);
        web.setVerticalScrollBarEnabled(false);
        web.loadUrl("file:///android_asset/home_placeholder1.png");


        //webView.loadDataWithBaseURL("file:///android_asset/home_placeholder.png", "<style>img{display: inline;height: auto;max-width: 100%;}</style>" , "text/html", "UTF-8", null);

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