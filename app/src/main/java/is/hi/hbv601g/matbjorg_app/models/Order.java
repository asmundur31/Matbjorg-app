package is.hi.hbv601g.matbjorg_app.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Order {
    private long id;
    private List<OrderItem> items = new ArrayList<OrderItem>();
    private Buyer buyer;
    private boolean active = true;
    private LocalDateTime timeOfPurchase;
    private double totalPrice;

    public Order(long id, List<OrderItem> items, Buyer buyer, boolean active, LocalDateTime timeOfPurchase, double totalPrice) {
        this.id = id;
        this.items = items;
        this.buyer = buyer;
        this.active = active;
        this.timeOfPurchase = timeOfPurchase;
        this.totalPrice = totalPrice;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public void setItems(List<OrderItem> items) {
        this.items = items;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public LocalDateTime getTimeOfPurchase() {
        return timeOfPurchase;
    }

    public void setTimeOfPurchase(LocalDateTime timeOfPurchase) {
        this.timeOfPurchase = timeOfPurchase;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
