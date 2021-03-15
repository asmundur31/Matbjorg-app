package is.hi.hbv601g.matbjorg_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Buyer;
import is.hi.hbv601g.matbjorg_app.models.User;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;


public class SignupActivity extends AppCompatActivity {

    private Button mSignup;
    private EditText mName;
    private EditText mEmail;
    private EditText mPassword;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SignupActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        NetworkController networkController = new NetworkController(SignupActivity.this);
        mEmail = (EditText) findViewById(R.id.signup_username);
        mPassword = (EditText) findViewById(R.id.signup_password);
        mName = (EditText) findViewById(R.id.signup_name);
        mSignup = (Button) findViewById(R.id.signup);
        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
                int selectedId = mRadioGroup.getCheckedRadioButtonId();
                mRadioButton = (RadioButton) findViewById(selectedId);
                networkController.signup(new NetworkCallback<User>() {
                    @Override
                    public void onError(String error) {
                        Toast.makeText(SignupActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(User user) {
                        SharedPreferences sharedPref = getSharedPreferences("is.hi.hbv601g.matbjorg_app", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putLong("loggedin_user_id", user.getId());
                        editor.putString("loggedin_user_type", user.getType());
                        editor.putString("token", user.getToken());
                        editor.apply();
                        Toast.makeText(SignupActivity.this, "Nýskráning gekk", Toast.LENGTH_SHORT).show();
                    }
                }, mRadioButton.getText().toString(), mName.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString());
            }
        });
    }
}