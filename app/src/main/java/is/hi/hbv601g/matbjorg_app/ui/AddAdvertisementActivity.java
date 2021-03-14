package is.hi.hbv601g.matbjorg_app.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaDataSource;
import android.os.Build;
import android.os.Bundle;
import android.util.ArraySet;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.models.Tag;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

@RequiresApi(api = Build.VERSION_CODES.M)
public class AddAdvertisementActivity extends AppCompatActivity {

    EditText mEditTextName;
    EditText mEditTextDesc;
    EditText mEditTextAmount;
    EditText mEditTextPrice;
    EditText mEditTextExpireDate;
    Button mButtonConfirm;
    List<String> mTagNames = new ArrayList<String>();


    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AddAdvertisementActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        List<Tag> tags = new ArrayList<>();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertisement);
        NetworkController networkController  = new NetworkController(AddAdvertisementActivity.this);
        SharedPreferences sharedPref = this.getSharedPreferences("is.hi.hbv601g.matbjorg_app", Context.MODE_PRIVATE);
        long loggedin_user_id = sharedPref.getLong("loggedin_user_id", -1);
        mEditTextName = (EditText) findViewById(R.id.textinput_heiti);
        mEditTextDesc = (EditText) findViewById(R.id.textinput_lysing);
        mEditTextAmount = (EditText) findViewById(R.id.textinput_magn);
        mEditTextPrice = (EditText) findViewById(R.id.textinput_verd);
        mEditTextExpireDate  = (EditText) findViewById(R.id.textinput_gildistimi);
        mButtonConfirm = (Button) findViewById(R.id.button_staðfesta_auglýsingu);
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                networkController.addAdvertisement(new NetworkCallback<Advertisement>() {
                    @Override
                    public void onError(String error) {
                        Toast.makeText(AddAdvertisementActivity.this, error, Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onResponse(Advertisement response) {
                        Toast.makeText(AddAdvertisementActivity.this, "Það tókst að setja inn auglýsingu", Toast.LENGTH_LONG).show();
                        finish();
                    }
                }, loggedin_user_id, mEditTextName.getText().toString(),  mEditTextDesc.getText().toString(), Double.parseDouble(mEditTextAmount.getText().toString()),
                        Double.parseDouble(mEditTextPrice.getText().toString()), LocalDateTime.parse(mEditTextExpireDate.getText().toString().replace(" ", "T"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
            }
        });

    }

    public void onCheckBoxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_tag1:
                if (checked) {
                    mTagNames.add("GRÆNMETI");
                }
                break;
            case R.id.checkbox_tag2:
                if (checked) {
                    mTagNames.add("MJÓLKURVARA");
                }
                break;
            case R.id.checkbox_tag3:
                if (checked) {
                    mTagNames.add("ÞURRVARA");
                }
                break;
            case R.id.checkbox_tag4:
                if (checked) {
                    mTagNames.add("BRAUÐ");
                }
                break;
            case R.id.checkbox_tag5:
                if (checked) {
                    mTagNames.add("BAKKELSI");
                }
                break;
            case R.id.checkbox_tag6:
                if (checked) {
                    mTagNames.add("ÁVEXTIR");
                }
                break;
            case R.id.checkbox_tag7:
                if (checked) {
                    mTagNames.add("DRYKKJARVARA");
                }
                break;
            case R.id.checkbox_tag8:
                if (checked) {
                    mTagNames.add("VEITINGASTAÐUR");
                }
                break;
            case R.id.checkbox_tag9:
                if (checked) {
                    mTagNames.add("SÆLGÆTI");
                }
                break;
            case R.id.checkbox_tag10:
                if (checked) {
                    mTagNames.add("HEILSUVARA");
                }
                break;
            case R.id.checkbox_tag11:
                if (checked) {
                    mTagNames.add("KJÖT");
                }
                break;
            case R.id.checkbox_tag12:
                if (checked) {
                    mTagNames.add("BAUNIR");
                }
                break;
            case R.id.checkbox_tag13:
                if (checked) {
                    mTagNames.add("VEGAN");
                }
                break;
            case R.id.checkbox_tag14:
                if (checked) {
                    mTagNames.add("GRÆNMETISFÆÐI");
                }
                break;
        }
    }
}