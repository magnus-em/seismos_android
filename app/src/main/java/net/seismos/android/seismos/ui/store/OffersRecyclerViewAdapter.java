package net.seismos.android.seismos.ui.store;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Offer;
import net.seismos.android.seismos.util.ResUtil;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class OffersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_OFFER = 1;
    private final int VIEW_TYPE_BOOST50 = 2;
    private final int VIEW_TYPE_BOOST150 = 3;


     ArrayList<Offer> offers;

     OfferClickListener listener;

     public interface OfferClickListener {
         void onOfferClicked(String Id);
     }


    public OffersRecyclerViewAdapter(ArrayList<Offer> offers, OfferClickListener listener) {
        this.offers = offers;
        this.listener = listener;
    }

    // for future use – to throw in other types of views like streak and sharing views

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_OFFER;
        // recreate this logic without using the deleted itemList, base it off offers

//        if (itemList.get(position) == null) return VIEW_TYPE_LOADING;
//        if (itemList.get(position).equalsIgnoreCase("offer")) {
//            return VIEW_TYPE_OFFER;
//        } else if (itemList.get(position).equalsIgnoreCase("boost50")) {
//            return VIEW_TYPE_BOOST50;
//        } else if (itemList.get(position).equalsIgnoreCase("boost100")) {
//            return VIEW_TYPE_BOOST150;
//        } else {
//            return VIEW_TYPE_LOADING;
//        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_OFFER) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.offer_item, parent, false);
            return new OfferViewHolder(view);
        } else if (viewType == VIEW_TYPE_BOOST50) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.offer_item, parent, false);
            return new OfferViewHolder(view);
        } else if (viewType == VIEW_TYPE_BOOST150) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.offer_item, parent, false);
            return new OfferViewHolder(view);
        }
        else return null;
    }



    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        if (viewHolder instanceof OfferViewHolder) {
            populateOffer((OfferViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return offers == null ? 0 : offers.size();
    }

    private class OfferViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title;
        TextView creator;
        TextView subtitle;
        TextView avail;
        TextView price;
        TextView details;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.titleImage);
            title = itemView.findViewById(R.id.articleTitle);
            creator = itemView.findViewById(R.id.publisherText);
            price = itemView.findViewById(R.id.priceText);

        }
    }

    private void populateOffer(OfferViewHolder viewHolder, final int position) {

        DecimalFormat formatter = new DecimalFormat("#,###");

        viewHolder.title.setText(offers.get(position).getTitle());
        viewHolder.price.setText(formatter.format(offers.get(position).getPrice()));
        viewHolder.creator.setText(offers.get(position).getCreator());

        Picasso.get()
                .load(offers.get(position).getPhoto())
                .into(viewHolder.image);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onOfferClicked(offers.get(position).getId());
            }
        });

    }

}
