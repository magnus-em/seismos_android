package net.seismos.android.seismos.data.local;


import android.arch.persistence.room.Room;
import android.content.Context;

import net.seismos.android.seismos.data.local.EarthquakeDatabase;

// Singleton with a private constructor that prohibits external instantiation. You shouldn't
// ever need to instantiate this class because the getInstance method is static
public class DayUpdateDBAccessor {
    private static DayUpdateDB EarthquakeDatabaseInstance;
    private static final String EARTHQUAKE_DB_NAME = "day_update_earthquake_db";

    private DayUpdateDBAccessor() {}

    public static DayUpdateDB getInstance(Context context) {
        if (EarthquakeDatabaseInstance == null) {
            // Create or open a new SQLite database, and return it as a Room db instance
            EarthquakeDatabaseInstance = Room.databaseBuilder(context,
                    DayUpdateDB.class, EARTHQUAKE_DB_NAME).build();
        }
        return EarthquakeDatabaseInstance;
    }
}

