package net.seismos.android.seismos.data;

import android.arch.persistence.room.TypeConverter;
import android.location.Location;

import java.util.Date;

public class EarthquakeTypeConverters {
    @TypeConverter
    public static Date dateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimeStamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String locationToString(Location location) {
        return location == null ?
                null : location.getLatitude() + "," + location.getLongitude();
    }

    @TypeConverter
    public static Location locationFromString(String locString) {
        Location location = new Location("Generated");
        if (locString != null && locString.contains(",")) {
            String[] parsed = locString.split(",");
            if (parsed.length == 2) {
                location.setLatitude(Double.parseDouble(parsed[0]));
                location.setLongitude(Double.parseDouble(parsed[1]));
                return location;
            } else return null;
        } else return null;
    }
}
