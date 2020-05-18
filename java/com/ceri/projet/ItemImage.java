package com.ceri.projet;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemImage implements Parcelable {
    public static final String NO_PICTURES_IMAGE = "https://i.imgur.com/sxH2K8c.jpg";
    public static final String NO_PICTURES_DESC = "Aucune photo n'est disponible.";

    private String description;
    private String imageUrl;

    ItemImage(String description, String imageUrl)

    {
        this.description = description;
        this.imageUrl = imageUrl;
    }

    protected ItemImage(Parcel in) {
        description = in.readString();
        imageUrl = in.readString();
    }

    public static final Creator<ItemImage> CREATOR = new Creator<ItemImage>() {
        @Override
        public ItemImage createFromParcel(Parcel in) {
            return new ItemImage(in);
        }

        @Override
        public ItemImage[] newArray(int size) {
            return new ItemImage[size];
        }
    };

    public String getDescription() {
        return this.description;
    }

    public String getImageUrl() {
        return this.imageUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeString(this.imageUrl);
    }
}
