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
import is.hi.hbv601g.matbjorg_app.ui.CategoryActivity;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private Button mGetAds;
    private Button mGetCategories;
    private static final int REQUEST_CODE_ADS = 0;
    private static final int REQUEST_CODE_CATEGORIES = 1;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        NetworkController networkController = new NetworkController(getContext());
        mGetAds = root.findViewById(R.id.button_get_ads);
        mGetCategories = root.findViewById(R.id.button_get_categories);

        mGetAds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AdvertisementsActivity.newIntent(getActivity());
                startActivityForResult(intent, REQUEST_CODE_ADS);
               }
        });
      
        mGetCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CategoryActivity.newIntent(getActivity());
                startActivityForResult(intent, REQUEST_CODE_CATEGORIES);
            }
        });
        return root;
    }
}