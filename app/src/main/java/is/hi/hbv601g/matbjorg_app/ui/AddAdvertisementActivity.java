package is.hi.hbv601g.matbjorg_app.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaDataSource;
import android.os.Build;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputType;
import android.text.TextUtils;
import android.util.ArraySet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import is.hi.hbv601g.matbjorg_app.MainActivity;
import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.models.Tag;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

@RequiresApi(api = Build.VERSION_CODES.M)
public class AddAdvertisementActivity extends AppCompatActivity {

    private static final String TAG = "AddAdvertisementActivity";
    private EditText mEditTextName;
    private EditText mEditTextDesc;
    private EditText mEditTextAmount;
    private EditText mEditTextPrice;
    private TextView mTextViewExpireDate;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private Button mButtonConfirm;
    //private String[] tagNames = Tag.getTags();
    //private ArrayList<Tag> tags = new ArrayList<>();


    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AddAdvertisementActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertisement);
        NetworkController networkController  = new NetworkController(AddAdvertisementActivity.this);
        SharedPreferences sharedPref = this.getSharedPreferences("is.hi.hbv601g.matbjorg_app", Context.MODE_PRIVATE);
        long loggedin_user_id = sharedPref.getLong("loggedin_user_id", -1);

        mEditTextName = (EditText) findViewById(R.id.textinput_heiti);
        mEditTextDesc = (EditText) findViewById(R.id.textinput_lysing);
        mEditTextAmount = (EditText) findViewById(R.id.textinput_magn);
        mEditTextPrice = (EditText) findViewById(R.id.textinput_verd);
        mTextViewExpireDate = (TextView) findViewById(R.id.text_velja_gildistima);
        mButtonConfirm = (Button) findViewById(R.id.button_staðfesta_auglýsingu);

        mTextViewExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddAdvertisementActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mOnDateSetListener,
                        year, month, day);
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = year + "-" + String.format("%02d", month) + "-" + String.format("%02d", dayOfMonth);
                mTextViewExpireDate.setText(date);
            }
        };
        mButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (checkInputs()) {
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
                                                       }, loggedin_user_id, mEditTextName.getText().toString(), mEditTextDesc.getText().toString(), Double.parseDouble(mEditTextAmount.getText().toString()),
                            Double.parseDouble(mEditTextPrice.getText().toString()), LocalDateTime.parse(mTextViewExpireDate.getText().toString().concat("T00:00"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")));
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private boolean checkInputs() {
        if(TextUtils.isEmpty(mEditTextName.getText())) {
            mEditTextName.setError("Gefðu auglýsingunni heiti");
            return false;
        }
        if(TextUtils.isEmpty(mEditTextDesc.getText())){
            mEditTextDesc.setError("Bættu við lýsingu á vörunni");
            return false;
        }
        if(TextUtils.isEmpty(mEditTextAmount.getText())){
            mEditTextAmount.setError("Settu inn magn af vöru");
            return false;
        }
        if(TextUtils.isEmpty(mEditTextPrice.getText())){
            mEditTextPrice.setError("Gefðu vörunni verð");
            return false;
        }
        if(mTextViewExpireDate.getText().toString().equals("Velja gildistíma")){
            mTextViewExpireDate.setError("Veldu gildistíma");
            return false;
        }
        return true;
    }

}