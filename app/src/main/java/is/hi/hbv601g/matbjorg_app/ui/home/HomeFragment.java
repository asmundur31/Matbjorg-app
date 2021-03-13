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

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.models.Buyer;
import is.hi.hbv601g.matbjorg_app.models.Seller;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;
import is.hi.hbv601g.matbjorg_app.ui.AdvertisementsActivity;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private Button mGetBuyers;
    private Button mGetSellers;
    private ListView mListView;
    private Button mGetAds;
    private static final int REQUEST_CODE_ADS = 0;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        NetworkController networkController = new NetworkController(getContext());
        mGetBuyers = root.findViewById(R.id.button_get_buyers);
        mGetSellers = root.findViewById(R.id.button_get_sellers);
        mListView = root.findViewById(R.id.listview_buyers);
        mGetAds = root.findViewById(R.id.button_get_ads);

        mGetBuyers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Sæki buyers", Toast.LENGTH_SHORT).show();
                networkController.getBuyers(new NetworkCallback<List<Buyer>>() {
                    @Override
                    public void onError(String error) {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<Buyer> buyers) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, buyers);
                        mListView.setAdapter(arrayAdapter);
                    }
                });
            }
        });
        mGetSellers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), "Sæki sellers", Toast.LENGTH_SHORT).show();
                networkController.getSellers(new NetworkCallback<List<Seller>>() {
                    @Override
                    public void onError(String error) {
                        Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<Seller> sellers) {
                        ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, sellers);
                        mListView.setAdapter(arrayAdapter);
                    }
                });
            }
        });

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
        return root;
    }
}