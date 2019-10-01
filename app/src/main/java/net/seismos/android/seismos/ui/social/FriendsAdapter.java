package net.seismos.android.seismos.ui.social;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.ui.home.EqGlobeView;
import net.seismos.android.seismos.util.DepressAnimationUtil;

import java.util.ArrayList;
import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter {

    private ArrayList<String> awards = new ArrayList<>();


    public FriendsAdapter() {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.friend, parent, false);


        return new TropyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        DepressAnimationUtil.setup(holder.itemView);
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
