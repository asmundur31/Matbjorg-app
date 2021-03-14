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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Order;
import is.hi.hbv601g.matbjorg_app.models.Seller;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;
import is.hi.hbv601g.matbjorg_app.ui.AddAdvertisementActivity;
import is.hi.hbv601g.matbjorg_app.ui.LoginActivity;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private TextView mText;
    private ListView mListView;
    private  TextView mTextPantanir;
    private Button mButtonSetjaInnAuglysingu;
    private static final int REQUEST_CODE_ADD_ADVERTISEMENT = 0;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        NetworkController networkController = new NetworkController(getContext());
        mText = root.findViewById(R.id.text_profile);
        mListView = root.findViewById(R.id.listView_profile_orders);
        mTextPantanir = root.findViewById(R.id.text_previous_orders);
        mButtonSetjaInnAuglysingu = root.findViewById(R.id.button_setja_inn_auglysingu);
        SharedPreferences sharedPref = getActivity().getSharedPreferences("is.hi.hbv601g.matbjorg_app", Context.MODE_PRIVATE);
        long loggedin_user_id = sharedPref.getLong("loggedin_user_id", -1);
        String loggedin_user_type = sharedPref.getString("loggedin_user_type", "");
        Log.i(TAG, "Tjékkum hvort eitthver sé loggaður inn");
        if(loggedin_user_id != -1) {
            mText.setText("Notandi sem er loggaður inn hefur id = "+ loggedin_user_id + "\n og er " + loggedin_user_type);

            if(loggedin_user_type.equals("buyer")) {
                mButtonSetjaInnAuglysingu.setVisibility(View.GONE);
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
                                                 }, loggedin_user_id
                );
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

            }

        } else {
            mText.setText("Enginn loggaður inn");
        }
        return root;
    }
}