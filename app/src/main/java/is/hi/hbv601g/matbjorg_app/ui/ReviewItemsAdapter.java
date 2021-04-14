package is.hi.hbv601g.matbjorg_app.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Review;

public class ReviewItemsAdapter extends RecyclerView.Adapter<ReviewItemsAdapter.ViewHolder> {
    private static final String TAG = "ReviewItemsAdapter";

    private List<Review> mReviews = new ArrayList<>();
    private Context context;

    public ReviewItemsAdapter(List<Review> reviews, Context context) {
        this.mReviews = reviews;
        this.context = context;
    }

    @NonNull
    @Override
    public ReviewItemsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_review_item, parent, false);
        ReviewItemsAdapter.ViewHolder holder = new ReviewItemsAdapter.ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewItemsAdapter.ViewHolder holder, int position) {
        holder.itemBuyerName.setText(mReviews.get(position).getBuyer().getName());
        holder.itemRating.setText("Einkunn: " + mReviews.get(position).getRating());
        holder.itemReview.setText(mReviews.get(position).getReview());
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void setReviews(List<Review> reviews) {
        mReviews.clear();
        mReviews.addAll(reviews);
        notifyDataSetChanged();
    }

    public void clearReviews() {
        mReviews.clear();
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemBuyerName;
        TextView itemRating;
        TextView itemReview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemBuyerName = itemView.findViewById(R.id.review_buyer_name);
            itemRating = itemView.findViewById(R.id.review_rating);
            itemReview = itemView.findViewById(R.id.review_text);
        }
    }
}
