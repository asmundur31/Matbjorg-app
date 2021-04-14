package is.hi.hbv601g.matbjorg_app.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Location;
import is.hi.hbv601g.matbjorg_app.models.Review;
import is.hi.hbv601g.matbjorg_app.models.Seller;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class ReviewActivity extends AppCompatActivity {

    private TextView mSellerDropdown;
    private TextView mSellerText;
    private Button mNewReviewButton;
    private TextView mAverageRatingText;
    private RecyclerView mReviewsRecyclerView;
    private NetworkController networkController;
    private String loggedin_user_type = "";

    private int selectedSeller = -1;
    private Seller seller;
    private String[] sellersString;
    private List<Seller> sellersList;
    private List<Review> mReveiws = new ArrayList<>();

    private static final int REQUEST_CODE_NEW_REVIEW = 0;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, ReviewActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);
        mSellerDropdown = (TextView) findViewById(R.id.seller_choose);
        mSellerText = (TextView) findViewById(R.id.seller_text);
        mNewReviewButton = (Button) findViewById(R.id.new_review_button);
        mAverageRatingText = (TextView) findViewById(R.id.average_rating_text);
        mReviewsRecyclerView = (RecyclerView) findViewById(R.id.reviews_recyclerView);
        networkController = new NetworkController(this);

        ReviewItemsAdapter adapter = new ReviewItemsAdapter(mReveiws, ReviewActivity.this);
        mReviewsRecyclerView.setAdapter(adapter);
        mReviewsRecyclerView.setLayoutManager(new LinearLayoutManager(ReviewActivity.this));

        SharedPreferences sharedPref = ReviewActivity.this.getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
        loggedin_user_type = sharedPref.getString("loggedin_user_type", "");

        mNewReviewButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = AddReviewActivity.newIntent(ReviewActivity.this);
                intent.putExtra("seller", seller);
                startActivityForResult(intent, REQUEST_CODE_NEW_REVIEW);
            }
        });

        networkController.getSellers(new NetworkCallback<List<Seller>>() {
            @Override
            public void onError(String error) {
                Toast.makeText(ReviewActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<Seller> sellers) {
                sellersList = sellers;
                sellersString = new String[sellers.size()];
                for (int i=0; i<sellersString.length; i++) {
                    sellersString[i] = sellers.get(i).getName();
                }
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
                AlertDialog.Builder builder = new AlertDialog.Builder(ReviewActivity.this);
                builder.setTitle("Veldu söluaðila");
                builder.setSingleChoiceItems(sellersString, selectedSeller, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        selectedSeller = which;
                        seller = sellersList.get(which);
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("Velja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedSeller > -1) {
                            mSellerText.setText(sellersString[selectedSeller]);
                            if (loggedin_user_type.equals("Buyer")) {
                                mNewReviewButton.setVisibility(View.VISIBLE);
                            }
                            // Sækja öll review eftir ákveðnum seller
                            getReviews();
                        } else {
                            mSellerText.setText("Vinsamlegast veldu söluaðila");
                        }
                    }
                });

                builder.setNegativeButton("Hætta við", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        selectedSeller = -1;
                        seller = null;
                        mSellerText.setText("Vinsamlegast veldu söluaðila");
                        mNewReviewButton.setVisibility(View.GONE);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (seller != null) {
            getReviews();
        }
    }

    private void getReviews() {
        networkController.getReviewsBySeller(new NetworkCallback<List<Review>>() {
            @Override
            public void onError(String error) {
                Toast.makeText(ReviewActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<Review> reviews) {
                mReveiws = reviews;
                ReviewItemsAdapter adapter = (ReviewItemsAdapter) mReviewsRecyclerView.getAdapter();
                adapter.setReviews(mReveiws);
                if (reviews.size()>0) {
                    double sum = 0;
                    for (int i=0; i<mReveiws.size(); i++) {
                        sum += mReveiws.get(i).getRating();
                    }
                    sum /= mReveiws.size();
                    mAverageRatingText.setText("Meðaleinkunn: "+(Math.round(sum*10)/10.0));
                    mAverageRatingText.setVisibility(View.VISIBLE);
                    mReviewsRecyclerView.setVisibility(View.VISIBLE);

                } else {
                    mAverageRatingText.setVisibility(View.VISIBLE);
                    mAverageRatingText.setText("Engar umsagnir komnar");
                }
            }
        }, seller.getId());
    }
}