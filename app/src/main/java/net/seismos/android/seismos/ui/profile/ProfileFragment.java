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

        View root = inflater.inflate(R.layout.fragment_profile, container, false);


        return root;
    }

    @Override
    public void showSomething() {

    }

    @Override
    public void setPresenter(ProfileContract.Presenter presenter) {

    }
}
