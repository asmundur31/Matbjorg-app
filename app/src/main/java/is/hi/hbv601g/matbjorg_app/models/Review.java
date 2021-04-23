package is.hi.hbv601g.matbjorg_app.models;

public class Review {
    private long id;
    private String review;
    private double rating;
    private Seller seller;
    private Buyer buyer;

    public Review(long id, String review, double rating, Seller seller, Buyer buyer) {
        this.id = id;
        this.review = review;
        this.rating = rating;
        this.seller = seller;
        this.buyer = buyer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    public Buyer getBuyer() {
        return buyer;
    }

    public void setBuyer(Buyer buyer) {
        this.buyer = buyer;
    }
}
