package net.seismos.android.seismos.ui.map;

import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Earthquake;
import net.seismos.android.seismos.databinding.EarthquakeListItemBinding;
import net.seismos.android.seismos.databinding.EarthquakeListItemTopBinding;
import net.seismos.android.seismos.ui.alert.AlertActivity;
import net.seismos.android.seismos.util.DepressAnimationUtil;
import net.seismos.android.seismos.util.ResUtil;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class EarthquakeRecyclerViewAdapter
        extends RecyclerView.Adapter<RecyclerView.ViewHolder> {





    private final List<Earthquake> mEarthquakes;
    private final OnEqClickListener listener;

    public EarthquakeRecyclerViewAdapter(List<Earthquake> mEarthquakes, OnEqClickListener listener) {
        this.mEarthquakes = mEarthquakes;
        this.listener = listener;
    }

    public interface OnEqClickListener {
        void onItemClicked(Earthquake eq);
        void onChipClicked(Earthquake eq);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == 1) {
            EarthquakeListItemBinding binding = EarthquakeListItemBinding.inflate(
                    LayoutInflater.from(parent.getContext()), parent, false);
            return new ViewHolder(binding);
        } else {
            EarthquakeListItemTopBinding binding =
                    EarthquakeListItemTopBinding.inflate(LayoutInflater.from(parent.getContext()), parent,
                    false);
            return new ViewHolderTop(binding);
        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position==0) return 0;
        else return 1;
    }

    @Override
    public int getItemCount() {
        return mEarthquakes.size();
    }

    private static final NumberFormat MAGNITUDE_FORMAT =
            new DecimalFormat("0.0");
    private static final SimpleDateFormat TIME_FORMAT =
            new SimpleDateFormat("EEE, d MMM hh:mm:ss a z", Locale.US);

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof ViewHolder) {
            Earthquake earthquake = mEarthquakes.get(position);
            if (earthquake.getMagnitude() < 5) {
                ((ViewHolder)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_49));
            } else if (earthquake.getMagnitude() < 6) {
                ((ViewHolder)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_59));
            } else if (earthquake.getMagnitude() < 6.5) {
                ((ViewHolder)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_64));
            } else if (earthquake.getMagnitude() < 7) {
                ((ViewHolder)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_69));
            } else if (earthquake.getMagnitude() < 7.5) {
                ((ViewHolder)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_74));
            } else if (earthquake.getMagnitude() < 8) {
                ((ViewHolder)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_79));
            } else {
                ((ViewHolder)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_8));
            }
            ((ViewHolder)holder).binding.setEarthquake(earthquake);
            ((ViewHolder)holder).binding.executePendingBindings();
            ((ViewHolder)holder).bind(earthquake, listener);
        } else if (holder instanceof ViewHolderTop) {
            Earthquake earthquake = mEarthquakes.get(position);
            if (earthquake.getMagnitude() < 5) {
                ((ViewHolderTop)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_49));
            } else if (earthquake.getMagnitude() < 6) {
                ((ViewHolderTop)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_59));
            } else if (earthquake.getMagnitude() < 6.5) {
                ((ViewHolderTop)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_64));
            } else if (earthquake.getMagnitude() < 7) {
                ((ViewHolderTop)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_69));
            } else if (earthquake.getMagnitude() < 7.5) {
                ((ViewHolderTop)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_74));
            } else if (earthquake.getMagnitude() < 8) {
                ((ViewHolderTop)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_79));
            } else {
                ((ViewHolderTop)holder).binding.ringView.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.mag_ring_8));
            }
            ((ViewHolderTop)holder).binding.setEarthquake(earthquake);
            ((ViewHolderTop)holder).binding.executePendingBindings();
            ((ViewHolderTop)holder).bind(earthquake, listener);
        }
    }


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final EarthquakeListItemBinding binding;

        public ViewHolder(EarthquakeListItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setMagnitudeformat(MAGNITUDE_FORMAT);
            binding.setTimeformat(TIME_FORMAT);

        }

        public void bind(final Earthquake eq, final OnEqClickListener listener) {
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(eq);
                }
            });

            TextView textView = binding.getRoot().findViewById(R.id.earthquakeTime);
            Date start = new Date(eq.getTime());
            textView.setText(TIME_FORMAT.format(start));

            ((TextView) binding.getRoot().findViewById(R.id.detectedByCount))
                    .setText(Integer.toString(eq.getFelt()));


        }
    }


    public static class ViewHolderTop extends RecyclerView.ViewHolder {
        public final EarthquakeListItemTopBinding binding;

        public ViewHolderTop(EarthquakeListItemTopBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.setMagnitudeformat(MAGNITUDE_FORMAT);
            binding.setTimeformat(TIME_FORMAT);
            ImageView button = binding.getRoot().findViewById(R.id.iFeltThisChip);
            DepressAnimationUtil.setup(button);

        }

        public void bind(final Earthquake eq, final EarthquakeRecyclerViewAdapter.OnEqClickListener listener) {
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClicked(eq);
                }
            });

            binding.getRoot().findViewById(R.id.iFeltThisChip).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onChipClicked(eq);
                }
            });

            TextView textView = binding.getRoot().findViewById(R.id.earthquakeTime);
            Date start = new Date(eq.getTime());
            textView.setText(TIME_FORMAT.format(start));

            ((TextView) binding.getRoot().findViewById(R.id.detectedByCount))
                    .setText(Integer.toString(eq.getFelt()));
        }
    }
}


