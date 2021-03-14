package is.hi.hbv601g.matbjorg_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import is.hi.hbv601g.matbjorg_app.MainActivity;
import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Seller;
import is.hi.hbv601g.matbjorg_app.models.User;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;
import is.hi.hbv601g.matbjorg_app.ui.menu.MenuFragment;

public class LoginActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mLogin;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, LoginActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        NetworkController networkController = new NetworkController(LoginActivity.this);
        mEmail = (EditText) findViewById(R.id.email);
        mPassword = (EditText) findViewById(R.id.password);
        mLogin = (Button) findViewById(R.id.login);
        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                networkController.login(new NetworkCallback<User>() {
                    @Override
                    public void onError(String error) {
                        Toast.makeText(LoginActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(User user) {
                        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putLong("loggedin_user_id", user.getId());
                        editor.putString("loggedin_user_type", user.getType());
                        editor.putString("token", user.getToken());
                        editor.apply();
                        Toast.makeText(LoginActivity.this, "Innskráning gekk", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, mEmail.getText().toString(), mPassword.getText().toString());
            }
        });
    }
}