package is.hi.hbv601g.matbjorg_app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Location implements Parcelable, Comparable<Location> {
    private long id;
    private double longitude;
    private double latitude;
    private String name;

    public Location(double longitude, double latitude, String name) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.name = name;
    }

    protected Location(Parcel in) {
        id = in.readLong();
        longitude = in.readDouble();
        latitude = in.readDouble();
        name = in.readString();
    }

    public static final Creator<Location> CREATOR = new Creator<Location>() {
        @Override
        public Location createFromParcel(Parcel in) {
            return new Location(in);
        }

        @Override
        public Location[] newArray(int size) {
            return new Location[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeDouble(longitude);
        dest.writeDouble(latitude);
        dest.writeString(name);
    }

    @Override
    public int compareTo(Location b) {
        return this.name.compareTo(b.getName());
    }
}
