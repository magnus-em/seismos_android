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

    final private ArrayList<Offer> offers = new ArrayList<>();
    final private ArrayList<Offer> bought = new ArrayList<>();

    private boolean initial = true;

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

                                String source =queryDocumentSnapshots.getMetadata().hasPendingWrites()
                                        ? "Local" : "Server";

                                Log.d(TAG, "SOURCE: " + source);

                            }

                            populateOffers();

//                            if (initial) {
//                                populateOffers();
//                                initial = false;
//                            } else {
//                                storeTabAdapter.notifyDataSetChanged(); // this might only do the tab adapter and not the fragment's adapters underneath
//                            }
                        }


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
        storeTabAdapter.addFragment(offersFragment, "Today's offers");
        storeTabAdapter.addFragment(collectionFragment, "My collection");


        viewPager.setAdapter(storeTabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        final TextView balance = view.findViewById(R.id.balanceText);

        db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@javax.annotation.Nullable DocumentSnapshot documentSnapshot, @javax.annotation.Nullable FirebaseFirestoreException e) {
                        balance.setText(Long.toString((long)documentSnapshot.get("balance")));
                    }
                });


    }



    private void populateOffers() {
        offersFragment.setData(offers);

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
                        populateCollection();
                    }
                });


//        db.collection("users").document(FirebaseAuth.getInstance().getUid())
//                .collection("bought")
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//                    @Override
//                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                        Log.d(TAG, "Successfully got BOUGHT offers");
//                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
//                            Offer offer = new Offer();
//                            String id = doc.getId();
//                            id = id.replaceAll("\\s","");
//                            offer.setId(id);
//                            offer.setKey((String)doc.get("key"));
//                            bought.add(offer);
//                        }
//                        populateCollection();
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "Failure to get bought offers: " + e.toString());
//            }
//        });
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Log.d("NAVDEBUG", "onAttach called in StoreFragment");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("NAVDEBUG", "onDetach() called in StoreFragment");
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.d("NAVDEBUG", "onStart() called in StoreFragment");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("NAVDEBUG", "onResume() called in StoreFragment");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("NAVDEBUG", "onPause() called in StoreFragment");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("NAVDEBUG", "onStop() called in StoreFragment");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("NAVDEBUG", "onSaveInstanceState() called in StoreFragment");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("NAVDEBUG", "onDestroy() called in StoreFragment");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("NAVDEBUG", "onDestroyView() called in StoreFragment()");
    }
}
