package net.seismos.android.seismos.data.local;


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import net.seismos.android.seismos.data.model.Earthquake;

import java.util.List;

@Dao
public interface EarthquakeDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertEarthquake(Earthquake earthquake);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertEarthquakes(List<Earthquake>earthquakes);

    @Delete
    public void deleteEarthquake(Earthquake earthquake);

    @Query("SELECT * FROM earthquake ORDER BY mMag DESC")
    public LiveData<List<Earthquake>> loadAllEarthquakes();


    // This query is run synchronously unlike the above LiveData query. Use this one for checking
    // for new biggest earthquakes when already on a background thread.

    @Query("SELECT * FROM earthquake ORDER BY mMag DESC")
    List<Earthquake> loadAllEarthquakesBlocking();
}

