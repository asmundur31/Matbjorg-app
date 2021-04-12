package is.hi.hbv601g.matbjorg_app.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
    private String token;

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

        // Sækjum token hjá innskráðum notanda
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "");

        NetworkController networkController = new NetworkController(this);
        networkController.getAdvertisements(new NetworkCallback<List<Advertisement>>() {
            @Override
            public void onError(String error) {
                Toast.makeText(AdvertisementsActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(List<Advertisement> ads) {
                mAds = ads;
                AdvertisementItemsAdapter adapter = new AdvertisementItemsAdapter(mAds, AdvertisementsActivity.this, AdvertisementsActivity.this);
                mAdvertisementItems.setAdapter(adapter);
                mAdvertisementItems.setLayoutManager(new LinearLayoutManager(AdvertisementsActivity.this));
            }
          }
        );
    }

    @Override
    public void onAdClick(int position) {
        Log.d(TAG, token);
        if (!token.isEmpty()) {
            Intent intent = AdvertisementActivity.newIntent(AdvertisementsActivity.this);
            intent.putExtra("selected_ad", mAds.get(position));
            intent.putExtra("token", token);
            startActivityForResult(intent, REQUEST_CODE_AD);
        }
    }
}