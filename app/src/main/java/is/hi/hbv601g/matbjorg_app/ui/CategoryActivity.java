package is.hi.hbv601g.matbjorg_app.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.Arrays;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Tag;

public class CategoryActivity extends AppCompatActivity implements CategoryItemsAdapter.OnAdListener {

    private RecyclerView mRecyclerViewCategories;
    private String[] tags;
    private static final int REQUEST_CODE_CATEGORY = 0;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, CategoryActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        mRecyclerViewCategories = (RecyclerView) findViewById(R.id.recycler_view_categories);
        tags = Tag.getTags();
        Arrays.sort(tags);
        CategoryItemsAdapter adapter = new CategoryItemsAdapter(tags, CategoryActivity.this, CategoryActivity.this);
        mRecyclerViewCategories.setAdapter(adapter);
        mRecyclerViewCategories.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));
    }

    @Override
    public void onAdClick(int position) {
        Intent intent = AdvertisementsActivity.newIntent(CategoryActivity.this);
        intent.putExtra("selected_category", tags[position]);
        startActivityForResult(intent, REQUEST_CODE_CATEGORY);
    }
}