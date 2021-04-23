package is.hi.hbv601g.matbjorg_app.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.ui.profile.ProfileFragment;

import static is.hi.hbv601g.matbjorg_app.network.NetworkController.URL_REST;

public class SellerAdsAdapter extends RecyclerView.Adapter<SellerAdsAdapter.ViewHolder> {
    private static final String TAG = "SellerAdsAdapter";
    private ArrayList<Advertisement> mSellerAds = new ArrayList<>();
    private Context mContext;
    private OnAdChangeListener onAdChangeListener;


    public SellerAdsAdapter(ArrayList<Advertisement> sellerAds, Context context, OnAdChangeListener onAdChangeListener) {
        this.mSellerAds = sellerAds;
        mContext = context;
        this.onAdChangeListener = onAdChangeListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_seller_ads, parent, false);
        ViewHolder viewHolder = new ViewHolder(view, onAdChangeListener);
        return viewHolder;
    }

    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        String url = URL_REST + "advertisements/image/" + mSellerAds.get(position).getPictureName();
        Picasso.get().load(url).resize(250, 250).centerCrop().placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.fastfood_100).into(holder.image);
        holder.name.setText("Heiti : " + mSellerAds.get(position).getName());
        holder.description.setText("Lýsing : " + mSellerAds.get(position).getDescription());
        holder.originalAmount.setText("Upphaflegt magn : " + mSellerAds.get(position).getOriginalAmount());
        holder.currentAmount.setText("Eftirstöðvar : " + mSellerAds.get(position).getCurrentAmount());
        holder.price.setText("Verð : " + mSellerAds.get(position).getPrice());
        holder.expireDate.setText("Síðasti söludagur : " + mSellerAds.get(position).getExpireDate().toString());
        if(!mSellerAds.get(position).isActive()){
            holder.editButton.setVisibility(View.GONE);
            holder.itemView.setBackgroundResource(R.drawable.boarder_grey);
        }
        else {
            holder.editButton.setVisibility(View.VISIBLE);
            holder.itemView.setBackgroundResource(R.drawable.border);
        }
    }

    @Override
    public int getItemCount() {
        return mSellerAds.size();
    }

    public void setAdvertisements(List<Advertisement> advertisements) {
        mSellerAds.clear();
        mSellerAds.addAll(advertisements);
        notifyDataSetChanged();
    }

    public Advertisement getAd(int position) {
        return mSellerAds.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        RelativeLayout parentLayout;
        ImageView image;
        TextView name;
        TextView description;
        TextView originalAmount;
        TextView currentAmount;
        TextView price;
        TextView expireDate;
        Button editButton;
        OnAdChangeListener onAdChangeListener;

        public ViewHolder(@NonNull View itemView, OnAdChangeListener onAdChangeListener) {
            super(itemView);
            image = itemView.findViewById(R.id.seller_ad_image);
            name =  itemView.findViewById(R.id.seller_ad_name);
            description = itemView.findViewById(R.id.seller_ad_description);
            originalAmount = itemView.findViewById(R.id.seller_ad_original_amount);
            currentAmount = itemView.findViewById(R.id.seller_ad_current_amount);
            price = itemView.findViewById(R.id.seller_ad_price);
            expireDate = itemView.findViewById(R.id.seller_ad_expire_date);
            editButton = itemView.findViewById(R.id.seller_ad_edit_button);
            this.onAdChangeListener = onAdChangeListener;
            editButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onAdChangeListener.onAdChangeClick(getAdapterPosition());
        }
    }

    public interface OnAdChangeListener {
        void onAdChangeClick(int position);
    }
}
