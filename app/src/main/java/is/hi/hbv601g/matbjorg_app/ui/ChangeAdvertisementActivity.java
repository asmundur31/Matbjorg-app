package is.hi.hbv601g.matbjorg_app.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.models.Location;
import is.hi.hbv601g.matbjorg_app.models.Tag;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class ChangeAdvertisementActivity extends AppCompatActivity {
    private static final String TAG = "ChangeAdvertisementActivity";

    private EditText mChangeAdvertisementHeiti;
    private EditText mChangeAdvertisementLysing;
    private EditText mChangeAdvertisementMagn;
    private EditText mChangeAdvertisementVerd;

    private TextView mChangeAdvertisementGildistimi;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;

    private TextView mChangeAdvertisementTags;
    private boolean[] selectedCategories;
    private ArrayList<Integer> categoryIndices = new ArrayList<>();
    private String[] categoryStrings;
    private List<String> chosenTags = new ArrayList<>();
    private TextView mChangeAdvertisementLocation;
    private String[] locationStrings;
    private List<Location> locationsArray = new ArrayList<>();
    private Location chosenLocation;
    private int selectedLocation = -1;
    private Button mChangeAdvertisementConfirmButton;
    private NetworkController networkController;
    private String token;

    private Advertisement advertisement;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ChangeAdvertisementActivity.class);
        return intent;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_advertisement);

        networkController  = new NetworkController(ChangeAdvertisementActivity.this);
        SharedPreferences sharedPref = this.getSharedPreferences("is.hi.hbv601g.matbjorg_app", Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "");

        mChangeAdvertisementHeiti = (EditText) findViewById(R.id.change_advertisement_heiti);
        mChangeAdvertisementLysing = (EditText) findViewById(R.id.change_advertisement_lysing);
        mChangeAdvertisementMagn = (EditText) findViewById(R.id.change_advertisement_magn);
        mChangeAdvertisementVerd = (EditText) findViewById(R.id.change_advertisement_verd);
        mChangeAdvertisementTags = (TextView) findViewById(R.id.change_advertisement_tags);
        mChangeAdvertisementLocation = (TextView) findViewById(R.id.change_advertisement_location);
        mChangeAdvertisementConfirmButton = (Button) findViewById(R.id.change_advertisement_confirm_button);
        mChangeAdvertisementGildistimi = (TextView) findViewById(R.id.change_advertisement_gildistimi);

        setUpFilterCategory();
        setUpFilterLocation();

        mChangeAdvertisementGildistimi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ChangeAdvertisementActivity.this,
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
                mChangeAdvertisementGildistimi.setText(date);
            }
        };

        mChangeAdvertisementConfirmButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View v) {
                if (checkInputs()) {
                    networkController.changeAdvertisement(new NetworkCallback<Advertisement>() {
                        @Override
                        public void onError(String error) {
                            Toast.makeText(ChangeAdvertisementActivity.this, error, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Advertisement response) {
                            Toast.makeText(ChangeAdvertisementActivity.this, "Það tókst að uppfæra auglýsingu", Toast.LENGTH_LONG).show();
                            finish();
                        }
                    }, advertisement.getId(), token, mChangeAdvertisementHeiti.getText().toString(), mChangeAdvertisementLysing.getText().toString(),
                    Double.parseDouble(mChangeAdvertisementMagn.getText().toString()),
                    Double.parseDouble(mChangeAdvertisementVerd.getText().toString()),
                    LocalDateTime.parse(mChangeAdvertisementGildistimi.getText().toString().concat("T00:00"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                    chosenTags,
                    chosenLocation);
                }
            }
        });
    }

    private void setUpFilterCategory() {
        categoryStrings = Tag.getTags();
        Arrays.sort(categoryStrings);
        selectedCategories = new boolean[Tag.values().length];
        setUpCategoryListener();
    }

    private void setUpFilterLocation() {
        SharedPreferences sharedPref = this.getSharedPreferences("is.hi.hbv601g.matbjorg_app", Context.MODE_PRIVATE);
        long loggedin_user_id = sharedPref.getLong("loggedin_user_id", -1);
        networkController.getLocationsBySeller(new NetworkCallback<List<Location>>() {
            @Override
            public void onError(String error) {
                Toast.makeText(ChangeAdvertisementActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(List<Location> locations) {
                locationStrings = new String[locations.size()];
                locationsArray = locations;
                for (int i=0; i<locations.size(); i++) {
                    locationStrings[i] = locations.get(i).getName();
                }
                Arrays.sort(locationStrings);
                Collections.sort(locationsArray);
                setUpLocationListener();
                putInData();
            }
        }, loggedin_user_id);
    }

    private void setUpCategoryListener() {
        mChangeAdvertisementTags.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeAdvertisementActivity.this);
                builder.setTitle("Veldu flokka");
                builder.setMultiChoiceItems(categoryStrings, selectedCategories, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            if (!categoryIndices.contains(which)) categoryIndices.add(which);
                        } else {
                            if (categoryIndices.contains(which))
                                categoryIndices.remove(Integer.valueOf(which));
                        }
                    }
                });
                builder.setCancelable(false);

                builder.setPositiveButton("Velja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chosenTags.clear();
                        for (int i = 0; i < categoryIndices.size(); i++) {
                            chosenTags.add(categoryStrings[categoryIndices.get(i)]);
                        }
                        if (chosenTags.size() > 0) {
                            mChangeAdvertisementTags.setError(null);
                        }
                    }
                });
                builder.setNegativeButton("Hætta við", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Hreinsa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < selectedCategories.length; i++) {
                            selectedCategories[i] = false;
                        }
                        chosenTags.clear();
                        categoryIndices.clear();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void setUpLocationListener() {
        mChangeAdvertisementLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ChangeAdvertisementActivity.this);
                builder.setTitle("Veldu staðsetningu");
                builder.setSingleChoiceItems(locationStrings, selectedLocation, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chosenLocation = locationsArray.get(which);
                        selectedLocation = which;
                    }
                });
                builder.setCancelable(false);

                builder.setPositiveButton("Velja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedLocation == -1) {
                            chosenLocation = null;
                        } else {
                            mChangeAdvertisementLocation.setError(null);
                        }
                    }
                });
                builder.setNegativeButton("Hætta við", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chosenLocation = null;
                        selectedLocation = -1;
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void putInData() {
        Intent intent = getIntent();
        if(intent.hasExtra("selected_ad")) {
            advertisement = intent.getParcelableExtra("selected_ad");
            mChangeAdvertisementHeiti.setText(advertisement.getName());
            mChangeAdvertisementLysing.setText(advertisement.getDescription());
            mChangeAdvertisementMagn.setText(advertisement.getCurrentAmount()+"");
            mChangeAdvertisementVerd.setText(advertisement.getPrice()+"");
            Set<Tag> tags = advertisement.getTags();
            for (Tag tag : tags) {
                for (int j=0; j<categoryStrings.length; j++) {
                    if (tag.name().equals(categoryStrings[j])) {
                        selectedCategories[j] = true;
                        categoryIndices.add(j);
                        chosenTags.add(categoryStrings[j]);
                        break;
                    }
                }
            }
            Location location = advertisement.getLocation();
            chosenLocation = location;
            for (int i=0; i<locationStrings.length; i++) {
                if (location.getName().equals(locationStrings[i])) {
                    selectedLocation = i;
                }
            }
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            mChangeAdvertisementGildistimi.setText(advertisement.getExpireDate().format(formatter));
        }
    }

    private boolean checkInputs() {
        boolean submit = true;
        if(TextUtils.isEmpty(mChangeAdvertisementHeiti.getText())) {
            mChangeAdvertisementHeiti.setError("Gefðu auglýsingunni heiti");
            submit = false;
        }
        if(TextUtils.isEmpty(mChangeAdvertisementLysing.getText())){
            mChangeAdvertisementLysing.setError("Bættu við lýsingu á vörunni");
            submit = false;
        }
        if(TextUtils.isEmpty(mChangeAdvertisementMagn.getText())){
            mChangeAdvertisementMagn.setError("Settu inn magn af vöru");
            submit = false;
        }
        if(TextUtils.isEmpty(mChangeAdvertisementVerd.getText())){
            mChangeAdvertisementVerd.setError("Gefðu vörunni verð");
            submit = false;
        }
        if(mChangeAdvertisementGildistimi.getText().toString().equals("Velja gildistíma")) {
            mChangeAdvertisementGildistimi.setError("Veldu gildistíma");
            submit = false;
        }
        if (categoryIndices.size() == 0) {
            mChangeAdvertisementTags.setError("Veldu a.m.k. einn vöruflokk");
            submit = false;
        }
        if (chosenLocation == null) {
            mChangeAdvertisementLocation.setError("Veldu staðsetningu");
            submit = false;
        }
        return submit;
    }
}