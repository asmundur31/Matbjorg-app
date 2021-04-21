package is.hi.hbv601g.matbjorg_app.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.models.Location;
import is.hi.hbv601g.matbjorg_app.models.Tag;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

@RequiresApi(api = Build.VERSION_CODES.M)
public class AddAdvertisementActivity extends AppCompatActivity {

    private static final String TAG = "AddAdvertisementActivity";
    static final int REQUEST_IMAGE_CAPTURE = 1;


    private EditText mEditTextName;
    private EditText mEditTextDesc;
    private EditText mEditTextAmount;
    private EditText mEditTextPrice;

    private TextView mTextViewExpireDate;
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
 
    private TextView mTextViewCategories;
    private boolean[] selectedCategories;
    private ArrayList<Integer> categoryIndices = new ArrayList<>();
    private String[] categoryStrings;
    private List<String> chosenTags = new ArrayList<>();
    private TextView mTextViewLocation;
    private String[] locationStrings;
    private List<Location> locationsArray = new ArrayList<>();
    private Location chosenLocation;
    private int selectedLocation = -1;
    private Button mButtonConfirm;
    private NetworkController networkController;
    private Button mCamera;
    private ImageView mImageCapture;
    private Bundle imageUpload;
    private String token;


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
        token = sharedPref.getString("token", "");

        mEditTextName = (EditText) findViewById(R.id.textinput_heiti);
        mEditTextDesc = (EditText) findViewById(R.id.textinput_lysing);
        mEditTextAmount = (EditText) findViewById(R.id.textinput_magn);
        mEditTextPrice = (EditText) findViewById(R.id.textinput_verd);
        mTextViewCategories = (TextView) findViewById(R.id.select_tags);
        mTextViewLocation = (TextView) findViewById(R.id.select_location);
        mButtonConfirm = (Button) findViewById(R.id.button_staðfesta_auglýsingu);
        mTextViewExpireDate = (TextView) findViewById(R.id.text_velja_gildistima);
        mCamera = (Button) findViewById(R.id.camera);
        mImageCapture = (ImageView) findViewById(R.id.captureImage);


        setUpFilterCategory();
        setUpFilterLocation();
        mCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    // display error state to the user
                }
            }
        });

        mTextViewExpireDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        AddAdvertisementActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog,
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
                  }, token, mEditTextName.getText().toString(), mEditTextDesc.getText().toString(),
                          Double.parseDouble(mEditTextAmount.getText().toString()),
                          Double.parseDouble(mEditTextPrice.getText().toString()),
                          LocalDateTime.parse(mTextViewExpireDate.getText().toString().concat("T00:00"), DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm")),
                          chosenTags,
                          chosenLocation, imageUpload);
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
                        if (chosenTags.size() > 0) {
                            mTextViewCategories.setError(null);
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
                            mTextViewLocation.setError(null);
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
        if(mTextViewExpireDate.getText().toString().equals("Velja gildistíma")) {
            mTextViewExpireDate.setError("Veldu gildistíma");
            return false;
        }
        if (categoryIndices.size() == 0) {
            mTextViewCategories.setError("Veldu a.m.k. einn vöruflokk");
            return false;
        }
        if (chosenLocation == null) {
            mTextViewLocation.setError("Veldu staðsetningu");
            return false;
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            imageUpload = data.getExtras();
            Bitmap imageBitmap = (Bitmap) imageUpload.get("data");
            mImageCapture.setImageBitmap(imageBitmap);
        }
    }

}