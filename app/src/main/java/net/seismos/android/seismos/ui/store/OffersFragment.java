package net.seismos.android.seismos.ui.store;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.seismos.android.seismos.R;

import java.util.ArrayList;

public class OffersFragment extends Fragment {

    RecyclerView recyclerView;
    OffersRecyclerViewAdapter recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();

    boolean isLoading = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_offers, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        populateData();
        initAdapter();

        return root;
    }

    private void populateData() {
        for (int i = 0; i < 12; i++)
        if (i%2==0) {
            rowsArrayList.add("offer");
        } else if (i%3==0) {
            rowsArrayList.add("boost50");
        } else {
            rowsArrayList.add("boost100");
        }
    }

    private void initAdapter() {
        recyclerViewAdapter = new OffersRecyclerViewAdapter(rowsArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }
}
