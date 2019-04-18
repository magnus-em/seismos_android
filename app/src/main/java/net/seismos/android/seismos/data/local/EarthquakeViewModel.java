package net.seismos.android.seismos.data.local;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;


import net.seismos.android.seismos.data.model.Earthquake;
import net.seismos.android.seismos.data.remote.usgs.EarthquakeListUpdateWorkerAtom;
import net.seismos.android.seismos.data.remote.usgs.UsgsJsonWorker;

import java.util.ArrayList;
import java.util.List;

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

public class EarthquakeViewModel extends AndroidViewModel {
    private static final String TAG = "EarthquakeUpdate";
    private LiveData<List<Earthquake>> earthquakes;
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

            loadEarthquakes();
        }
        return earthquakes;
    }

    // Asynchronously load the Earthquakes from the feed
    public void loadEarthquakes() {
        OneTimeWorkRequest downloadEarthquakes =
                new OneTimeWorkRequest.Builder(UsgsJsonWorker.class)
                .build();
        WorkManager.getInstance().enqueue(downloadEarthquakes);

    }
}
