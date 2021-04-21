package is.hi.hbv601g.matbjorg_app.ui;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.OrderItem;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.ViewHolder> {
    private static final String TAG = "OrderItemAdapter";

    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<String> mSellers = new ArrayList<>();
    private ArrayList<String> mDescription = new ArrayList<>();
    private ArrayList<Double> mPrice = new ArrayList<>();
    private Context context;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public OrderItemAdapter(List<OrderItem> items, Context context) {
        for (int i=0; i<items.size(); i++) {
            this.mTitles.add(items.get(i).getAdvertisement().getName());
            this.mSellers.add(items.get(i).getAdvertisement().getSellerName());
            this.mDescription.add(items.get(i).getAdvertisement().getDescription());
            this.mPrice.add(items.get(i).getAdvertisement().getPrice());
        }
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_order_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTitle.setText(mTitles.get(position));
        holder.itemSeller.setText("Söluaðili: " + mSellers.get(position));
        holder.itemDescription.setText("Lýsing: " + mDescription.get(position));
        holder.itemPrice.setText("Verð: " + mPrice.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return mTitles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemTitle;
        TextView itemSeller;
        TextView itemDescription;
        TextView itemPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.order_item_title);
            itemSeller = itemView.findViewById(R.id.order_item_seller);
            itemDescription = itemView.findViewById(R.id.order_item_description);
            itemPrice = itemView.findViewById(R.id.order_item_price);
        }
    }
}

