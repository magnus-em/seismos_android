package net.seismos.android.seismos.ui.store;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Article;
import net.seismos.android.seismos.data.model.Offer;

import java.util.ArrayList;

public class StoreFragment extends Fragment implements StoreContract.View,
                            OffersRecyclerViewAdapter.OfferClickListener {
    private static final String TAG = "StoreFragment";

    private StoreTabAdapter storeTabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private OffersFragment offersFragment;
    private CollectionFragment collectionFragment;


    FirebaseFirestore db;

    final ArrayList<Offer> offers = new ArrayList<>();


    public StoreFragment() {} // required empty public constructor

    public static StoreFragment newInstance() {
        return new StoreFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        db = FirebaseFirestore.getInstance();

        db.collection("globalstore")
                .orderBy("entry", Query.Direction.DESCENDING)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                            Offer offer = document.toObject(Offer.class);
                            offer.setId(document.getId());
                            offers.add(offer);

                            Log.d(TAG, "offer title: " + document.get("title"));
                            Log.d(TAG, "offer ID: " + offer.getId());
                        }

                        // only begin initialization of the recyclerView and everything once
                        // the firestore query returns

                        populateOffers();

                    }
                });


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout = view.findViewById(R.id.tabLayout);
        viewPager = view.findViewById(R.id.viewPager);

        offersFragment = new OffersFragment(this);
        collectionFragment = new CollectionFragment();

        storeTabAdapter = new StoreTabAdapter(getChildFragmentManager());
        storeTabAdapter.addFragment(offersFragment, "Today's offers");
        storeTabAdapter.addFragment(collectionFragment, "My collection");


        viewPager.setAdapter(storeTabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        TextView balance = view.findViewById(R.id.balanceText);
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StoreDetailActivity.class);
                startActivity(intent);
            }
        });
    }

    private void populateOffers() {
        offersFragment.populateData(offers);
    }


    @Override
    public void onOfferClicked(String Id) {
        Intent intent = new Intent(getActivity(), StoreDetailActivity.class);
        intent.putExtra(StoreDetailActivity.ID, Id);
        startActivity(intent);
    }



    @Override
    public void showSomething() {

    }

    @Override
    public void setPresenter(StoreContract.Presenter presenter) {

    }
}
