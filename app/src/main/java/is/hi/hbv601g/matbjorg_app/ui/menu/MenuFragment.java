package is.hi.hbv601g.matbjorg_app.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.ui.BasketActivity;
import is.hi.hbv601g.matbjorg_app.ui.LoginActivity;
import is.hi.hbv601g.matbjorg_app.ui.MapsActivity;
import is.hi.hbv601g.matbjorg_app.ui.MapsFragment;
import is.hi.hbv601g.matbjorg_app.ui.ReviewActivity;
import is.hi.hbv601g.matbjorg_app.ui.SignupActivity;

public class MenuFragment extends Fragment {
    private static final String TAG = "MenuFragment";
    private MenuViewModel menuViewModel;
    private Button mLoginButton;
    private Button mSignupButton;
    private Button mLogoutButton;
    private Button mBasketButton;
    private Button mMapButton;
    private Button mReviewButton;
    private static final int REQUEST_CODE_LOGIN = 0;
    private static final int REQUEST_CODE_NOT_LOGGED_IN = 1;
    private static final int REQUEST_CODE_BASKET = 2;
    private static final int REQUEST_CODE_SIGNUP = 3;
    private static final int REQUEST_CODE_MAP = 4;
    private static final int REQUEST_CODE_REVIEW = 5;


    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_menu, container, false);
        mLoginButton = (Button) root.findViewById(R.id.login_button);
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LoginActivity.newIntent(getActivity());
                startActivityForResult(intent, REQUEST_CODE_LOGIN);
            }
        });
        mSignupButton = (Button) root.findViewById(R.id.signup_button);
        mSignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = SignupActivity.newIntent(getActivity());
                startActivityForResult(intent, REQUEST_CODE_SIGNUP);
            }
        });
        mLogoutButton = (Button) root.findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("loggedin_user_id");
                editor.remove("loggedin_user_type");
                editor.remove("token");
                editor.apply();
                Toast.makeText(getActivity(), "Útskráning tókst", Toast.LENGTH_SHORT).show();
                checkLoggedIn();
            }
        });
        mBasketButton = (Button) root.findViewById(R.id.basket_button);
        mBasketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tjékkum hvort buyer sé loggaður inn
                SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
                long loggedin_user_id = sharedPref.getLong("loggedin_user_id", -1);
                Log.d(TAG, String.valueOf(loggedin_user_id));
                if(loggedin_user_id == -1) {
                    // Sendum notanda í login ef hann er ekki loggaður inn
                    Log.d(TAG, "Notandi ekki loggaður inn");
                    Intent intent = LoginActivity.newIntent(getActivity());
                    startActivityForResult(intent, REQUEST_CODE_NOT_LOGGED_IN);
                    return;
                }
                Log.d(TAG, "Notandi er loggaður inn");
                Intent intent = BasketActivity.newIntent(getActivity());
                startActivityForResult(intent, REQUEST_CODE_BASKET);
            }
        });

        mMapButton = (Button) root.findViewById(R.id.map_button);
        mMapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = MapsActivity.newIntent(getActivity());
                startActivityForResult(intent, REQUEST_CODE_MAP);
            }
        });

        mReviewButton = (Button) root.findViewById(R.id.review_button);
        mReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = ReviewActivity.newIntent(getActivity());
                startActivityForResult(intent, REQUEST_CODE_REVIEW);
            }
        });
        // Athugum hvort eitthver sé loggaður inn
        checkLoggedIn();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLoggedIn();
    }

    private void checkLoggedIn() {
        SharedPreferences sharedPref = getActivity().getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
        String loggedin_user_type = sharedPref.getString("loggedin_user_type", "");
        Log.d(TAG, loggedin_user_type);
        if (loggedin_user_type.equals("")) {
            mLoginButton.setVisibility(View.VISIBLE);
            mSignupButton.setVisibility(View.VISIBLE);
            mLogoutButton.setVisibility(View.GONE);
            mBasketButton.setVisibility(View.GONE);
        } else if (loggedin_user_type.equals("buyer")){
            mLoginButton.setVisibility(View.GONE);
            mSignupButton.setVisibility(View.GONE);
            mLogoutButton.setVisibility(View.VISIBLE);
            mBasketButton.setVisibility(View.VISIBLE);
        } else {
            mLoginButton.setVisibility(View.GONE);
            mSignupButton.setVisibility(View.GONE);
            mLogoutButton.setVisibility(View.VISIBLE);
            mBasketButton.setVisibility(View.GONE);
        }
    }
}