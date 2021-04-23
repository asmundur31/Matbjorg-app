package is.hi.hbv601g.matbjorg_app.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Location;
import is.hi.hbv601g.matbjorg_app.models.User;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;


public class SignupActivity extends AppCompatActivity {

    private Button mSignup;
    private EditText mName;
    private boolean isName;
    private EditText mEmail;
    private boolean isEmail;
    private EditText mPassword;
    private boolean isPassword;
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButton;
    private boolean isType;
    private Button mAddLocation;
    private ListView mLocations;
    private LocationAdapter locationAdapter;
    private List<Location> locations = new ArrayList<>();
    private boolean isSeller;
    private static final int ADD_LOCATION = 0;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, SignupActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        NetworkController networkController = new NetworkController(SignupActivity.this);
        mName = (EditText) findViewById(R.id.signup_name);
        mEmail = (EditText) findViewById(R.id.signup_username);
        mPassword = (EditText) findViewById(R.id.signup_password);
        mRadioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        mAddLocation = (Button) findViewById(R.id.add_location);
        mLocations = (ListView) findViewById(R.id.locations);
        mSignup = (Button) findViewById(R.id.signup);

        // Hlustarar til að passa að Nýskrá takki sé óvirkur ef það vantar upplýsingar
        addNameTextChangeListener();
        addEmailTextChangeListener();
        addPasswordTextChangeListener();
        addTypeListener();

        mAddLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddLocationActivity.newIntent(SignupActivity.this);
                startActivityForResult(intent, ADD_LOCATION);
            }
        });

        // Setjum tóman lista af staðsetningum inn í listView-ið
        ArrayList<Location> arrayOfLocations = new ArrayList<Location>();
        locationAdapter = new LocationAdapter(this, arrayOfLocations);
        mLocations.setAdapter(locationAdapter);

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = mRadioGroup.getCheckedRadioButtonId();
                mRadioButton = (RadioButton) findViewById(selectedId);
                networkController.signup(new NetworkCallback<User>() {
                    @Override
                    public void onError(String error) {
                        Toast.makeText(SignupActivity.this, error + "\nNetfang frátekið eða á vitlausu formi", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(User user) {
                        SharedPreferences sharedPref = getSharedPreferences("is.hi.hbv601g.matbjorg_app", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putLong("loggedin_user_id", user.getId());
                        editor.putString("loggedin_user_type", user.getType());
                        editor.putString("token", user.getToken());
                        editor.putString("loggedin_user_email", user.getEmail());
                        editor.apply();
                        Toast.makeText(SignupActivity.this, "Nýskráning gekk", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }, mRadioButton.getText().toString(), mName.getText().toString(), mEmail.getText().toString(), mPassword.getText().toString(), locations);
            }
        });
    }

    private void addNameTextChangeListener() {
        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    isName = true;
                } else {
                    isName = false;
                }
                checkEverything();
            }
        });
    }

    private void addEmailTextChangeListener() {
        mEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    isEmail = true;
                } else {
                    isEmail = false;
                }
                checkEverything();
            }
        });
    }

    private void addPasswordTextChangeListener() {
        mPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    isPassword = true;
                } else {
                    isPassword = false;
                }
                checkEverything();
            }
        });
    }

    private void addTypeListener() {
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int selectedId = mRadioGroup.getCheckedRadioButtonId();
                if (selectedId == -1) {
                    isType = false;
                } else {
                    isType = true;
                }
                // Athugum hvort viðkomandi er söluaðili
                RadioButton radioButton = findViewById(selectedId);
                if (radioButton.getText().toString().equals("Seller")) {
                    // Þá viljum við leyfa að velja staðsetningar
                    mAddLocation.setVisibility(View.VISIBLE);
                    isSeller = true;
                } else {
                    // Annars fjarlægjum við möguleikan
                    mAddLocation.setVisibility(View.GONE);
                    isSeller = false;
                    // Hreinsum líka staðsetningar ef þær eru til
                    locations.clear();
                    locationAdapter.clear();
                }
                checkEverything();
            }
        });
    }

    private void checkEverything() {
        if (isName && isEmail && isPassword && isType && (locations.size() > 0 || !isSeller)) {
            mSignup.setEnabled(true);
        } else {
            mSignup.setEnabled(false);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        if (requestCode == ADD_LOCATION) {
            if (data == null) {
                return;
            }
            Location newLocation = AddLocationActivity.getChosenLocation(data);
            locations.add(newLocation);
            locationAdapter.add(newLocation);
            checkEverything();
        }
    }
}