package is.hi.hbv601g.matbjorg_app.models;

public class OrderItem {
    private long id;
    private Advertisement advertisement;
    private double amount;

    public OrderItem(long id, Advertisement advertisement, double amount) {
        this.id = id;
        this.advertisement = advertisement;
        this.amount = amount;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Advertisement getAdvertisement() {
        return advertisement;
    }

    public void setAdvertisement(Advertisement advertisement) {
        this.advertisement = advertisement;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
