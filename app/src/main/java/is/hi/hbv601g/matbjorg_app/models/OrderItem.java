package is.hi.hbv601g.matbjorg_app.models;

public class OrderItem {
    private long id;
    private long advertisementId;
    private double amount;

    public OrderItem(long id, long advertisementId, double amount) {
        this.id = id;
        this.advertisementId = advertisementId;
        this.amount = amount;
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public long getAdvertisementId() {

        return advertisementId;
    }

    public void setAdvertisementId(long advertisementId) {

        this.advertisementId = advertisementId;
    }

    public double getAmount() {

        return amount;
    }

    public void setAmount(double amount) {

        this.amount = amount;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", advertisementId=" + advertisementId +
                ", amount=" + amount +
                '}';
    }
}
