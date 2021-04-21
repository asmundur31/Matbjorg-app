package is.hi.hbv601g.matbjorg_app.ui;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.models.Tag;

import static is.hi.hbv601g.matbjorg_app.network.NetworkController.URL_REST;

public class AdvertisementItemsAdapter extends RecyclerView.Adapter<AdvertisementItemsAdapter.ViewHolder> {
    private static final String TAG = "AdvertisementItemsAdapter";
    
    private List<Advertisement> mAdvertisements = new ArrayList<>();
    // advertisementArrayList inniheldur allar auglýsingar
    private ArrayList<Advertisement> advertisementArrayList = new ArrayList<>();
    private Context context;
    private OnAdListener mOnAdListener;

    public AdvertisementItemsAdapter(List<Advertisement> ads, Context context, OnAdListener onAdListener) {
        this.advertisementArrayList.addAll(ads);
        this.mAdvertisements = ads;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String url = URL_REST + "advertisements/image/" + mAdvertisements.get(position).getPictureName();
        //Drawable image = LoadImageFromWebOperations(url);
        //holder.itemImage.setImageDrawable(image);
        //holder.itemImage.setImageBitmap(BitmapFactory.decodeFile(mAdvertisements.get(position).getPictureName()));
        Picasso.get().load(url).resize(250, 250).centerCrop().placeholder(R.drawable.ic_launcher_foreground).into(holder.itemImage);
        holder.itemName.setText(mAdvertisements.get(position).getName());
        holder.itemSeller.setText("Söluaðili: " + mAdvertisements.get(position).getSellerName());
        // holder.itemDescription.setText("Lýsing: " + mAdvertisements.get(position).getDescription());
        holder.itemCurrentAmount.setText("Magn: " + mAdvertisements.get(position).getCurrentAmount());
        holder.itemPrice.setText("Verð: " + mAdvertisements.get(position).getPrice());
        holder.itemExpireDate.setText("Gildir til: " + mAdvertisements.get(position).getExpireDate());
    }

    public static Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is, "src name");
            return d;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public int getItemCount() {
        return mAdvertisements.size();
    }

    /**
     * Fall sem filterar auglýsingar eftir streng, vöruflokk og söluaðila
     * @param charText strengur sem leitað er eftir
     * @param categories Allir vöruflokkar
     * @param categoryIndecies indecies á valda vöruflokka
     * @param sellers Allir sellers
     * @param sellerIndecies indecies á valda söluaðila
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void filter(String charText, String[] categories, ArrayList<Integer> categoryIndecies, String[] sellers, ArrayList<Integer> sellerIndecies) {
        // Ef ekkert er skrifað í charText setjum við tómastrenginn til að losan við null point villur
        if(charText == null) {
            charText = "";
        } else {
            charText = charText.toLowerCase(Locale.getDefault());
        }
        // Ef categoryIndecies og sellerIndecies eru tóm þá jafngildir það að allt sé valið
        ArrayList<String> chosenTags = new ArrayList<>();
        if (categoryIndecies.size()==0) {
            String[] tags = Tag.getTags();
            for (int i=0; i<tags.length; i++) {
                chosenTags.add(tags[i]);
            }
        } else {
            for (int i=0; i<categoryIndecies.size(); i++) {
                chosenTags.add(categories[categoryIndecies.get(i)]);
            }
        }
        ArrayList<String> chosenSellers = new ArrayList<>();
        if (sellerIndecies.size()==0) {
            for (Advertisement ad : advertisementArrayList) {
                if (!chosenSellers.contains(ad.getSellerName())) chosenSellers.add(ad.getSellerName());
            }
        } else {
            for (int i=0; i<sellerIndecies.size(); i++) {
                chosenSellers.add(sellers[sellerIndecies.get(i)]);
            }
        }
        mAdvertisements.clear();
        // Ef texti er tómur, chosenTags er tómt og chosenSellers er tómt þá birtum við allt
        if (charText.length() == 0 && chosenTags.size() == 0 && chosenSellers.size() == 0) {
            mAdvertisements.addAll(advertisementArrayList);
        } else {
            for (Advertisement ad : advertisementArrayList) {
                // Fyrir hverja auglýsingu athugum við hvort charText sé í nafni auglýsingar og
                // hvort seller auglýsingu sé í chosenSeller
                if (ad.getName().toLowerCase(Locale.getDefault()).contains(charText) && chosenSellers.contains(ad.getSellerName())) {
                    // athugum fyrir hvert tag í auglýsingu hvort það sé í chosenTags
                    for (Tag tag : ad.getTags()) {
                        if (chosenTags.contains(tag.toString())) {
                            mAdvertisements.add(ad);
                            break;
                        }
                    }
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // ATH mynd!
        ImageView itemImage;
        TextView itemName;
        TextView itemSeller;
        // TextView itemDescription;
        TextView itemCurrentAmount;
        TextView itemPrice;
        TextView itemExpireDate;
        OnAdListener onAdListener;

        public ViewHolder(@NonNull View itemView, OnAdListener onAdListener) {
            super(itemView);
            itemImage = itemView.findViewById(R.id.advertisement_item_image);
            itemName = itemView.findViewById(R.id.advertisement_item_name);
            itemSeller = itemView.findViewById(R.id.advertisement_item_seller);
            //itemDescription = itemView.findViewById(R.id.advertisement_item_description);
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
