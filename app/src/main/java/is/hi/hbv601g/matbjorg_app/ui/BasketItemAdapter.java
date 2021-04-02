package is.hi.hbv601g.matbjorg_app.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
    private ArrayList<LocalDateTime> mExpireDate = new ArrayList<>();
    private Context context;
    private OnItemListener listener;

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

    public void setOnItemListener(OnItemListener listener) {
        this.listener = listener;
    }

    public void updateAmount(int position, double amount) {
        if (amount <= 0) {
            // Þá vil ég eyða itemi með index position
            mTitles.remove(position);
            mSellers.remove(position);
            mDescription.remove(position);
            mAmount.remove(position);
            mPrice.remove(position);
            mExpireDate.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mTitles.size());
        } else {
            mAmount.set(position, amount);
            notifyItemChanged(position);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_basket_item, parent, false);
        ViewHolder holder = new ViewHolder(view, listener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemTitle.setText(mTitles.get(position));
        holder.itemSeller.setText("Söluaðili: " + mSellers.get(position));
        holder.itemDescription.setText("Lýsing: " + mDescription.get(position));
        int amount = mAmount.get(position).intValue();
        holder.itemAmount.setText(amount+"");
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
        ImageView deleteItem;
        Button changeAmount;
        OnItemListener listener;

        public ViewHolder(@NonNull View itemView, OnItemListener listener) {
            super(itemView);
            itemTitle = itemView.findViewById(R.id.basket_item_title);
            itemSeller = itemView.findViewById(R.id.basket_item_seller);
            itemDescription = itemView.findViewById(R.id.basket_item_description);
            itemAmount = itemView.findViewById(R.id.basket_item_amount);
            itemPrice = itemView.findViewById(R.id.basket_item_price);
            itemExpireDate = itemView.findViewById(R.id.basket_item_expire_date);
            deleteItem = itemView.findViewById(R.id.delete_item);
            changeAmount = itemView.findViewById(R.id.change_amount);
            this.listener = listener;

            deleteItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });

            changeAmount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        double newAmount = 0;
                        try {
                            newAmount = Double.parseDouble(itemAmount.getText().toString());
                        } catch (NumberFormatException e) {
                            // Förum hingað ef form er tómt, g.r.f. að notani vilji eyða
                            newAmount = 0;
                            e.printStackTrace();
                        }
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onChangeAmountClick(position, newAmount);
                        }
                    }
                }
            });
        }
    }

    public interface OnItemListener {
        void onChangeAmountClick(int position, double newAmount);
        void onDeleteClick(int position);
    }
}
