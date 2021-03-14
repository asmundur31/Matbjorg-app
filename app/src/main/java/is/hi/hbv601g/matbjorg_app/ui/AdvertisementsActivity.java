package is.hi.hbv601g.matbjorg_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class AdvertisementsActivity extends AppCompatActivity implements AdvertisementItemsAdapter.OnAdListener {
    private static final String TAG = "AdvertisementsActivity";
    private TextView mAdvertisementsTitle;
    private RecyclerView mAdvertisementItems;
    private LinearLayout mAdvertisementItem;
    private static final int REQUEST_CODE_AD = 0;
    private List<Advertisement> mAds;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AdvertisementsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisements);

        mAdvertisementsTitle = (TextView) findViewById(R.id.advertisements_title_view);
        mAdvertisementItems = (RecyclerView) findViewById(R.id.advertisement_items);
        mAdvertisementItem = (LinearLayout) findViewById(R.id.advertisement_item);

        //SharedPreferences sharedPref = getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
        // String token = sharedPref.getString("token", "");
        NetworkController networkController = new NetworkController(this);
        networkController.getAdvertisements(new NetworkCallback<List<Advertisement>>() {
            @Override
            public void onError(String error) {
                Toast.makeText(AdvertisementsActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<Advertisement> ads) {
                mAds = ads;
                AdvertisementItemsAdapter adapter = new AdvertisementItemsAdapter(mAds, AdvertisementsActivity.this, AdvertisementsActivity.this);
                mAdvertisementItems.setAdapter(adapter);
                mAdvertisementItems.setLayoutManager(new LinearLayoutManager(AdvertisementsActivity.this));
            }
        }
        );

        /* mAdvertisementItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdvertisementActivity.newIntent(AdvertisementsActivity.this);
                startActivityForResult(intent, REQUEST_CODE_AD);
            }
        });*/ 
    }

    @Override
    public void onAdClick(int position) {
        Intent intent = AdvertisementActivity.newIntent(AdvertisementsActivity.this);
        intent.putExtra("selected_ad", mAds.get(position));
        startActivityForResult(intent, REQUEST_CODE_AD);
    }
}