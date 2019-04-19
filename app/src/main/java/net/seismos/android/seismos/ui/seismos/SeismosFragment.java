package net.seismos.android.seismos.ui.seismos;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import net.seismos.android.seismos.R;

import java.util.ArrayList;

public class SeismosFragment extends Fragment implements SeismosContract.View {

    private SeismosContract.Presenter mPresenter;

    RecyclerView recyclerView;
    NewsRecyclerViewAdapter recyclerViewAdapter;
    ArrayList<String> rowsArrayList = new ArrayList<>();

    boolean isLoading = false;


    public SeismosFragment() {} // Required empty public constructor

    public static SeismosFragment newInstance() {
        return new SeismosFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_seismos, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        populateData();
        initAdapter();
        initScrollListener();

        return root;
    }

    private void populateData() {
        for (int i = 0; i < 12; i++) {
            if (i%2==0) {
                rowsArrayList.add("large");
            } else {
                rowsArrayList.add("small");
            }
        }
    }

    private void initAdapter() {
        recyclerViewAdapter = new NewsRecyclerViewAdapter(rowsArrayList);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

                if (!isLoading) {
                    {
                        if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition()
                                == rowsArrayList.size() -1) {
                            // Bottom of the list
                            Log.d("newsfeed","loading more");
                            loadMore();
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    private void loadMore() {
        rowsArrayList.add(null);
        recyclerViewAdapter.notifyItemChanged(rowsArrayList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                populateData();
                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    @Override
    public void showSomething() {

    }

    @Override
    public void setPresenter(SeismosContract.Presenter presenter) {
        mPresenter = presenter;

    }
}
