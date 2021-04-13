package is.hi.hbv601g.matbjorg_app.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Location;
import is.hi.hbv601g.matbjorg_app.models.Seller;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class MapsActivity extends AppCompatActivity {

    private static final String TAG = "MapsActivity";
    private TextView mSellerDropdown;
    private boolean[] selectedSellers;
    private ArrayList<Integer> sellersIndecies = new ArrayList<>();
    private String[] sellersString;
    private List<Seller> sellersList;
    private MapsFragment mMapFragment;
    private NetworkController networkController;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, MapsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        mSellerDropdown = (TextView) findViewById(R.id.seller_maps_dropdown);
        mMapFragment = (MapsFragment) getSupportFragmentManager().findFragmentById(R.id.map_fragment);
        networkController = new NetworkController(this);
        setUpFilterSellers();
    }

    private void setUpFilterSellers() {
        // Sækjum alla sellers sem eru til
        networkController.getSellers(new NetworkCallback<List<Seller>>() {
            @Override
            public void onError(String error) {
                Toast.makeText(MapsActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<Seller> sellers) {
                sellersList = sellers;
                sellersString = new String[sellers.size()];
                for (int i=0; i<sellersString.length; i++) {
                    sellersString[i] = sellers.get(i).getName();
                }
                selectedSellers = new boolean[sellers.size()];
                mMapFragment.clearMarkers();
                networkController.getLocations(new NetworkCallback<List<Location>>() {
                    @Override
                    public void onError(String error) {
                        Toast.makeText(MapsActivity.this, error, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(List<Location> locations) {
                        mMapFragment.addMarkers(locations);
                    }
                });
                Arrays.sort(sellersString);
                Collections.sort(sellersList);
                setUpSellerListener();
            }
        });
    }

    private void setUpSellerListener() {
        mSellerDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(MapsActivity.this);
                builder.setTitle("Veldu söluaðila");
                builder.setMultiChoiceItems(sellersString, selectedSellers, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            if (!sellersIndecies.contains(which)) sellersIndecies.add(which);
                        } else {
                            if (sellersIndecies.contains(which))
                                sellersIndecies.remove(Integer.valueOf(which));
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("Velja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMapFragment.clearMarkers();
                        if (selectedSellers.length>0) {
                            for (int i=0; i<sellersIndecies.size(); i++) {
                                networkController.getLocationsBySeller(new NetworkCallback<List<Location>>() {
                                    @Override
                                    public void onError(String error) {
                                        Toast.makeText(MapsActivity.this, error, Toast.LENGTH_SHORT).show();
                                    }

                                    @Override
                                    public void onResponse(List<Location> locations) {
                                        mMapFragment.addMarkers(locations);
                                    }
                                }, sellersList.get(sellersIndecies.get(i)).getId());
                            }
                        } else {
                            // Merkjum alla ef enginn söluaðili er valinn
                            networkController.getLocations(new NetworkCallback<List<Location>>() {
                                @Override
                                public void onError(String error) {
                                    Toast.makeText(MapsActivity.this, error, Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onResponse(List<Location> locations) {
                                    mMapFragment.addMarkers(locations);
                                }
                            });
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
                        for (int i = 0; i < selectedSellers.length; i++) {
                            selectedSellers[i] = false;
                        }
                        sellersIndecies.clear();
                        mMapFragment.clearMarkers();
                        networkController.getLocations(new NetworkCallback<List<Location>>() {
                            @Override
                            public void onError(String error) {
                                Toast.makeText(MapsActivity.this, error, Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onResponse(List<Location> locations) {
                                mMapFragment.addMarkers(locations);
                            }
                        });
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
}