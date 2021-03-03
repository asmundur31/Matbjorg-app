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
    private ProfileViewModel profileViewModel;
    private TextView mText;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
        mText = root.findViewById(R.id.text_profile);
        SharedPreferences preferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String loggedin_user_id = preferences.getString("loggedin_user_id", "");
        Log.i(TAG, "Tjékkum hvort eitthver sé loggaður inn");
        // TODO: virkar ekki
        if(!loggedin_user_id.equals("")) {
            mText.setText("Notandi sem er loggaður inn er "+ loggedin_user_id);
        } else {
            mText.setText("Enginn loggaður inn");
        }
        return root;
    }
}