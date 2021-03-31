package is.hi.hbv601g.matbjorg_app.ui.home;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.models.Buyer;
import is.hi.hbv601g.matbjorg_app.models.Order;
import is.hi.hbv601g.matbjorg_app.models.Seller;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;
import is.hi.hbv601g.matbjorg_app.ui.AdvertisementsActivity;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private Button mGetOrders;
    private ListView mListView;
    private Button mGetAds;
    private static final int REQUEST_CODE_ADS = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        NetworkController networkController = new NetworkController(getContext());
        mListView = root.findViewById(R.id.listview_buyers);
        mGetAds = root.findViewById(R.id.button_get_ads);
        mGetOrders = root.findViewById(R.id.button_get_orders);

        mGetAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
                // long loggedin_user_id = sharedPref.getLong("loggedin_user_id", -1);
                // Log.d(TAG, String.valueOf(loggedin_user_id));
                /* if(loggedin_user_id == -1) {
                    // Sendum notanda í login ef hann er ekki loggaður inn
                    Log.d(TAG, "Notandi ekki loggaður inn");
                    Intent intent = LoginActivity.newIntent(getActivity());
                    startActivityForResult(intent, REQUEST_CODE_NOT_LOGGED_IN);
                    return;
                }*/
                // Log.d(TAG, "Notandi er loggaður inn");
                Intent intent = AdvertisementsActivity.newIntent(getActivity());
                startActivityForResult(intent, REQUEST_CODE_ADS);
               }
        });
      
        mGetOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Sæki orders", Toast.LENGTH_SHORT).show();
                networkController.getOrders(new NetworkCallback<List<Order>>() {
                    @Override
                    public void onError(String error) {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<Order> orders) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, orders);
                        mListView.setAdapter(arrayAdapter);
                    }
                });
            }
        });
        return root;
    }
}