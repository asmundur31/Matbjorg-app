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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Buyer;
import is.hi.hbv601g.matbjorg_app.models.Order;
import is.hi.hbv601g.matbjorg_app.models.OrderItem;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class ReceiptActivity extends AppCompatActivity {
    private static final String TAG = "ReceiptActivity";
    private LinearLayout mOrder;
    private RecyclerView mOrderItems;
    private TextView mOrderTitle;
    private TextView mTotalPrice;
    private List<OrderItem> mItems = new ArrayList<OrderItem>();
    private Order order;

    private NetworkController networkController;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ReceiptActivity.class);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receipt);
        Intent intent = getIntent();
        mOrder = (LinearLayout) findViewById(R.id.order);
        mOrderTitle = (TextView) findViewById(R.id.orderTitle);
        mOrderTitle.setText("Pöntun");
        mTotalPrice = (TextView) findViewById(R.id.totalPrice);

        if(intent.hasExtra("order") && intent.hasExtra("token")) {
            Gson gson = new Gson();
            String json = intent.getStringExtra("order");
            String token = intent.getStringExtra("token");
            System.out.println(json);
            order = gson.fromJson(json, Order.class);
            buildRecyclerView(order, token);
            double totalPrice = order.getTotalPrice();
            mTotalPrice.setText("Heildarverð: " + totalPrice);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void buildRecyclerView(Order order, String token) {
        mOrderItems = (RecyclerView) findViewById(R.id.order_items);
        OrderItemAdapter adapter = new OrderItemAdapter(order.getItems(), ReceiptActivity.this);
        mOrderItems.setAdapter(adapter);
        mOrderItems.setLayoutManager(new LinearLayoutManager(ReceiptActivity.this));
    }


}