package net.seismos.android.seismos.ui.map;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.Earthquake;
import net.seismos.android.seismos.databinding.EarthquakeListItemBinding;
import net.seismos.android.seismos.util.ResUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class EarthquakeRecyclerViewAdapter
        extends RecyclerView.Adapter<EarthquakeRecyclerViewAdapter.ViewHolder> {

    private final List<Earthquake> mEarthquakes;

    public EarthquakeRecyclerViewAdapter(List<Earthquake> mEarthquakes) {
        this.mEarthquakes = mEarthquakes;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        EarthquakeListItemBinding binding = EarthquakeListItemBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public int getItemCount() {
        return mEarthquakes.size();
    }

    private static final NumberFormat MAGNITUDE_FORMAT =
            new DecimalFormat("0.0");
    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("HH:mm", Locale.US);

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Earthquake earthquake = mEarthquakes.get(position);
        if (earthquake.getMagnitude() < 5) {
            holder.binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_49));
        } else if (earthquake.getMagnitude() < 6) {
            holder.binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_59));
        } else if (earthquake.getMagnitude() < 6.5) {
            holder.binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_64));
        } else if (earthquake.getMagnitude() < 7) {
            holder.binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_69));
        } else if (earthquake.getMagnitude() < 7.5) {
            holder.binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_74));
        } else if (earthquake.getMagnitude() < 8) {
            holder.binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_79));
        } else {
            holder.binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_8));
        }
        holder.binding.setEarthquake(earthquake);
        holder.binding.executePendingBindings();
    }



    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final EarthquakeListItemBinding binding;

        public ViewHolder(EarthquakeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setMagnitudeformat(MAGNITUDE_FORMAT);
            binding.setTimeformat(TIME_FORMAT);

        }
    }
}
