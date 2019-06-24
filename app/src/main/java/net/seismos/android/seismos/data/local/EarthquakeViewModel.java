package net.seismos.android.seismos.data.local;

import android.app.Application;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


import net.seismos.android.seismos.data.model.Earthquake;
import net.seismos.android.seismos.data.remote.usgs.InitialUsgsJsonWorker;
import net.seismos.android.seismos.data.remote.usgs.UpdateUsgsJsonWorker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkManager;

public class EarthquakeViewModel extends AndroidViewModel {
    private static final String TAG = "EarthquakeUpdate";
    private LiveData<List<Earthquake>> earthquakes;
    private LiveData<List<Earthquake>> significantEqs;

    public Earthquake getNotifiedEq() {
        return notifiedEq;
    }

    public void setNotifiedEq(Earthquake notifiedEq) {
        this.notifiedEq = notifiedEq;
    }

    private Earthquake notifiedEq;

    ArrayList<Earthquake> tempEqs = new ArrayList<>();
    public EarthquakeViewModel(Application application) {
        super(application);
    }

    public LiveData<List<Earthquake>> getEarthquakes() {
        if (earthquakes == null) {
            // Load the earthquakes from the database
            earthquakes = EarthquakeDatabaseAccessor.getInstance(getApplication())
                    .earthquakeDAO()
                    .loadAllEarthquakes();

//            loadEarthquakes();
        }
        return earthquakes;
    }

    public LiveData<List<Earthquake>> getSignificantEqs() {
        if (significantEqs == null) {
            // load quick earthquakes from significant eq db for performance
            significantEqs = SignificantEqDBAccessor.getInstance(getApplication())
                    .earthquakeDAO()
                    .loadAllEarthquakes();
            loadEarthquakes();
        }
        return significantEqs;
    }

    // Asynchronously load the Earthquakes from the feed
    public void loadEarthquakes() {

        Data data = new Data.Builder()
                .putString("test", "this is the test string")
                .build();


        OneTimeWorkRequest downloadEarthquakes =
                new OneTimeWorkRequest.Builder(InitialUsgsJsonWorker.class)
                        .setInputData(data)
                        .build();
        WorkManager.getInstance().enqueue(downloadEarthquakes);

        PeriodicWorkRequest updateEarthquakes =
                new PeriodicWorkRequest.Builder(UpdateUsgsJsonWorker.class, 15, TimeUnit.MINUTES)
//                .setConstraints(constraints)
                .build();

        WorkManager.getInstance().enqueue(updateEarthquakes);

    }
}
