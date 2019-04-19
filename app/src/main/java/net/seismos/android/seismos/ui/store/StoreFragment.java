package net.seismos.android.seismos.ui.store;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import net.seismos.android.seismos.R;

public class StoreFragment extends Fragment implements StoreContract.View {

    private WebView web;

    public StoreFragment() { // required empty public constructor
        }

    public static StoreFragment newInstance() {
        return new StoreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_store, container, false);

//        web = root.findViewById(R.id.web);
//        web.getSettings().setBuiltInZoomControls(false);
//        web.getSettings().setUseWideViewPort(true);
//        web.getSettings().setJavaScriptEnabled(true);
//        web.getSettings().setLoadWithOverviewMode(true);
//        web.setHorizontalScrollBarEnabled(false);
//        web.setVerticalScrollBarEnabled(false);
//        web.loadUrl("file:///android_asset/store_placeholder.png");

        return root;
    }

    @Override
    public void showSomething() {

    }

    @Override
    public void setPresenter(StoreContract.Presenter presenter) {

    }
}
