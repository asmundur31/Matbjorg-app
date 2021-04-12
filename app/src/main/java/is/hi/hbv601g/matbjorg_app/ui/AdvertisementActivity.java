package is.hi.hbv601g.matbjorg_app.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.models.OrderItem;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class AdvertisementActivity extends AppCompatActivity {
    private static final String TAG = "AdvertisementActivity";

    private TextView mName;
    private TextView mPrice;
    private TextView mDescription;
    private TextView mCurrentAmount;
    private TextView mExpireDate;
    private EditText mAmount;
    private Button mAddToCart;
    private long adId;
    private double amount = 0;

    private NetworkController networkController;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AdvertisementActivity.class);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisement);
        mName = (TextView) findViewById(R.id.ad_name);
        mPrice = (TextView) findViewById(R.id.ad_price);
        mDescription = (TextView) findViewById(R.id.ad_description);
        mCurrentAmount = (TextView) findViewById(R.id.ad_current_amount);
        mExpireDate = (TextView) findViewById(R.id.ad_expire_date);
        mAmount = (EditText) findViewById(R.id.editTextNumber);
        mAddToCart = (Button) findViewById(R.id.button_add_to_cart);
        networkController = new NetworkController(this);
        Intent intent = getIntent();

        if(intent.hasExtra("selected_ad")) {
            Advertisement advertisement = intent.getParcelableExtra("selected_ad");
            Log.d(TAG, "onCreate: " + advertisement.toString());

            mName.setText(advertisement.getName());
            mPrice.setText("Verð: " + advertisement.getPrice());
            mDescription.setText("Lýsing: " + advertisement.getDescription());
            mCurrentAmount.setText("Magn: " + advertisement.getCurrentAmount());
            mExpireDate.setText("Gildir til: " + advertisement.getExpireDate());
            adId = advertisement.getId();

            if(intent.hasExtra("token")) {
                String token = intent.getStringExtra("token");
                Log.d(TAG, "onCreate: " + token);
                mAddToCart.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String amountString = mAmount.getText().toString();
                        if (!amountString.isEmpty()) {
                            amount = Double.valueOf(mAmount.getText().toString());
                        }
                        if (amount != 0 && amount <= advertisement.getCurrentAmount()) {
                            networkController.addToCart(new NetworkCallback<OrderItem>() {
                                @Override
                                public void onError(String error) {
                                    Toast.makeText(AdvertisementActivity.this, error, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(OrderItem orderItem) {
                                    Toast.makeText(AdvertisementActivity.this, "Tókst að bæta við körfu", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }, adId, amount, token);
                        } else if(amount==0){
                            Toast.makeText(AdvertisementActivity.this, "Magn má ekki vera núll", Toast.LENGTH_SHORT).show();
                        } else if (amount > advertisement.getCurrentAmount()){
                            Toast.makeText(AdvertisementActivity.this, "Of mikið magn valið", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
    }
}