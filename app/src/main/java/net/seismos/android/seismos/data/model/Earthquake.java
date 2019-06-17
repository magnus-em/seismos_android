package net.seismos.android.seismos.data.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import android.location.Location;
import androidx.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@Entity
public class Earthquake {

    // The field to use as the primary key to identify earthquake entities in the db table
    @NonNull
    @PrimaryKey
    private String mId;

    private String mPlace;
    private String mTitle;
    private long mTime;
    private long mUpdated;
    private int mTz;
    private String mUrl;
    private Date mDate; // Complex object that needs a type converter for Room
    private String mDetail;
    private int mFelt;
    private double mCdi;
    private double mMmi;
    private String mAlert;
    private String mStatus;
    private int mTsunami;
    private int mSig;
    private String mNet;
    private String mCode;
    private String mIds;
    private String mSources;
    private String mTypes;
    private int mNst;
    private double mDmin;
    private double mRms;
    private double mGap;
    private String mMagType;
    private String mType;

    private double mLongitude;
    private double mLatitude;
    private double mDepth;


    private Location mLocation; // Complex object that needs a type converter for Room
    private double mMag;
    private String mLink;


    @NonNull
    public String getId() { return mId; }

    public void setId(@NonNull String mId) { this.mId = mId; }

    public String getPlace() { return mPlace; }

    public void setPlace(String mPlace) { this.mPlace = mPlace; }

    public String getTitle() { return mTitle; }

    public void setTitle(String mTitle) { this.mTitle = mTitle; }

    public long getTime() { return mTime; }

    public void setTime(long mTime) { this.mTime = mTime; }

    public long getUpdated() { return mUpdated; }

    public void setUpdated(long mUpdated) { this.mUpdated = mUpdated; }

    public int getTz() { return mTz; }

    public void setTz(int mTz) { this.mTz = mTz; }

    public String getUrl() { return mUrl; }

    public void setUrl(String mUrl) { this.mUrl = mUrl; }

    public Date getDate() { return mDate; }

    public void setDate(Date mDate) { this.mDate = mDate; }

    public String getDetail() { return mDetail; }

    public void setDetail(String mDetail) { this.mDetail = mDetail;  }

    public int getFelt() { return mFelt; }

    public void setFelt(int mFelt) { this.mFelt = mFelt; }

    public double getCdi() { return mCdi; }

    public void setCdi(double mCdi) { this.mCdi = mCdi; }

    public double getMmi() { return mMmi; }

    public void setMmi(double mMmi) { this.mMmi = mMmi; }

    public String getAlert() { return mAlert; }

    public void setAlert(String mAlert) { this.mAlert = mAlert; }

    public String getStatus() { return mStatus; }

    public void setStatus(String mStatus) { this.mStatus = mStatus; }

    public int getTsunami() { return mTsunami; }

    public void setTsunami(int mTsunami) { this.mTsunami = mTsunami; }

    public int getSig() { return mSig; }

    public void setSig(int mSig) { this.mSig = mSig; }

    public String getNet() { return mNet; }

    public void setNet(String mNet) { this.mNet = mNet; }

    public String getCode() { return mCode; }

    public void setCode(String mCode) { this.mCode = mCode; }

    public String getIds() { return mIds; }

    public void setIds(String mIds) {
        this.mIds = mIds;
    }

    public String getSources() {
        return mSources;
    }

    public void setSources(String mSources) {
        this.mSources = mSources;
    }

    public String getTypes() {
        return mTypes;
    }

    public void setTypes(String mTypes) {
        this.mTypes = mTypes;
    }

    public int getNst() {
        return mNst;
    }

    public void setNst(int mNst) {
        this.mNst = mNst;
    }

    public double getDmin() {
        return mDmin;
    }

    public void setDmin(double mDmin) {
        this.mDmin = mDmin;
    }

    public double getRms() {
        return mRms;
    }

    public void setRms(double mRms) {
        this.mRms = mRms;
    }

    public double getGap() {
        return mGap;
    }

    public void setGap(double mGap) {
        this.mGap = mGap;
    }

    public String getMagType() {
        return mMagType;
    }

    public void setMagType(String mMagType) {
        this.mMagType = mMagType;
    }

    public String getType() {
        return mType;
    }

    public void setType(String mType) {
        this.mType = mType;
    }

    public double getLongitude() {
        return mLongitude;
    }

    public void setLongitude(double mLongitude) {
        this.mLongitude = mLongitude;
    }

    public double getLatitude() {
        return mLatitude;
    }

    public void setLatitude(double mLatitude) {
        this.mLatitude = mLatitude;
    }

    public double getDepth() {
        return mDepth;
    }

    public void setDepth(double mDepth) {
        this.mDepth = mDepth;
    }

    public Location getLocation() {
        return mLocation;
    }

    public void setLocation(Location mLocation) {
        this.mLocation = mLocation;
    }

    public double getMag() {
        return mMag;
    }

    public void setMag(double mMag) {
        this.mMag = mMag;
    }

    public String getLink() {
        return mLink;
    }

    public void setLink(String mLink) {
        this.mLink = mLink;
    }


    public String getDetails() { return mDetail; }

    public double getMagnitude()  { return mMag; }



    public Earthquake() {
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH.mm", Locale.US);
        String dateString = sdf.format(mDate);
        return dateString + ": " + mMag + " " + mDetail;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Earthquake)
            return (((Earthquake)obj).getId().contentEquals(mId));
        else
            return false;
    }

}
