package is.hi.hbv601g.matbjorg_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Seller implements Comparable<Seller>, Parcelable {
    private long id;
    private String name;
    private String email;
    private List<Location> locations;

    public Seller() {
    }

    public Seller(long id, String name, String email, List<Location> locations) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.locations = locations;
    }

    protected Seller(Parcel in) {
        id = in.readLong();
        name = in.readString();
        email = in.readString();
        locations = in.createTypedArrayList(Location.CREATOR);
    }

    public static final Creator<Seller> CREATOR = new Creator<Seller>() {
        @Override
        public Seller createFromParcel(Parcel in) {
            return new Seller(in);
        }

        @Override
        public Seller[] newArray(int size) {
            return new Seller[size];
        }
    };

    @Override
    public String toString() {
        return "Seller{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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

    public List<Location> getLocations() {
        return locations;
    }

    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    @Override
    public int compareTo(Seller b) {
        return this.name.compareTo(b.getName());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeTypedList(locations);
    }
}
