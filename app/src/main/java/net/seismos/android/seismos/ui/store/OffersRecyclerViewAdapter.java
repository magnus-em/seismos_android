package net.seismos.android.seismos.ui.store;

import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.util.ResUtil;

import java.util.List;

public class OffersRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_LOADING = 0;
    private final int VIEW_TYPE_OFFER = 1;
    private final int VIEW_TYPE_BOOST50 = 2;
    private final int VIEW_TYPE_BOOST150 = 3;

    public List<String> mItemList;


    public OffersRecyclerViewAdapter(List<String> itemList) {
        mItemList = itemList;
    }

    @Override
    public int getItemViewType(int position) {
        if (mItemList.get(position) == null) return VIEW_TYPE_LOADING;
        if (mItemList.get(position).equalsIgnoreCase("offer")) {
            return VIEW_TYPE_OFFER;
        } else if (mItemList.get(position).equalsIgnoreCase("boost50")) {
            return VIEW_TYPE_BOOST50;
        } else if (mItemList.get(position).equalsIgnoreCase("boost100")) {
            return VIEW_TYPE_BOOST150;
        } else {
            return VIEW_TYPE_LOADING;
        }
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
        return mItemList == null ? 0 : mItemList.size();
    }

    private class OfferViewHolder extends RecyclerView.ViewHolder {
        ImageView image;

        public OfferViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.titleImage);
        }
    }

    private void populateOffer(OfferViewHolder viewHolder, int position) {
        if (position%2==0) {
            viewHolder.image.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.offers_iphone));
        } else if (position%3==0) {
            viewHolder.image.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.offers_airpods));
        } else {
            viewHolder.image.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.disaster));
        }
    }

}
