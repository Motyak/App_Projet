package com.ceri.projet;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Calendar;


public class Item implements Parcelable {

    public static final String TAG = Item.class.getSimpleName();

    private long id; // used for the _id column of the db helper

    private String webId; //used for web service
    private String name;
    private String thumbnail;
    private String brand;
    private int year;
    private ArrayList<Integer> timeFrame;
    private ArrayList<String> categories;
    private String desc;
    private ArrayList<ItemImage> pictures;
    private ArrayList<String> technicalDetails;
    private String lastUpdate;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getWebId() { return webId; }
    public void setWebId(String webId) { this.webId = webId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getThumbnail() { return this.thumbnail; }
    public void setThumbnail(String url) { this.thumbnail = url; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public int getYear() { return year; }
    public void setYear(int year) { this.year = year; }

    public ArrayList<Integer> getTimeFrame() { return timeFrame; }
    public void setTimeFrame(ArrayList<Integer> timeFrame) { this.timeFrame = timeFrame; }

    public ArrayList<String> getCategories() { return categories; }
    public void setCategories(ArrayList<String> categories) { this.categories = categories; }

    public String getDesc() { return desc; }
    public void setDesc(String desc) { this.desc = desc; }

    public ArrayList<ItemImage> getPictures() { return pictures; }
    public void setPictures(ArrayList<ItemImage> pictures) { this.pictures = pictures; }

    public ArrayList<String> getTechnicalDetails() { return technicalDetails; }
    public void setTechnicalDetails(ArrayList<String> technicalDetails) { this.technicalDetails = technicalDetails; }

    public String getLastUpdate() { return lastUpdate; }
    public void setLastUpdate() {
        Date currentTime = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MMM/yyyy HH:mm:ss");
        this.lastUpdate = dateFormat.format(currentTime);
    }

    public Item() {
        ;
    }

    public Item(long id, String webId, String name, String thumbnail, String brand, int year,
                ArrayList<Integer> timeFrame, ArrayList<String> categories, String desc,
                ArrayList<ItemImage> pictures, ArrayList<String> technicalDetails, String lastUpdate)
    {
        this.id = id;
        this.webId = webId;
        this.name = name;
        this.thumbnail = thumbnail;
        this.brand = brand;
        this.year = year;
        this.timeFrame = timeFrame;
        this.categories = categories;
        this.desc = desc;
        this.pictures = pictures;
        this.technicalDetails = technicalDetails;
        this.lastUpdate = lastUpdate;
    }

    @Override
    public String toString() {
        return this.name+", "+this.brand +" ("+this.year +")";
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        //verifier pour chacun que c'est pas null, sinon ca peut throw une exception probablement?

        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(thumbnail);
        dest.writeString(brand);
        dest.writeInt(year);
        dest.writeList(timeFrame);
        dest.writeList(categories);
        dest.writeString(desc);

        dest.writeTypedList(pictures);

        dest.writeList(technicalDetails);
        dest.writeString(lastUpdate);
    }


    public static final Creator<Item> CREATOR = new Creator<Item>()
    {
        @Override
        public Item createFromParcel(Parcel source)
        {
            return new Item(source);
        }

        @Override
        public Item[] newArray(int size)
        {
            return new Item[size];
        }
    };

    public Item(Parcel in) {
        this.id = in.readLong();
        this.name = in.readString();
        this.thumbnail = in.readString();
        this.brand = in.readString();
        this.year = in.readInt();
        this.timeFrame = in.readArrayList(Integer.class.getClassLoader());
        this.categories = in.readArrayList(String.class.getClassLoader());
        this.desc = in.readString();

        if(this.pictures == null) { this.pictures = new ArrayList<>(); }
        in.readTypedList(this.pictures, ItemImage.CREATOR);

        this.technicalDetails = in.readArrayList(String.class.getClassLoader());
        this.lastUpdate = in.readString();
    }


}
