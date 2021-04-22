package is.hi.hbv601g.matbjorg_app.ui.profile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.models.Order;
import is.hi.hbv601g.matbjorg_app.models.Seller;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;
import is.hi.hbv601g.matbjorg_app.ui.AddAdvertisementActivity;
import is.hi.hbv601g.matbjorg_app.ui.AdvertisementActivity;
import is.hi.hbv601g.matbjorg_app.ui.AdvertisementItemsAdapter;
import is.hi.hbv601g.matbjorg_app.ui.AdvertisementsActivity;
import is.hi.hbv601g.matbjorg_app.ui.BasketActivity;
import is.hi.hbv601g.matbjorg_app.ui.ChangeAdvertisementActivity;
import is.hi.hbv601g.matbjorg_app.ui.LoginActivity;
import is.hi.hbv601g.matbjorg_app.ui.ReceiptActivity;
import is.hi.hbv601g.matbjorg_app.ui.SellerAdsAdapter;

public class ProfileFragment extends Fragment implements SellerAdsAdapter.OnAdChangeListener {
    private static final String TAG = "ProfileFragment";
    private TextView mText;
    private ListView mListView;
    private  TextView mTextPantanir;
    private Button mButtonSetjaInnAuglysingu;
    private RecyclerView mSellerAdvertisements;
    private LinearLayout mSellerAdvertisement;
    private static final int REQUEST_CODE_ADD_ADVERTISEMENT = 0;
    private static final int REQUEST_CODE_CHANGE_ADVERTISEMENT = 1;
    private static final int REQUEST_CODE_RECEIPT = 2;
    private ArrayList<Advertisement> mSellerAds = new ArrayList<>();
    private ArrayList<Order> orderList = new ArrayList<>();
    List<String> orderDates = new ArrayList<>();
    private NetworkController networkController;
    private String token;
    private long loggedin_user_id;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        networkController = new NetworkController(getContext());
        mText = root.findViewById(R.id.text_profile);
        mListView = root.findViewById(R.id.listView_profile_orders);
        mTextPantanir = root.findViewById(R.id.text_previous_orders);
        mButtonSetjaInnAuglysingu = root.findViewById(R.id.button_setja_inn_auglysingu);
        mSellerAdvertisements = root.findViewById(R.id.recycler_view_seller_ads);
        mSellerAdvertisement = root.findViewById(R.id.layout_advertisement_seller);

        SellerAdsAdapter adapter = new SellerAdsAdapter(mSellerAds, ProfileFragment.super.getContext(), ProfileFragment.this);
        mSellerAdvertisements.setAdapter(adapter);
        mSellerAdvertisements.setLayoutManager(new LinearLayoutManager(ProfileFragment.super.getContext()));

        SharedPreferences sharedPref = getActivity().getSharedPreferences("is.hi.hbv601g.matbjorg_app", Context.MODE_PRIVATE);
        loggedin_user_id = sharedPref.getLong("loggedin_user_id", -1);
        String loggedin_user_type = sharedPref.getString("loggedin_user_type", "");
        String loggedin_user_email = sharedPref.getString("loggedin_user_email", "");
        token = sharedPref.getString("token", "");
        Log.i(TAG, "Tjékkum hvort eitthver sé loggaður inn");
        if(loggedin_user_id != -1) {
            mText.setText("Þú ert skráður inn sem " + loggedin_user_type + " með netfangið " + loggedin_user_email);

            if(loggedin_user_type.equals("buyer")) {
                mSellerAdvertisements.setVisibility(View.GONE);
                mButtonSetjaInnAuglysingu.setVisibility(View.GONE);
                mTextPantanir.setText("Þínar pantanir:");
                Toast.makeText(getActivity(), "Sæki pantanir notanda", Toast.LENGTH_SHORT).show();
                ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, orderDates);
                mListView.setAdapter(arrayAdapter);
                getBuyersOrders();

            }

            if(loggedin_user_type.equals("seller")){
                mButtonSetjaInnAuglysingu.setVisibility(View.VISIBLE);
                mButtonSetjaInnAuglysingu.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.M)
                    @Override
                    public void onClick(View v) {
                        Intent intent = AddAdvertisementActivity.newIntent(getActivity());
                        startActivityForResult(intent, REQUEST_CODE_ADD_ADVERTISEMENT);
                    }
                });
                getSellerAds();
            }

        } else {
            mText.setText("Enginn skráður inn");
            mButtonSetjaInnAuglysingu.setVisibility(View.GONE);
        }

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Order o = (Order) orderList.get(position);
                Intent intent = ReceiptActivity.newIntent(ProfileFragment.super.getContext());
                intent.putExtra("order", (new Gson()).toJson(o));
                intent.putExtra("token", token);
                startActivityForResult(intent, REQUEST_CODE_RECEIPT);
            }
        });

        return root;
    }

    @Override
    public void onAdChangeClick(int position) {
        if (!token.isEmpty()) {
            Intent intent = ChangeAdvertisementActivity.newIntent(ProfileFragment.super.getContext());
            SellerAdsAdapter adapter = (SellerAdsAdapter) mSellerAdvertisements.getAdapter();
            intent.putExtra("selected_ad", adapter.getAd(position));
            intent.putExtra("token", token);
            startActivityForResult(intent, REQUEST_CODE_CHANGE_ADVERTISEMENT);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getSellerAds();
    }

    private void getBuyersOrders (){
        networkController.getBuyerOrders(new NetworkCallback<List<Order>>() {
            @Override
            public void onError(String error) {
                Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onResponse(List<Order> orders) {
                if(orders.isEmpty()) {
                    mTextPantanir.setText("Þú átt engar pantanir");
                }
                else {
                    ArrayAdapter arrayAdapter = (ArrayAdapter) mListView.getAdapter();
                    for (Order order : orders) {
                        orderList.add(order);
                        orderDates.add("Sækja kvittun fyrir pöntun frá " + order.getTimeOfPurchase().toString().replace("T", " "));
                    }
                    arrayAdapter.notifyDataSetChanged();
                }
            }
            }, loggedin_user_id
        );
    }


    private void getSellerAds() {
        networkController.getSellersAds(new NetworkCallback<List<Advertisement>>() {
            @Override
            public void onError(String error) {
                Toast.makeText(ProfileFragment.super.getContext(), error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<Advertisement> response) {
                mSellerAds = (ArrayList<Advertisement>) response;
                SellerAdsAdapter adapter = (SellerAdsAdapter) mSellerAdvertisements.getAdapter();
                adapter.setAdvertisements(mSellerAds);
            }
        }, loggedin_user_id);
    }
}