package net.seismos.android.seismos.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

@Database(entities = {Earthquake.class}, version = 1)
@TypeConverters({EarthquakeTypeConverters.class})
public abstract class EarthquakeDatabase extends RoomDatabase {
    public abstract EarthquakeDAO earthquakeDAO();
}
