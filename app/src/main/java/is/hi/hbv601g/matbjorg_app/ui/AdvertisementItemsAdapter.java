package is.hi.hbv601g.matbjorg_app.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;

public class AdvertisementItemsAdapter extends RecyclerView.Adapter<AdvertisementItemsAdapter.ViewHolder> {
    private static final String TAG = "AdvertisementItemsAdapter";

    private ArrayList<String> mNames = new ArrayList<>();
    // private ArrayList<String> mSellers = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<Double> mCurrentAmount = new ArrayList<>();
    private ArrayList<Double> mPrice = new ArrayList<>();
    private ArrayList<LocalDateTime> mExpireDate = new ArrayList<>();
    private Context context;
    private OnAdListener mOnAdListener;

    public AdvertisementItemsAdapter(List<Advertisement> ads, Context context, OnAdListener onAdListener) {
        for (int i=0; i<ads.size(); i++) {
            this.mNames.add(ads.get(i).getName());
            // this.mSellers.add(ads.get(i).getSellerName());
            this.mDescription.add(ads.get(i).getDescription());
            this.mCurrentAmount.add(ads.get(i).getCurrentAmount());
            this.mPrice.add(ads.get(i).getPrice());
            this.mExpireDate.add(ads.get(i).getExpireDate());
        }
        this.context = context;
        this.mOnAdListener = onAdListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_advertisment_item, parent, false);
        ViewHolder holder = new ViewHolder(view, mOnAdListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemName.setText(mNames.get(position));
        // holder.itemSeller.setText("Söluaðili: " + mSellers.get(position));
        holder.itemDescription.setText("Lýsing: " + mDescription.get(position));
        holder.itemCurrentAmount.setText("Magn: " + mCurrentAmount.get(position).toString());
        holder.itemPrice.setText("Verð: " + mPrice.get(position).toString());
        holder.itemExpireDate.setText("Gildir til: " + mExpireDate.get(position));
    }

    @Override
    public int getItemCount() {
        return mNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // ATH mynd!
        TextView itemName;
        // TextView itemSeller;
        TextView itemDescription;
        TextView itemCurrentAmount;
        TextView itemPrice;
        TextView itemExpireDate;
        OnAdListener onAdListener;

        public ViewHolder(@NonNull View itemView, OnAdListener onAdListener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.advertisement_item_name);
            // itemSeller = itemView.findViewById(R.id.basket_item_seller);
            itemDescription = itemView.findViewById(R.id.advertisement_item_description);
            itemCurrentAmount = itemView.findViewById(R.id.advertisement_item_current_amount);
            itemPrice = itemView.findViewById(R.id.advertisement_item_price);
            itemExpireDate = itemView.findViewById(R.id.advertisement_item_expire_date);
            this.onAdListener = onAdListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAdListener.onAdClick(getAdapterPosition());
        }
    }

    public interface OnAdListener {
        void onAdClick(int position);
    }
}
