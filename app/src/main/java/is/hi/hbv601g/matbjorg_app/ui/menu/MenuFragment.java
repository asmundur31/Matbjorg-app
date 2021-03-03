package is.hi.hbv601g.matbjorg_app.ui.menu;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.ui.LoginActivity;

public class MenuFragment extends Fragment {

    private MenuViewModel menuViewModel;
    private Button mLoginButton;
    private Button mSignupButton;
    private Button mLogoutButton;
    private static final int REQUEST_CODE_LOGIN = 0;

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
                Toast.makeText(getActivity(), "Leyfum notanda að nýskrá sig", Toast.LENGTH_SHORT).show();
            }
        });
        mLogoutButton = (Button) root.findViewById(R.id.logout_button);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = getActivity().getSharedPreferences("is.hi.hbv601g.matbjorg_app", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove("loggedin_user_id");
                editor.remove("loggedin_user_type");
                editor.apply();
                Toast.makeText(getActivity(), "Útskráning tókst", Toast.LENGTH_SHORT).show();
            }
        });
        return root;
    }
}