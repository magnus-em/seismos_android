package net.seismos.android.seismos;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";


    private ImageButton mEnablesButton;
    private ImageView rings;
    private Toolbar mToolbar;

    private int ringsCount = 1;

    OnRecentEQSelectedListener mOnRecentEQSelectedListener;

    public HomeFragment() {}// Required empty public constructor

    // Callback interface that the host Activity must implement. When the user clicks on one of
    // the recent earthquake globes, HomeFragment calls onRecentEQSelected in the host activity,
    // passing in the location selected
    // TODO genearlize location selection to something other than a String
    public interface OnRecentEQSelectedListener {
        void onRecentEQSelected(String location);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            mOnRecentEQSelectedListener = (OnRecentEQSelectedListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement OnRecentEQSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Need to switch recent earthquake view scroll from Horizontal ScrollView to something like RecyclerView
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        ImageView mGlobe1 = view.findViewById(R.id.globeview_japan);
        ImageView mGlobe2 = view.findViewById(R.id.globeview_usa);
        ImageView mGlobe3 = view.findViewById(R.id.globeview_nepal);
        ImageView mGlobe4 = view.findViewById(R.id.globeview_chile);
        ImageView mGlobe5 = view.findViewById(R.id.globeview_turkey);
        ImageView mGlobe6 = view.findViewById(R.id.globeview_australia);

        rings = view.findViewById(R.id.rings);


        TextView mSEEALL = view.findViewById(R.id.see_all_descriptor);

        mSEEALL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecentEQSelectedListener.onRecentEQSelected("");
            }
        });

        mGlobe1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecentEQSelectedListener.onRecentEQSelected("japan");
            }
        });

        mGlobe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecentEQSelectedListener.onRecentEQSelected("usa");
            }
        });

        mGlobe3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecentEQSelectedListener.onRecentEQSelected("nepal");
            }
        });

        mGlobe4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecentEQSelectedListener.onRecentEQSelected("chile");
            }
        });

        mGlobe5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecentEQSelectedListener.onRecentEQSelected("turkey");
            }
        });

        mGlobe6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecentEQSelectedListener.onRecentEQSelected("australia");
            }
        });

        ImageView mEnablesButton = view.findViewById(R.id.enable_button);
        mEnablesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateRings();
            }
        });

        return view;
    }

    private void updateRings() {
        switch (ringsCount) {
            case 0:
                rings.setImageDrawable(getResources().getDrawable(R.drawable.rings_1));
                ringsCount++;
                break;
            case 1:
                rings.setImageDrawable(getResources().getDrawable(R.drawable.rings_2));
                ringsCount++;
                break;
            case 2:
                rings.setImageDrawable(getResources().getDrawable(R.drawable.rings_3));
                ringsCount = 0;
                break;
        }
    }
}
