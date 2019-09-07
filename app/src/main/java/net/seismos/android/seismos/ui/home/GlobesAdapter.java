package net.seismos.android.seismos.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.model.LatLng;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Earthquake;
import net.seismos.android.seismos.util.DepressAnimationUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GlobesAdapter extends RecyclerView.Adapter {


    private ArrayList<Earthquake> earthquakes = new ArrayList<>();

    private HomeFragment.OnEqGlobeSelectedListener listener;

    GlobesAdapter(HomeFragment.OnEqGlobeSelectedListener listener) {
        this.listener = listener;
    }

    public void setEarthquakes(ArrayList<Earthquake> earthquakes) {
        this.earthquakes = earthquakes;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.globe, parent, false);


        return new GlobeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DepressAnimationUtil.setup(holder.itemView);
        GlobeViewHolder vh = (GlobeViewHolder)holder;
        vh.globe.setEarthquake(earthquakes.get(position));
//        vh.globe.setDrawable(position);
        vh.title.setText(parseTitle(earthquakes.get(position)));
        vh.detail.setText(parseDetail(earthquakes.get(position)));

        holder.itemView.setOnClickListener((View v) -> {
            listener.openMapToLatLng(new LatLng(
                    earthquakes.get(position).getLatitude(),
                    earthquakes.get(position).getLongitude()
            ));
        });




    }

    private String parseTitle(Earthquake eq) {
        String result = "M";
        result = result.concat(Double.toString(eq.getMag()));

        return result;
    }
    private String parseDetail(Earthquake eq) {
        Date date = new Date(eq.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd");

        return sdf.format(date);
    }

    @Override
    public int getItemCount() {
        return earthquakes.size();
    }


    private class GlobeViewHolder extends RecyclerView.ViewHolder {
         EqGlobeView globe;
         TextView title;
         TextView detail;

        GlobeViewHolder(View itemView) {
            super(itemView);
            globe = itemView.findViewById(R.id.globe);
            title = itemView.findViewById(R.id.title);
            detail = itemView.findViewById(R.id.detail);
        }
    }
}
