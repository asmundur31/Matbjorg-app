package is.hi.hbv601g.matbjorg_app.ui;

import android.content.Context;
import android.util.Log;
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
import is.hi.hbv601g.matbjorg_app.models.OrderItem;

public class BasketItemAdapter extends RecyclerView.Adapter<BasketItemAdapter.ViewHolder> {
    private static final String TAG = "BasketItemAdapter";

    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<String> mSellers = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<Double> mAmount = new ArrayList<>();
    private ArrayList<Double> mPrice = new ArrayList<>();
    private ArrayList<String> mExpireDate = new ArrayList<>();
    private Context context;

    public BasketItemAdapter(List<OrderItem> items, Context context) {
        for (int i=0; i<items.size(); i++) {
            this.mTitles.add(items.get(i).getAdvertisement().getName());
            this.mSellers.add(items.get(i).getAdvertisement().getSellerName());
            this.mDescription.add(items.get(i).getAdvertisement().getDescription());
            this.mAmount.add(items.get(i).getAmount());
            this.mPrice.add(items.get(i).getAdvertisement().getPrice());
            this.mExpireDate.add(items.get(i).getAdvertisement().getExpireDate());
        }
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_basket_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTitle.setText(mTitles.get(position));
        holder.itemSeller.setText("Söluaðili: " + mSellers.get(position));
        holder.itemDescription.setText("Lýsing: " + mDescription.get(position));
        holder.itemAmount.setText("Magn: " + mAmount.get(position).toString());
        holder.itemPrice.setText("Verð: " + mPrice.get(position).toString());
        holder.itemExpireDate.setText("Gildir til: " + mExpireDate.get(position));
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Ég sleppi myndum í bili
        TextView itemTitle;
        TextView itemSeller;
        TextView itemDescription;
        TextView itemAmount;
        TextView itemPrice;
        TextView itemExpireDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.basket_item_title);
            itemSeller = itemView.findViewById(R.id.basket_item_seller);
            itemDescription = itemView.findViewById(R.id.basket_item_description);
            itemAmount = itemView.findViewById(R.id.basket_item_amount);
            itemPrice = itemView.findViewById(R.id.basket_item_price);
            itemExpireDate = itemView.findViewById(R.id.basket_item_expire_date);
        }
    }
}
