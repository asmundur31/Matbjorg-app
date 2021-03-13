package is.hi.hbv601g.matbjorg_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class AdvertisementsActivity extends AppCompatActivity {
    private static final String TAG = "AdvertisementsActivity";
    private TextView mAdvertisementsTitle;
    private RecyclerView mAdvertisementItems;

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
                AdvertisementItemsAdapter adapter = new AdvertisementItemsAdapter(ads, AdvertisementsActivity.this);
                mAdvertisementItems.setAdapter(adapter);
                mAdvertisementItems.setLayoutManager(new LinearLayoutManager(AdvertisementsActivity.this));
            }
        }
        );
    }
}