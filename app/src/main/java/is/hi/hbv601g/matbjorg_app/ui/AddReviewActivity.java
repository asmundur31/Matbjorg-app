package is.hi.hbv601g.matbjorg_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Review;
import is.hi.hbv601g.matbjorg_app.models.Seller;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class AddReviewActivity extends AppCompatActivity {

    private static final String TAG = "AddReviewActivity";
    private TextView mSellerText;
    private EditText mReviewText;
    private RatingBar mRatingBar;
    private Button mSubmitReview;

    private Seller seller;
    private String token = "";
    private NetworkController networkController;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AddReviewActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_review);
        mSellerText = (TextView) findViewById(R.id.seller_header_text);
        mReviewText = (EditText) findViewById(R.id.review_text);
        mRatingBar = (RatingBar) findViewById(R.id.ratingBar);
        mSubmitReview = (Button) findViewById(R.id.submit_review);
        networkController = new NetworkController(this);

        SharedPreferences sharedPref = AddReviewActivity.this.getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "");

        Intent intent = getIntent();
        if (intent.hasExtra("seller")) {
            seller = intent.getParcelableExtra("seller");
            mSellerText.setText(seller.getName());
        }

        mSubmitReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkInputs()) {
                    String review = mReviewText.getText().toString();
                    double rating = mRatingBar.getRating();
                    Log.d(TAG, "Köllum næst á networkcontroller");
                    networkController.addReview(new NetworkCallback<Review>() {
                        @Override
                        public void onError(String error) {
                            Toast.makeText(AddReviewActivity.this, error, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onResponse(Review response) {
                            Toast.makeText(AddReviewActivity.this, "Gekk upp að vista umsögn", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }, seller.getId(), token, review, rating);
                }
            }
        });
    }

    private boolean checkInputs() {
        boolean submit = true;
        if(TextUtils.isEmpty(mReviewText.getText())) {
            mReviewText.setError("Má ekki vera tómt");
            submit = false;
        }
        return submit;
    }
}