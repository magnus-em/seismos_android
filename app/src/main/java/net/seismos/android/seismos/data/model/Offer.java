package net.seismos.android.seismos.data.model;

public class Offer {

    private String photo;
    private String title;
    private String subtitle;
    private int price;
    private String details;
    private String creator;
    private String available;
    private String Id;

    public void setId(String id) {
        Id = id;
    }

    public String getId() {
        return Id;
    }

    public String getPhoto() {
        return photo;
    }

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public int getPrice() {
        return price;
    }

    public String getDetails() {
        return details;
    }

    public String getCreator() {
        return creator;
    }

    public String getAvailable() {
        return available;
    }

}
