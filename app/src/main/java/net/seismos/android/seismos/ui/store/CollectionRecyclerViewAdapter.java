package net.seismos.android.seismos.ui.store;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.util.ResUtil;

import java.util.ArrayList;

public class CollectionRecyclerViewAdapter extends RecyclerView.Adapter<ViewHolder> {
    private final int VIEW_TYPE_IPHONE = 1;
    private final int VIEW_TYPE_AIRPODS = 2;

    ArrayList<String> demoList;

    public CollectionRecyclerViewAdapter(ArrayList<String> list) {
        demoList = list;
    }

    @Override
    public int getItemViewType(int position) {
        String entry = demoList.get(position);
        if (entry.equalsIgnoreCase("even")) {
            return VIEW_TYPE_IPHONE;
        } else {
            return VIEW_TYPE_AIRPODS;
        }
    }

    @Override
    public int getItemCount() {
        return demoList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {
        if (position%2==0) {
            viewHolder.titleImage.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.iphone_square));
        } else {
            viewHolder.titleImage.setImageDrawable(ResUtil.getInstance().getDrawable(R.drawable.starbucks_square));
        }
    }

}
 class ViewHolder extends RecyclerView.ViewHolder {
    ImageView titleImage;
    public ViewHolder(View view) {
        super(view);
        titleImage = view.findViewById(R.id.titleImage);
    }
}
