package net.seismos.android.seismos.ui.store;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Offer;
import net.seismos.android.seismos.util.ResUtil;

import java.util.ArrayList;

public class CollectionRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final int VIEW_TYPE_IPHONE = 1;
    private final int VIEW_TYPE_AIRPODS = 2;

    ArrayList<Offer> offers;

    OfferClickListener listener;

    public interface OfferClickListener {
        void onOfferClicked(String id);
    }

    public CollectionRecyclerViewAdapter(ArrayList<Offer> offers,
                                         CollectionRecyclerViewAdapter.OfferClickListener listener) {
        this.offers = offers;
        this.listener = listener;
    }


    @Override
    public int getItemCount() {
        return offers.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        viewHolder.title.setText(offers.get(position).getTitle());
        viewHolder.creator.setText(offers.get(position).getCreator());

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOfferClicked(offers.get(position).getId());
            }
        });

        if (position%2==0) {
            viewHolder.titleImage.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.iphone_square));
        } else {
            viewHolder.titleImage.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.starbucks_square));
        }
    }

}
 class ViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView creator;
    TextView validUntil;

    ImageView titleImage;
    public ViewHolder(View view) {
        super(view);
        titleImage = view.findViewById(R.id.titleImage);
        title = view.findViewById(R.id.title);
        creator = view.findViewById(R.id.creator);
        validUntil = view.findViewById(R.id.validUntil);
    }
}
