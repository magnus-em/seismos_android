package net.seismos.android.seismos.ui.store;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
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
                            OffersRecyclerViewAdapter.OfferClickListener,
                            CollectionRecyclerViewAdapter.OfferClickListener {
    private static final String TAG = "StoreFragment";

    private StoreTabAdapter storeTabAdapter;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private OffersFragment offersFragment;
    private CollectionFragment collectionFragment;


    private boolean offersLoaded = false;


    FirebaseFirestore db;

    final ArrayList<Offer> offers = new ArrayList<>();
    final ArrayList<Offer> bought = new ArrayList<>();


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
                            String id = document.getId();
                            id = id.replaceAll("\\s","");
                            offer.setId(id);
                            offers.add(offer);
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
        collectionFragment = new CollectionFragment(this);

        storeTabAdapter = new StoreTabAdapter(getChildFragmentManager());
        storeTabAdapter.addFragment(offersFragment, "Today's offers");
        storeTabAdapter.addFragment(collectionFragment, "My collection");


        viewPager.setAdapter(storeTabAdapter);
        tabLayout.setupWithViewPager(viewPager);

        final TextView balance = view.findViewById(R.id.balanceText);
        balance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), StoreDetailActivity.class);
                startActivity(intent);
            }
        });

         db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        Log.d(TAG, "GOT BALANCE: " + (long)documentSnapshot.get("balance"));
                        balance.setText(Long.toString((long)documentSnapshot.get("balance")));
                    }
                });
    }



    private void populateOffers() {
        offersFragment.populateData(offers);
        db.collection("users").document(FirebaseAuth.getInstance().getUid())
                .collection("bought")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
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
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d(TAG, "Failure to get bought offers: " + e.toString());
            }
        });
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

        for (Offer offer : bought) {
            Log.d(TAG, "Bought titles: " + offer.getTitle());
        }
        collectionFragment.populateData(bought);
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
