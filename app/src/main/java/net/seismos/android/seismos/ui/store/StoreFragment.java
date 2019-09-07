package net.seismos.android.seismos.ui.store;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Offer;

import java.util.ArrayList;

public class StoreFragment extends Fragment implements StoreContract.View,
                            OffersRecyclerViewAdapter.OfferClickListener,
                            CollectionRecyclerViewAdapter.OfferClickListener {
    private static final String TAG = "StoreFragment";

    private OffersFragment offersFragment;
    private CollectionFragment collectionFragment;

    private FirebaseFirestore db;

    LiveData<ArrayList<Offer>> liveOffers = new MutableLiveData<>();

    final private ArrayList<Offer> offers = new ArrayList<>();
    final private ArrayList<Offer> bought = new ArrayList<>();

    private String balanceString;

    private boolean offersLoaded = false;
    private boolean collectionLoaded = false;

    StoreTabAdapter storeTabAdapter;


    public StoreFragment() {} // required empty public constructor

    public static StoreFragment newInstance() {
        return new StoreFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("NAVDEBUG", "onCreate called in StoreFragment");

        offersFragment = new OffersFragment(this);
        collectionFragment = new CollectionFragment(this);

        db = FirebaseFirestore.getInstance();

        db.collection("globalstore")
                .orderBy("entry", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots,
                                        FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(TAG, "Listen failed: ", e);
                            return;
                        }

                        if (queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                Offer offer = document.toObject(Offer.class);
                                String id = document.getId();
                                id = id.replaceAll("\\s", "");
                                offer.setId(id);
                                offers.add(offer);

                            }

                            offersLoaded = true;
                            populateOffers();
                        }
                    }
                });

        db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .collection("bought")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots,
                                        FirebaseFirestoreException e) {
                        Log.d(TAG, "Successfully got BOUGHT offers");
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            Offer offer = new Offer();
                            String id = doc.getId();
                            id = id.replaceAll("\\s","");
                            offer.setId(id);
                            offer.setKey((String)doc.get("key"));
                            bought.add(offer);
                        }
                        collectionLoaded = true;
                        populateOffers();
                    }
                });
        db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        balanceString = Long.toString((long)documentSnapshot.get("balance"));
                    }
                });


    }

    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d("NAVDEBUG", "onCreateView called in StoreFragment");
            return inflater.inflate(R.layout.fragment_store, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d("NAVDEBUG", "onViewCreated called in StoreFragment");
        TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        ViewPager viewPager = view.findViewById(R.id.viewPager);


        storeTabAdapter = new StoreTabAdapter(getChildFragmentManager());
        storeTabAdapter.addFragment(offersFragment, "Today's rewards");
        storeTabAdapter.addFragment(collectionFragment, "Your rewards");


        viewPager.setAdapter(storeTabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        final TextView balance = view.findViewById(R.id.balanceText);
        balance.setText(balanceString);

        db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        balance.setText(Long.toString((long)documentSnapshot.get("balance")));
                    }
                });

        populateOffers();
    }



    private void populateOffers() {
        Log.d(TAG, "Populate called: offers size: " + offers.size());
        Log.d(TAG, "collection size: " + bought.size());

        if (offersLoaded && collectionLoaded) {
            offersFragment.setData(offers);
            populateCollection();
        }
    }

    private void populateCollection() {
        String key0;
        String key1;

        for (Offer boughtOffer : bought) {
            for (Offer globalOffer : offers) {

                key0 = boughtOffer.getId();
                key1 = globalOffer.getId();

                if (key0.equalsIgnoreCase(key1)) {
                    Log.d(TAG, "MATCHED ID: " + globalOffer.getId() + " and " + boughtOffer.getId());
                    boughtOffer.setTitle(globalOffer.getTitle());
                    boughtOffer.setSubtitle(globalOffer.getSubtitle());
                    boughtOffer.setPrice(globalOffer.getPrice());
                    boughtOffer.setPhoto(globalOffer.getPhoto());
                    boughtOffer.setCreator(globalOffer.getCreator());
                    boughtOffer.setDetails(globalOffer.getDetails());
                }
            }
        }

        collectionFragment.setData(bought);
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
