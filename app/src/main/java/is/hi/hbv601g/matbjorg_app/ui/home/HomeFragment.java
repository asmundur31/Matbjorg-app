package is.hi.hbv601g.matbjorg_app.ui.home;


import android.os.Bundle;
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
import is.hi.hbv601g.matbjorg_app.models.Buyer;
import is.hi.hbv601g.matbjorg_app.models.Seller;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private Button mGetBuyers;
    private Button mGetSellers;
    private ListView mListView;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        NetworkController networkController = new NetworkController(getContext());
        mGetBuyers = root.findViewById(R.id.button_get_buyers);
        mGetSellers = root.findViewById(R.id.button_get_sellers);
        mListView = root.findViewById(R.id.listview_buyers);
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
        return root;
    }
}