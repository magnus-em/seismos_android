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

public class CollectionFragment extends Fragment {

    RecyclerView recyclerView;
    CollectionRecyclerViewAdapter recyclerViewAdapter;

    // for demo
    ArrayList<String> rowsArrayList = new ArrayList<>();

    ArrayList<Offer> offers;

    CollectionRecyclerViewAdapter.OfferClickListener listener;

    CollectionFragment() {

    }

    CollectionFragment(CollectionRecyclerViewAdapter.OfferClickListener listener) {
        this.listener = listener;
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_collection, container, false);
        recyclerView = root.findViewById(R.id.recyclerView);
        initAdapter();
        return root;
    }

    private void initAdapter() {
        for (int i = 0; i < 4; i++) {
            if (i%2==0) {
                rowsArrayList.add("even");
            } else {
                rowsArrayList.add("odd");
            }
        }
    }

    protected void populateData(ArrayList<Offer> offerList) {
        offers = offerList;
        recyclerViewAdapter = new CollectionRecyclerViewAdapter(offers, listener);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerViewAdapter.notifyDataSetChanged();
    }
}
