package net.seismos.android.seismos.ui.store;

import android.content.Intent;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.seismos.android.seismos.R;

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

        TextView balance = root.findViewById(R.id.balanceText);
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StoreDetailActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }

    @Override
    public void showSomething() {

    }

    @Override
    public void setPresenter(StoreContract.Presenter presenter) {

    }
}
