package is.hi.hbv601g.matbjorg_app.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Order;
import is.hi.hbv601g.matbjorg_app.models.Seller;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private TextView mText;
    private ListView mListView;
    private  TextView mTextPantanir;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        NetworkController networkController = new NetworkController(getContext());
        mText = root.findViewById(R.id.text_profile);
        mListView = root.findViewById(R.id.listView_profile_orders);
        mTextPantanir = root.findViewById(R.id.text_previous_orders);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("is.hi.hbv601g.matbjorg_app", Context.MODE_PRIVATE);
        String loggedin_user_id = sharedPref.getString("loggedin_user_id", "");
        String loggedin_user_type = sharedPref.getString("loggedin_user_type", "");
        Long loggedin_user_id_long = sharedPref.getLong("loggedin_user_id_long", 0);
        Log.i(TAG, "Tjékkum hvort eitthver sé loggaður inn");
        if(!loggedin_user_id.equals("")) {
            mText.setText("Notandi sem er loggaður inn hefur id = "+ loggedin_user_id +"\n og er " + loggedin_user_type);

            if(loggedin_user_type.equals("buyer")) {
                mTextPantanir.setText("Þínar pantanir:");
                Toast.makeText(getActivity(), "Sæki pantanir notanda", Toast.LENGTH_SHORT).show();
                networkController.getBuyerOrders(new NetworkCallback<List<Order>>() {
                                                     @Override
                                                     public void onError(String error) {
                                                         Toast.makeText(getContext(), error, Toast.LENGTH_SHORT).show();
                                                     }

                                                     @Override
                                                     public void onResponse(List<Order> orders) {
                                                         ArrayAdapter arrayAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, orders);
                                                         mListView.setAdapter(arrayAdapter);
                                                     }
                                                 }, loggedin_user_id_long
                );
            }

        } else {
            mText.setText("Enginn loggaður inn");
        }
        return root;
    }
}