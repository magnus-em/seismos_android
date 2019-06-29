package net.seismos.android.seismos.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.seismos.android.seismos.R;

import java.util.ArrayList;
import java.util.List;

public class AwardsAdapter extends RecyclerView.Adapter {

    private ArrayList<String> awards = new ArrayList<>();


    AwardsAdapter() {
        for (int i = 0; i < 10; i++) {
            if (i%2==0) {
                awards.add("yello");
            } else {
                awards.add("okayyy");
            }
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.tropy, parent, false);


        return new TropyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {



    }

    @Override
    public int getItemCount() {
        return awards.size();
    }


    private class TropyViewHolder extends RecyclerView.ViewHolder {


        TropyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
