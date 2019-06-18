package net.seismos.android.seismos.ui.store;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Offer;

import java.util.ArrayList;
import java.util.List;

public class OffersFragment extends Fragment {

    RecyclerView recyclerView;
    OffersRecyclerViewAdapter recyclerViewAdapter;

    OffersRecyclerViewAdapter.OfferClickListener listener;

    ArrayList<Offer> offers = new ArrayList<>();

    OffersFragment(OffersRecyclerViewAdapter.OfferClickListener listener) {
        this.listener = listener;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_offers, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        return root;
    }

    protected void populateData(ArrayList<Offer> offerList) {
        offers = offerList;
        recyclerViewAdapter = new OffersRecyclerViewAdapter(offers, listener);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
