package net.seismos.android.seismos.data.model;

public class Offer {

    private String photo;
    private String squarePhoto;
    private String title;
    private String subtitle;
    private int price;
    private String details;
    private String creator;

    public String getSquarePhoto() {
        return squarePhoto;
    }

    public void setSquarePhoto(String squarePhoto) {
        this.squarePhoto = squarePhoto;
    }

    private String available;
    private String Id;
    private String key;

    public boolean isBought() {
        return isBought;
    }

    public void setBought(boolean bought) {
        isBought = bought;
    }

    private boolean isBought;

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setAvailable(String available) {
        this.available = available;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getKey() {
        return key;
    }

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
