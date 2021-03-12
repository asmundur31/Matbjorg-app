package is.hi.hbv601g.matbjorg_app.ui.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.TestLooperManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    private TextView mText;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mText = root.findViewById(R.id.text_profile);
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
        long loggedin_user_id = sharedPref.getLong("loggedin_user_id", -1);
        String loggedin_user_type = sharedPref.getString("loggedin_user_type", "");
        Log.i(TAG, "Tjékkum hvort eitthver sé loggaður inn");
        if(loggedin_user_id != -1) {
            mText.setText("Notandi sem er loggaður inn hefur id = "+ loggedin_user_id +"\n og er " + loggedin_user_type);
        } else {
            mText.setText("Enginn loggaður inn");
        }
        return root;
    }
}