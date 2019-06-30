package net.seismos.android.seismos.ui.seismos;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Article;

import java.util.ArrayList;

public class SeismosFragment extends Fragment implements SeismosContract.View,
                                                    NewsRecyclerViewAdapter.ArticleClickListener {

    private SeismosContract.Presenter mPresenter;

    private static final String TAG = "SeismosFragment";


    ArrayList<String> rowsArrayList = new ArrayList<>();

    FirebaseFirestore db;

    boolean isLoading = false;

    final ArrayList<Article> articles = new ArrayList<>();

    RecyclerView recyclerView;
    NewsRecyclerViewAdapter recyclerViewAdapter;

    private boolean initial = true;


    public SeismosFragment() {} // Required empty public constructor

    public static SeismosFragment newInstance() {
        return new SeismosFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("NAVDEBUG", "onCreate called in SeismosFragment");

        Log.d(TAG, "onCreate called");

        db = FirebaseFirestore.getInstance();


        db.collection("articles").orderBy("entry", Query.Direction.DESCENDING)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot queryDocumentSnapshots,
                                        FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.d(TAG, "Listen failed.", e);
                            return;
                        }

                        if (queryDocumentSnapshots != null) {
                            for (QueryDocumentSnapshot document : queryDocumentSnapshots) {
                                articles.add(document.toObject(Article.class));
                                Log.d(TAG, "article title: " + document.get("title"));
                            }


//
//                            if (false) {
//                                initAdapter();
//                                initScrollListener();
//                                initial = false;
//                            } else {
//                                recyclerViewAdapter.notifyDataSetChanged();
//                            }

                        }
                    }
                });
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_seismos, container, false);

        recyclerView = root.findViewById(R.id.recyclerView);
        return root;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initAdapter();
//        initScrollListener();



        ImageView projectSeismos = view.findViewById(R.id.projectSeismos);
        ImageView networkStatus = view.findViewById(R.id.networkStatus);
        ImageView eqSafety = view.findViewById(R.id.earthquakeSafety);
        ImageView dyfi = view.findViewById(R.id.didYouFeelIt);

        setupImageButton(projectSeismos);
        setupImageButton(networkStatus);
        setupImageButton(eqSafety);
        setupImageButton(dyfi);

        networkStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_seismosFragment_to_networkStatusFragment);
            }
        });

        eqSafety.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_seismosFragment_to_eqSafetyFragment);
            }
        });

        dyfi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_seismosFragment_to_DYFIFragment);
            }
        });


        projectSeismos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(v).navigate(R.id.action_seismosFragment_to_projectSeismosFragment);
            }
        });
    }


    @SuppressLint("ClickableViewAccessibility")
    private void setupImageButton(ImageView button) {
        button.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.setAlpha(0.5f);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.setAlpha(1);
                        v.performClick();
                        break;
                    }
                    case MotionEvent.ACTION_CANCEL:{
                        v.setAlpha(1);
                        break;
                    }
                }
                return true;
            }
        });
    }


    private void initAdapter() {
        recyclerViewAdapter = new NewsRecyclerViewAdapter(articles, this);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void initScrollListener() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                LinearLayoutManager linearLayoutManager = (LinearLayoutManager)
                        recyclerView.getLayoutManager();

                if (!isLoading) {
                    {
                        if (linearLayoutManager != null &&
                        linearLayoutManager.findLastCompletelyVisibleItemPosition()
                                == rowsArrayList.size() -1) {
                            // Bottom of the list
                            Log.d("newsfeed","loading more");
                            loadMore();
                            isLoading = true;
                        }
                    }
                }
            }
        });
    }

    private void loadMore() {
        rowsArrayList.add(null);
        recyclerViewAdapter.notifyItemChanged(rowsArrayList.size() - 1);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                rowsArrayList.remove(rowsArrayList.size() - 1);
                int scrollPosition = rowsArrayList.size();
                recyclerViewAdapter.notifyItemRemoved(scrollPosition);
                recyclerViewAdapter.notifyDataSetChanged();
                isLoading = false;
            }
        }, 2000);
    }

    @Override
    public void onArticleClicked(Article article) {
        String url = article.getUrl();

        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(browserIntent);

        Log.d(TAG, "Article clicked: " + article.getTitle());
    }

    @Override
    public void showSomething() {

    }

    @Override
    public void setPresenter(SeismosContract.Presenter presenter) {
        mPresenter = presenter;

    }
}
