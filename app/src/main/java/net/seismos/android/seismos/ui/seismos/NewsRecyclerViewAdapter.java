package net.seismos.android.seismos.ui.seismos;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.seismos.android.seismos.R;

import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_SMALL = 0;
    private final int VIEW_TYPE_LARGE = 1;
    private final int VIEW_TYPE_LOADING = 2;

    public List<String> mItemList;

    public NewsRecyclerViewAdapter(List<String> itemList) {
        mItemList = itemList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_LARGE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_large_list_item, parent, false);
            return new NewsLargeViewHolder(view);
        } else  if (viewType == VIEW_TYPE_SMALL){
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.article_small_list_item, parent, false);
            return new NewsSmallViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.news_loading_item, parent, false);
            return new LoadingViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof NewsLargeViewHolder) {
            populateLargeArticle((NewsLargeViewHolder) viewHolder, position);
        } else if (viewHolder instanceof NewsSmallViewHolder) {
            populateSmallArticle((NewsSmallViewHolder) viewHolder, position);
        }
    }

    @Override
    public int getItemCount() {
        return mItemList == null ? 0 : mItemList.size();
    }

    // the following method decides the type of ViewHolder to display in the RecyclerView

    @Override
    public int getItemViewType(int position) {
        if (mItemList.get(position) == null) return VIEW_TYPE_LOADING;
        return mItemList.get(position).equalsIgnoreCase("small")  ? VIEW_TYPE_SMALL : VIEW_TYPE_LARGE;
    }

    private class NewsLargeViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public NewsLargeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.detailTimeText);
        }
    }

    private class NewsSmallViewHolder extends RecyclerView.ViewHolder {

        TextView title;

        public NewsSmallViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.detailTimeText);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }

    private void populateLargeArticle(NewsLargeViewHolder viewHolder, int position) {
        String title = mItemList.get(position);
//        viewHolder.title.setText(title);
    }

    private void populateSmallArticle(NewsSmallViewHolder viewholder, int position) {
        String title = mItemList.get(position);
//        viewholder.title.setText(title);
    }
}
