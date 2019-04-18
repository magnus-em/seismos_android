package net.seismos.android.seismos.ui.seismos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import net.seismos.android.seismos.R;

public class SeismosFragment extends Fragment implements SeismosContract.View {

    private SeismosContract.Presenter mPresenter;

    public SeismosFragment() {} // Required empty public constructor

    public static SeismosFragment newInstance() {
        return new SeismosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_seismos, container, false);

        return root;
    }

    @Override
    public void showSomething() {

    }

    @Override
    public void setPresenter(SeismosContract.Presenter presenter) {
        mPresenter = presenter;

    }
}
