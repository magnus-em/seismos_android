package net.seismos.android.seismos;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mTextMessage = findViewById(R.id.message);

        final BottomNavigationView navigation =  findViewById(R.id.navigation);




        BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
                = new BottomNavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.navigation_dashboard:
                        mTextMessage.setText(R.string.title_home);
                        return true;
                    case R.id.navigation_map:
                        mTextMessage.setText(R.string.title_map);
                        return true;
                    case R.id.navigation_safety:
                        mTextMessage.setText(R.string.title_safety);
                        return true;
                   case R.id.navigation_wallet:
                        mTextMessage.setText(R.string.title_wallet);
                        item.setChecked(true);


                }
                return false;
            }
        };



        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
