package net.seismos.android.seismos.data.model;



public class Article {

    private String title;
    private String publisher;
    private int entry;
    private String url;
    private Boolean big;
    private String photo;
    private String subtitle;
    private String date;

    public String getDate() {
        return date;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getPhoto() {
        return photo;
    }

    public String getTitle() {
        return title;
    }

    public String getPublisher() {
        return publisher;
    }

    public int getEntry() {
        return entry;
    }

    public String getUrl() {
        return url;
    }

    public Boolean getBig() {
        return big;
    }
}
