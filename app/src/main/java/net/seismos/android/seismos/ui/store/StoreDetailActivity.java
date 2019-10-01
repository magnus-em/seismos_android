package net.seismos.android.seismos.ui.store;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import net.seismos.android.seismos.R;
import net.seismos.android.seismos.data.model.Offer;

public class StoreDetailActivity extends AppCompatActivity {
    private static final String TAG = "StoreDetailActivity";

    public static final String ID = "id";

    private String mId;

    private Offer offer = new Offer();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_detail);

        mId = getIntent().getStringExtra(ID);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("globalstore")
                .document(mId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        offer = documentSnapshot.toObject(Offer.class);
                        Log.d(TAG, "Successfully got offer from id");
                        populateOffer();
                    }
                });

        db.collection("users")
                .document(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .collection("bought")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        for (DocumentSnapshot doc : queryDocumentSnapshots) {
                            if (doc.getId().equalsIgnoreCase(mId)) {
                                offer.setKey((String)doc.get("key"));
                                offer.setBought(true);
                                Log.d(TAG, "Successfully got key");
                                populateKey();
                            }
                        }
                    }
                });

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);



        final Button unlock = findViewById(R.id.unlockButton);
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unlock.setVisibility(View.GONE);
            }
        });
    }

    private void populateOffer() {

    }

    private void populateKey() {
        ((Button)findViewById(R.id.itemCodeChip)).setText(offer.getKey());
    }

    @Override
    protected void onStart() {
        super.onStart();
        TextView title = findViewById(R.id.title);
        title.setText(getIntent().getStringExtra(ID));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
