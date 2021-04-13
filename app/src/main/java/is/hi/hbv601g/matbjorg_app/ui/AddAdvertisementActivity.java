package is.hi.hbv601g.matbjorg_app.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.models.Location;
import is.hi.hbv601g.matbjorg_app.models.Tag;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

@RequiresApi(api = Build.VERSION_CODES.M)
public class AddAdvertisementActivity extends AppCompatActivity {

    private EditText mEditTextName;
    private EditText mEditTextDesc;
    private EditText mEditTextAmount;
    private EditText mEditTextPrice;
    private EditText mEditTextExpireDate;
    private TextView mTextViewCategories;
    private boolean[] selectedCategories;
    private ArrayList<Integer> categoryIndices = new ArrayList<>();
    private String[] categoryStrings;
    private List<String> chosenTags = new ArrayList<>();
    private TextView mTextViewLocation;
    private String[] locationStrings;
    private List<Location> locationsArray = new ArrayList<>();
    private Location chosenLocation;
    private Button mButtonConfirm;
    private NetworkController networkController;


    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AddAdvertisementActivity.class);
        return intent;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_advertisement);
        networkController  = new NetworkController(AddAdvertisementActivity.this);
        SharedPreferences sharedPref = this.getSharedPreferences("is.hi.hbv601g.matbjorg_app", Context.MODE_PRIVATE);
        long loggedin_user_id = sharedPref.getLong("loggedin_user_id", -1);
        mEditTextName = (EditText) findViewById(R.id.textinput_heiti);
        mEditTextDesc = (EditText) findViewById(R.id.textinput_lysing);
        mEditTextAmount = (EditText) findViewById(R.id.textinput_magn);
        mEditTextPrice = (EditText) findViewById(R.id.textinput_verd);
        mEditTextExpireDate  = (EditText) findViewById(R.id.textinput_gildistimi);
        mTextViewCategories = (TextView) findViewById(R.id.select_tags);
        mTextViewLocation = (TextView) findViewById(R.id.select_location);
        mButtonConfirm = (Button) findViewById(R.id.button_staðfesta_auglýsingu);

        setUpFilterCategory();
        setUpFilterLocation();

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
                }, loggedin_user_id, mEditTextName.getText().toString(), mEditTextDesc.getText().toString(),
                        Double.parseDouble(mEditTextAmount.getText().toString()),
                        Double.parseDouble(mEditTextPrice.getText().toString()),
                        LocalDateTime.parse(mEditTextExpireDate.getText().toString().replace(" ", "T"),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")
                        ),
                        chosenTags,
                        chosenLocation);
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
                Toast.makeText(AddAdvertisementActivity.this, error, Toast.LENGTH_SHORT).show();
            }

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
            }
        }, loggedin_user_id);
    }

    private void setUpCategoryListener() {
        mTextViewCategories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddAdvertisementActivity.this);
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
        mTextViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddAdvertisementActivity.this);
                builder.setTitle("Veldu staðsetningu");
                builder.setSingleChoiceItems(locationStrings, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        chosenLocation = locationsArray.get(which);
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
                    }
                });
                builder.setNegativeButton("Hætta við", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}