package is.hi.hbv601g.matbjorg_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;

public class AdvertisementActivity extends AppCompatActivity {
    private static final String TAG = "AdvertisementActivity";

    private TextView mName;
    private TextView mPrice;
    private TextView mDescription;
    private TextView mCurrentAmount;
    private TextView mExpireDate;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AdvertisementActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        mName = (TextView) findViewById(R.id.ad_name);
        mPrice = (TextView) findViewById(R.id.ad_price);
        mDescription = (TextView) findViewById(R.id.ad_description);
        mCurrentAmount = (TextView) findViewById(R.id.ad_current_amount);
        mExpireDate = (TextView) findViewById(R.id.ad_expire_date);

        if(getIntent().hasExtra("selected_ad")) {
            Advertisement advertisement = getIntent().getParcelableExtra("selected_ad");
            // Hér er aðgengi að Location, Tags og búið er að laga að expireDate skili null
            // TODO: Laga útlit og birta kannski location og tags líka
            mName.setText(advertisement.getName());
            mPrice.setText("Verð: " + advertisement.getPrice());
            mDescription.setText("Lýsing: " + advertisement.getDescription());
            mCurrentAmount.setText("Magn: " + advertisement.getCurrentAmount());
            mExpireDate.setText("Gildir til: " + advertisement.getExpireDate());
        }

    }
}