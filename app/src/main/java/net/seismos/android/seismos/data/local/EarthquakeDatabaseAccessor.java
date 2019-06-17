package net.seismos.android.seismos.data.local;


import androidx.room.Room;
import android.content.Context;

// Singleton with a private constructor that prohibits external instantiation. You shouldn't
// ever need to instantiate this class because the getInstance method is static
public class EarthquakeDatabaseAccessor {
    private static EarthquakeDatabase EarthquakeDatabaseInstance;
    private static final String EARTHQUAKE_DB_NAME = "earthquake_db";

    private EarthquakeDatabaseAccessor() {}

    public static EarthquakeDatabase getInstance(Context context) {
        if (EarthquakeDatabaseInstance == null) {
            // Create or open a new SQLite database, and return it as a Room db instance
            EarthquakeDatabaseInstance = Room.databaseBuilder(context,
                    EarthquakeDatabase.class, EARTHQUAKE_DB_NAME).build();
        }
        return EarthquakeDatabaseInstance;
    }
}

