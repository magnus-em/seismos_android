package net.seismos.android.seismos.data.local;


import android.arch.persistence.room.Room;
import android.content.Context;

import net.seismos.android.seismos.data.local.EarthquakeDatabase;

// Singleton with a private constructor that prohibits external instantiation. You shouldn't
// ever need to instantiate this class because the getInstance method is static
public class SignificantEqDBAccessor {
    private static SignificantEqDB EarthquakeDatabaseInstance;
    private static final String EARTHQUAKE_DB_NAME = "significant_earthquake_db";

    private SignificantEqDBAccessor() {}

    public static SignificantEqDB getInstance(Context context) {
        if (EarthquakeDatabaseInstance == null) {
            // Create or open a new SQLite database, and return it as a Room db instance
            EarthquakeDatabaseInstance = Room.databaseBuilder(context,
                    SignificantEqDB.class, EARTHQUAKE_DB_NAME).build();
        }
        return EarthquakeDatabaseInstance;
    }
}

