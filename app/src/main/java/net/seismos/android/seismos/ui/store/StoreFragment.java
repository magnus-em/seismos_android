package net.seismos.android.seismos.ui.store;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.ui.home.ChartFragmentToday;
import net.seismos.android.seismos.ui.home.ChartFragmentWeek;
import net.seismos.android.seismos.ui.home.ChartTabAdapter;

public class StoreFragment extends Fragment implements StoreContract.View {

    private StoreTabAdapter storeTabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;


    public StoreFragment() { // required empty public constructor
        }

    public static StoreFragment newInstance() {
        return new StoreFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_store, container, false);

        tabLayout = root.findViewById(R.id.tabLayout);
        viewPager = root.findViewById(R.id.viewPager);

        storeTabAdapter = new StoreTabAdapter(getChildFragmentManager());
        storeTabAdapter.addFragment(new OffersFragment(), "Today's offers");
        storeTabAdapter.addFragment(new CollectionFragment(), "My collection");


        viewPager.setAdapter(storeTabAdapter);
        tabLayout.setupWithViewPager(viewPager);
        return root;
    }

    @Override
    public void showSomething() {

    }

    @Override
    public void setPresenter(StoreContract.Presenter presenter) {

    }
}
