package net.seismos.android.seismos;

import android.content.Context;
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
import android.widget.TextView;



public class HomeFragment extends Fragment {

    private Button mButton;
    private Button mButton2;
    private Button mButton3;
    private Button mButton4;
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*
         mToolbar =  view.findViewById(R.id.toolbar_home);
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);

        ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false); */



        mButton = view.findViewById(R.id.button);
        mButton2 = view.findViewById(R.id.button2);
        mButton3 = view.findViewById(R.id.button3);
        mButton4 = view.findViewById(R.id.button4);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecentEQSelectedListener.onRecentEQSelected("seattle");
            }
        });

        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecentEQSelectedListener.onRecentEQSelected("nyc");
            }
        });

        mButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecentEQSelectedListener.onRecentEQSelected("hk");
            }
        });

        mButton4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnRecentEQSelectedListener.onRecentEQSelected("london");
            }
        });

        return view;
    }


}
