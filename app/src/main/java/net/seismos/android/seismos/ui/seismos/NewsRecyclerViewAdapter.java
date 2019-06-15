package net.seismos.android.seismos.ui.seismos;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Article;

import java.util.List;

public class NewsRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_SMALL = 0;
    private final int VIEW_TYPE_LARGE = 1;
    private final int VIEW_TYPE_LOADING = 2;

    private List<Article> mArticles;

    private final ArticleClickListener clickListener;

    public interface ArticleClickListener {
        void onArticleClicked(Article article);
    }

    public NewsRecyclerViewAdapter(List<Article> articles, ArticleClickListener listener) {
        clickListener = listener;
        mArticles = articles;
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
        return mArticles == null ? 0 : mArticles.size();
    }

    // the following method decides the type of ViewHolder to display in the RecyclerView

    @Override
    public int getItemViewType(int position) {
        if (mArticles.get(position) == null) return VIEW_TYPE_LOADING;
        return mArticles.get(position).getBig()  ? VIEW_TYPE_LARGE : VIEW_TYPE_SMALL;
    }

    private class NewsLargeViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView subtitle;
        ImageView image;
        TextView date;
        TextView publisher;

        protected NewsLargeViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.articleTitle);
            image = itemView.findViewById(R.id.titleImage);
            subtitle = itemView.findViewById(R.id.subtitleText);
            date = itemView.findViewById(R.id.detailTimeText);
            publisher = itemView.findViewById(R.id.publisherText);
        }
    }

    private class NewsSmallViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView subtitle;
        TextView date;
        TextView publisher;
        ImageView image;


        public NewsSmallViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.articleTitle);
            image = itemView.findViewById(R.id.titleImage);
            subtitle = itemView.findViewById(R.id.subtitleText);
            date = itemView.findViewById(R.id.detailTimeText);
            publisher = itemView.findViewById(R.id.publisherText);
        }
    }

    private class LoadingViewHolder extends RecyclerView.ViewHolder {
        ProgressBar progressBar;

        public LoadingViewHolder(@NonNull View itemView) {
            super(itemView);
            progressBar = itemView.findViewById(R.id.progressbar);
        }
    }

    private void populateLargeArticle(NewsLargeViewHolder viewHolder, final int position) {

        Picasso.get()
                .load(mArticles.get(position).getPhoto())
                .into(viewHolder.image);

        viewHolder.title.setText(mArticles.get(position).getTitle());

        if (mArticles.get(position).getSubtitle() != null) {
            viewHolder.subtitle.setText(mArticles.get(position).getSubtitle());
        } else {
            viewHolder.subtitle.setVisibility(View.GONE);
        }

        viewHolder.publisher.setText(mArticles.get(position).getPublisher());

        viewHolder.date.setText(mArticles.get(position).getDate());


        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onArticleClicked(mArticles.get(position));
            }
        });



    }

    private void populateSmallArticle(NewsSmallViewHolder viewHolder, final int position) {
        Picasso.get()
                .load(mArticles.get(position).getPhoto())
                .into(viewHolder.image);

        viewHolder.title.setText(mArticles.get(position).getTitle());

        if (mArticles.get(position).getSubtitle() != null) {
            viewHolder.subtitle.setText(mArticles.get(position).getSubtitle());
        } else {
            viewHolder.subtitle.setVisibility(View.INVISIBLE);
            viewHolder.subtitle.setHeight(25);
        }

        viewHolder.publisher.setText(mArticles.get(position).getPublisher());

       viewHolder.date.setText(mArticles.get(position).getDate());

       viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickListener.onArticleClicked(mArticles.get(position));
            }
        });

    }
}
