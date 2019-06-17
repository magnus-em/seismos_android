package net.seismos.android.seismos.data.local;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import net.seismos.android.seismos.data.model.Earthquake;

@Database(entities = {Earthquake.class}, version = 1)
@TypeConverters({EarthquakeTypeConverters.class})
public abstract class DayUpdateDB extends RoomDatabase {
    public abstract EarthquakeDAO earthquakeDAO();
}
