package net.seismos.android.seismos.data.model;

public class User {

    private String id;
    private String photo;
    private String name;
    private String phone;
    private String email;
    private String provider;
    private int balance;
    private int earnedToday;
    private int earnedTotal;

    public int getEarnedToday() {
        return earnedToday;
    }

    public void setEarnedToday(int earnedToday) {
        this.earnedToday = earnedToday;
    }

    public int getEarnedTotal() {
        return earnedTotal;
    }

    public void setEarnedTotal(int earnedTotal) {
        this.earnedTotal = earnedTotal;
    }

    public int getBalance() {
        return balance;
    }

    public void setBalance(int balance) {
        this.balance = balance;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
