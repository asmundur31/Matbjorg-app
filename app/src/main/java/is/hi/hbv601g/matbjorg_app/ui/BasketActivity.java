package is.hi.hbv601g.matbjorg_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Order;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class BasketActivity extends AppCompatActivity {
    private static final String TAG = "BasketActivity";
    private TextView mBasketTitle;
    private RecyclerView mItemsInBasket;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, BasketActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        mBasketTitle = (TextView) findViewById(R.id.basket_title_view);
        mItemsInBasket = (RecyclerView) findViewById(R.id.items_in_basket);

        // Sækjum token hjá innskráðum notanda
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");
        NetworkController networkController = new NetworkController(this);
        networkController.getOrder(new NetworkCallback<Order>() {
            @Override
            public void onError(String error) {
                Toast.makeText(BasketActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Order order) {
                BasketItemAdapter adapter = new BasketItemAdapter(order.getItems(), BasketActivity.this);
                mItemsInBasket.setAdapter(adapter);
                mItemsInBasket.setLayoutManager(new LinearLayoutManager(BasketActivity.this));
            }
        }, token);

    }
}