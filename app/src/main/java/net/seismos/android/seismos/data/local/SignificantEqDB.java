package net.seismos.android.seismos.data.local;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;

import net.seismos.android.seismos.data.model.Earthquake;

@Database(entities = {Earthquake.class}, version = 1)
@TypeConverters({EarthquakeTypeConverters.class})
public abstract class SignificantEqDB extends RoomDatabase {
    public abstract EarthquakeDAO earthquakeDAO();
}
