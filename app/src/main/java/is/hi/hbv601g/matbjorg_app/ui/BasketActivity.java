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
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Order;
import is.hi.hbv601g.matbjorg_app.models.OrderItem;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class BasketActivity extends AppCompatActivity {
    private static final String TAG = "BasketActivity";
    private LinearLayout mBasket;
    private TextView mBasketTitle;
    private TextView mTotalPrice;
    private RecyclerView mItemsInBasket;
    private Button mConfirmBasket;
    private List<OrderItem> mItems = new ArrayList<OrderItem>();

    private NetworkController networkController;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, BasketActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);
        mBasket = (LinearLayout) findViewById(R.id.basket);
        mBasketTitle = (TextView) findViewById(R.id.basket_title);
        mTotalPrice = (TextView) findViewById(R.id.total_price);
        mConfirmBasket = (Button) findViewById(R.id.confirm_basket);

        // Sækjum token hjá innskráðum notanda
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
        String token = sharedPref.getString("token", "");
        networkController = new NetworkController(this);
        networkController.getActiveOrder(new NetworkCallback<Order>() {
            @Override
            public void onError(String error) {
                Toast.makeText(BasketActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Order order) {
                String title = "";
                if (order.getItems().size() == 0) {
                    title = "Karfan er tóm";
                    mBasket.setVisibility(View.GONE);
                } else {
                    title = "Karfa";
                    mBasket.setVisibility(View.VISIBLE);
                }
                mBasketTitle.setText(title);
                mItems = order.getItems();
                buildRecyclerView(order, token);
                double totalPrice = order.getTotalPrice();
                mTotalPrice.setText("Samtals: " + totalPrice);
            }
        }, token);

        mConfirmBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkController.confirmOrder(new NetworkCallback<Order>() {
                    @Override
                    public void onError(String error) {
                        Toast.makeText(BasketActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Order order) {
                        Toast.makeText(BasketActivity.this, "Karfa staðfest", Toast.LENGTH_SHORT).show();
                        finish();
                        // Hvert viljum við fara? Heimasvæðið?
                        // Kannski kvittun?
                    }
                }, token);
            }
        });
    }

    /**
     * Fall sem sér um að setja upp recyclerView með réttu efni og listenerum
     * @param order Order-ið sem á að fara inn í recyclerView
     * @param token Tokenið sem að innskráður notandi er með
     */
    public void buildRecyclerView(Order order, String token) {
        mItemsInBasket = (RecyclerView) findViewById(R.id.items_in_basket);
        BasketItemAdapter adapter = new BasketItemAdapter(mItems, BasketActivity.this);
        adapter.setOnItemListener(new BasketItemAdapter.OnItemListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onChangeAmountClick(int position, double newAmount) {
                double currentAmount = order.getItems().get(position).getAdvertisement().getCurrentAmount();
                if (currentAmount < newAmount) {
                    Toast.makeText(BasketActivity.this, "Eftirstöðvar af vöru: " + currentAmount, Toast.LENGTH_SHORT).show();
                    return;
                }
                networkController.changeOrderItem(new NetworkCallback<Order>() {
                    @Override
                    public void onError(String error) {
                        Toast.makeText(BasketActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Order order) {
                        double totalPrice = order.getTotalPrice();
                        mTotalPrice.setText("Samtals: " + totalPrice);
                        Toast.makeText(BasketActivity.this, "Tókst að breyta", Toast.LENGTH_SHORT).show();
                    }
                }, mItems.get(position).getId(), newAmount, token);
                mItems.get(position).setAmount(newAmount);
                if (newAmount <= 0) {
                    // Þegar við viljum eyða itemi
                    mItems.remove(position);
                    mItemsInBasket.removeViewAt(position);
                }
                adapter.updateAmount(position, newAmount);
            }

            @Override
            public void onDeleteClick(int position) {
                networkController.deleteOrderItem(new NetworkCallback<Order>() {
                    @Override
                    public void onError(String error) {
                        Toast.makeText(BasketActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(Order order) {
                        double totalPrice = order.getTotalPrice();
                        mTotalPrice.setText("Samtals: " + totalPrice);
                        Toast.makeText(BasketActivity.this, "Tókst að eyða", Toast.LENGTH_SHORT).show();
                    }
                }, mItems.get(position).getId(), token);
                mItems.remove(position);
                mItemsInBasket.removeViewAt(position);
                adapter.updateAmount(position, 0);
            }
        });
        mItemsInBasket.setAdapter(adapter);
        mItemsInBasket.setLayoutManager(new LinearLayoutManager(BasketActivity.this));
    }


}