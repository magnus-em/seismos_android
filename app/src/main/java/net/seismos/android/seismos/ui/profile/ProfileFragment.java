package net.seismos.android.seismos.ui.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import net.seismos.android.seismos.R;

public class ProfileFragment extends Fragment implements ProfileContract.View {

    public ProfileFragment() {}

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_placeholder, container, false);

        WebView web = root.findViewById(R.id.web);

        web = root.findViewById(R.id.web);
        web.getSettings().setBuiltInZoomControls(false);
        web.getSettings().setUseWideViewPort(true);
        web.getSettings().setJavaScriptEnabled(true);
        web.getSettings().setLoadWithOverviewMode(true);
        web.setHorizontalScrollBarEnabled(false);
        web.setVerticalScrollBarEnabled(false);
        web.loadUrl("file:///android_asset/profile_placeholder.png");

        return root;
    }

    @Override
    public void showSomething() {

    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {

    }
}
