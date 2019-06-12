package net.seismos.android.seismos.data.local;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.util.Log;


import net.seismos.android.seismos.data.model.Earthquake;
import net.seismos.android.seismos.data.remote.usgs.UsgsJsonWorker;

import java.util.ArrayList;
import java.util.List;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class EarthquakeViewModel extends AndroidViewModel {
    private static final String TAG = "EarthquakeUpdate";
    private LiveData<List<Earthquake>> earthquakes;
    private LiveData<List<Earthquake>> significantEqs;

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
        OneTimeWorkRequest downloadEarthquakes =
                new OneTimeWorkRequest.Builder(UsgsJsonWorker.class)
                .build();
        WorkManager.getInstance().enqueue(downloadEarthquakes);
    }
}
