package com.example.mobile_assignment;

import android.os.Parcel;
import android.os.Parcelable;

//class for data we want from the books
public class BookData implements Parcelable
{
    public String id;
    public String title;
    public String subtitle;
    public String[] authors;
    public String publisher;
    public String date;
    public String description;

    public BookData(String id, String title, String subtitle, String[] authors, String publisher, String date, String description)
    {
        this.id = id;
        this.title = title;
        this. subtitle = subtitle;
        this.authors = authors;
        this.publisher = publisher;
        this.date = date;
        this.description = description;
    }

    protected BookData(Parcel in) {
        id = in.readString();
        title = in.readString();
        subtitle = in.readString();
        authors = in.createStringArray();
        publisher = in.readString();
        date = in.readString();
        description = in.readString();
    }
    //parcelable code for fast transfer of object information between activities
    public static final Creator<BookData> CREATOR = new Creator<BookData>() {
        @Override
        public BookData createFromParcel(Parcel in) {
            return new BookData(in);
        }

        @Override
        public BookData[] newArray(int size) {
            return new BookData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(title);
        parcel.writeString(subtitle);
        parcel.writeStringArray(authors);
        parcel.writeString(publisher);
        parcel.writeString(date);
        parcel.writeString(description);
    }
}
