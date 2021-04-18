package is.hi.hbv601g.matbjorg_app.ui;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.ui.profile.ProfileFragment;

public class SellerAdsAdapter extends RecyclerView.Adapter<SellerAdsAdapter.ViewHolder> {
    private static final String TAG = "SellerAdsAdapter";
    private ArrayList<Advertisement> mSellerAds = new ArrayList<>();
    private Context mContext;

    public SellerAdsAdapter(ArrayList<Advertisement> sellerAds, Context context) {
        this.mSellerAds = sellerAds;
        mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_seller_ads, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d(TAG, "onBindViewHolder: called");
        holder.name.setText("Heiti : " + mSellerAds.get(position).getName());
        holder.description.setText("Lýsing : " + mSellerAds.get(position).getDescription());
        holder.originalAmount.setText("Upphaflegt magn : " + mSellerAds.get(position).getOriginalAmount());
        holder.currentAmount.setText("Eftirstöðvar : " + mSellerAds.get(position).getCurrentAmount());
        holder.price.setText("Verð : " + mSellerAds.get(position).getPrice());
        holder.expireDate.setText("Síðasti söludagur : " + mSellerAds.get(position).getExpireDate().toString());
        holder.editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: clicked on:" + mSellerAds.get(position).getName());
                Toast.makeText(mContext, mSellerAds.get(position).getName(), Toast.LENGTH_SHORT).show();
            }
        });
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

    public class ViewHolder extends RecyclerView.ViewHolder{

        RelativeLayout parentLayout;
        ImageView image;
        TextView name;
        TextView description;
        TextView originalAmount;
        TextView currentAmount;
        TextView price;
        TextView expireDate;
        Button editButton;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.seller_ad_image);
            name =  itemView.findViewById(R.id.seller_ad_name);
            description = itemView.findViewById(R.id.seller_ad_description);
            originalAmount = itemView.findViewById(R.id.seller_ad_original_amount);
            currentAmount = itemView.findViewById(R.id.seller_ad_current_amount);
            price = itemView.findViewById(R.id.seller_ad_expire_date);
            expireDate = itemView.findViewById(R.id.seller_ad_expire_date);
            editButton = itemView.findViewById(R.id.seller_ad_edit_button);
        }
    }
}
