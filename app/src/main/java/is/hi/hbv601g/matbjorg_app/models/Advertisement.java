package is.hi.hbv601g.matbjorg_app.models;

import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.RequiresApi;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.O)
public class Advertisement implements Parcelable {
    private long id;
    private String name;
    private String sellerName;
    private String description;
    private boolean active = true;
    private double originalAmount;
    private double currentAmount;
    private double price;
    private LocalDateTime expireDate;
    private LocalDateTime createdAt;
    private Set<Tag> tags;
    private String pictureName;
    private Location location;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");


    public Advertisement(long id, String name, String sellerName, String description, boolean active, double originalAmount, double currentAmount, double price, LocalDateTime expireDate, LocalDateTime createdAt, Set<Tag> tags, String pictureName, Location location) {
        this.id = id;
        this.name = name;
        this.sellerName = sellerName;
        this.description = description;
        this.active = active;
        this.originalAmount = originalAmount;
        this.currentAmount = currentAmount;
        this.price = price;
        this.expireDate = expireDate;
        this.createdAt = createdAt;
        this.tags = tags;
        this.pictureName = pictureName;
        this.location = location;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    protected Advertisement(Parcel in) {
        id = in.readLong();
        name = in.readString();
        description = in.readString();
        active = in.readByte() != 0;
        originalAmount = in.readDouble();
        currentAmount = in.readDouble();
        price = in.readDouble();
        expireDate = LocalDateTime.parse(in.readString(), formatter);
        createdAt = LocalDateTime.parse(in.readString(), formatter);
        tags = (Set<Tag>) in.readSerializable();
        pictureName = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        sellerName = in.readString();
    }

    public static final Creator<Advertisement> CREATOR = new Creator<Advertisement>() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public Advertisement createFromParcel(Parcel in) {
            return new Advertisement(in);
        }

        @Override
        public Advertisement[] newArray(int size) {
            return new Advertisement[size];
        }
    };

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

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public double getOriginalAmount() {
        return originalAmount;
    }

    public void setOriginalAmount(double originalAmount) {
        this.originalAmount = originalAmount;
    }

    public double getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(double currentAmount) {
        this.currentAmount = currentAmount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public LocalDateTime getExpireDate() {
        return expireDate;
    }

    public void setExpireDate(LocalDateTime expireDate) {
        this.expireDate = expireDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    public void setTags(Set<Tag> tags) {
        this.tags = tags;
    }

    public String getPictureName() {
        return pictureName;
    }

    public void setPictureName(String pictureName) {
        this.pictureName = pictureName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeDouble(originalAmount);
        dest.writeDouble(currentAmount);
        dest.writeDouble(price);
        dest.writeString(String.valueOf(expireDate));
        dest.writeString(String.valueOf(createdAt));
        dest.writeSerializable((Serializable) tags);
        dest.writeString(pictureName);
        dest.writeParcelable(location, flags);
        dest.writeString(sellerName);
    }
}
