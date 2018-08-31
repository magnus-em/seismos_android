package net.seismos.android.seismos;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;


public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";


    private TextView mSEEALL;
    private ImageView mGlobe1;
    private ImageView mGlobe2;
    private ImageView mGlobe3;
    private ImageView mGlobe4;
    private Toolbar mToolbar;


    OnRecentEQSelectedListener mOnRecentEQSelectedListener;

    public HomeFragment() {
    }

    public interface OnRecentEQSelectedListener {
        public void onRecentEQSelected(String location);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home_2, container, false);

        /*
         mToolbar =  view.findViewById(R.id.toolbar_home);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); */

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference mCheckBoxRef = database.getReference("checkbox");






        mGlobe1 = view.findViewById(R.id.globeview_japan);
        mGlobe2 = view.findViewById(R.id.globeview_usa);
        mGlobe3 = view.findViewById(R.id.globeview_nepal);
        mGlobe4 = view.findViewById(R.id.globeview_chile);

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
        /*
        mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                Log.i("homefrag", "checked: " + b);

                if (b) {
                    myRef.setValue("true");

                } else {
                    myRef.setValue("false");

                }


            }
        });
        */

        mCheckBoxRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String value = dataSnapshot.getValue(String.class);
                if (value.equals("true")) {

                    Intent intent = new Intent(getActivity(), AlertActivity.class);
                    startActivity(intent);

                } else if (value.equals("false")) {

                }

            }



            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("homefrag", "Failed to read value.", error.toException());
            }
        });

        return view;
    }




}
