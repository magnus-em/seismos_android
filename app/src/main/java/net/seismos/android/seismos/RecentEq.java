package net.seismos.android.seismos;

public class  RecentEq {
    private String mag;
    private String place;
    private String time;
    private String title;
    private float depth;
    private float Lat;
    private float Long;

    @Override

    public String toString() {
        return title;
    }

    public String getMag() {
        return mag;
    }

    public void setMag(String mag) {
        this.mag = mag;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public float getDepth() {
        return depth;
    }

    public void setDepth(float depth) {
        this.depth = depth;
    }

    public float getLat() {
        return Lat;
    }

    public void setLat(float lat) {
        Lat = lat;
    }

    public float getLong() {
        return Long;
    }

    public void setLong(float aLong) {
        Long = aLong;
    }
}
