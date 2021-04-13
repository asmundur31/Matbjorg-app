package is.hi.hbv601g.matbjorg_app.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Location;

public class AddLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener {

    private static final String EXTRA_CHOSEN_LOCATION = "is.hi.hbv601g.matbjorg_app.location";
    private SupportMapFragment mapFragment;
    private GoogleMap mMap;
    private Button mConfirmLocation;
    private EditText mName;

    private LatLng newLocation;
    private String newName;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AddLocationActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_location);

        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map_add_location);
        mapFragment.getMapAsync(this);
        mConfirmLocation = (Button) findViewById(R.id.confirm_location);
        mName = (EditText) findViewById(R.id.name_of_location);

        mName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    newName = s.toString();
                }
            }
        });

        mConfirmLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (newLocation != null && newName != null) {
                    Location loc = new Location(newLocation.longitude, newLocation.latitude, newName);
                    setLocationResults(loc);
                    finish();
                }
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng iceland = new LatLng(64.8, -18.4);
        float zoomLevel = 5;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(iceland, zoomLevel));
        mMap.setOnMapClickListener(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Nýr staður"));
        newLocation = latLng;
    }

    private void setLocationResults(Location location) {
        Intent data = new Intent();
        data.putExtra(EXTRA_CHOSEN_LOCATION, location);
        setResult(RESULT_OK, data);
    }

    public static Location getChosenLocation(Intent result) {
        Location position = result.getParcelableExtra(EXTRA_CHOSEN_LOCATION);
        return position;
    }
}