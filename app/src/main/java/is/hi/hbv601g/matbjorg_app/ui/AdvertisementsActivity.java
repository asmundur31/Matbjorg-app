package is.hi.hbv601g.matbjorg_app.ui;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.InputMismatchException;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.models.Seller;
import is.hi.hbv601g.matbjorg_app.models.Tag;
import is.hi.hbv601g.matbjorg_app.network.NetworkCallback;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

public class AdvertisementsActivity extends AppCompatActivity implements AdvertisementItemsAdapter.OnAdListener {
    private static final String TAG = "AdvertisementsActivity";
    private SearchView mSearchView;
    private String searchQuery;
    // Breytur fyrir leit eftir flokkum og söluaðilum
    private TextView mCategoryDropdown;
    private boolean[] selectedCategories;
    private ArrayList<Integer> categoryList = new ArrayList<>();
    private String[] categoryArray;
    private TextView mCategoryList;
    private TextView mSellerDropdown;
    private boolean[] selectedSellers;
    private ArrayList<Integer> sellerList = new ArrayList<>();
    private String[] sellerArray;
    private TextView mSellerList;
    private TextView mAdvertisementsTitle;
    // Breytur fyrir flokka og söluaðila enda
    private RecyclerView mAdvertisementItems;
    private LinearLayout mAdvertisementItem;
    private static final int REQUEST_CODE_AD = 0;
    private List<Advertisement> mAds = new ArrayList<>();
    private String token;
    private NetworkController networkController;

    public static Intent newIntent(Context packageContext) {
        Intent intent = new Intent(packageContext, AdvertisementsActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advertisements);

        mSearchView = (SearchView) findViewById(R.id.search_view);
        mCategoryDropdown = (TextView) findViewById(R.id.category_dropdown);
        mCategoryList = (TextView) findViewById(R.id.category_list);
        mSellerDropdown = (TextView) findViewById(R.id.seller_dropdown);
        mSellerList = (TextView) findViewById(R.id.seller_list);
        mAdvertisementsTitle = (TextView) findViewById(R.id.advertisements_title_view);
        mAdvertisementItems = (RecyclerView) findViewById(R.id.advertisement_items);
        mAdvertisementItem = (LinearLayout) findViewById(R.id.advertisement_item);


        // Sækjum token hjá innskráðum notanda
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.sharedPref), Context.MODE_PRIVATE);
        token = sharedPref.getString("token", "");

        networkController = new NetworkController(this);

        AdvertisementItemsAdapter adapter = new AdvertisementItemsAdapter(mAds, AdvertisementsActivity.this, AdvertisementsActivity.this);
        mAdvertisementItems.setAdapter(adapter);
        mAdvertisementItems.setLayoutManager(new LinearLayoutManager(AdvertisementsActivity.this));

        networkController.getAdvertisements(new NetworkCallback<List<Advertisement>>() {
            @Override
            public void onError(String error) {
                Toast.makeText(AdvertisementsActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(List<Advertisement> ads) {
                mAds = ads;
                AdvertisementItemsAdapter adapter = (AdvertisementItemsAdapter) mAdvertisementItems.getAdapter();
                adapter.setAdvertisements(mAds);
                adapter.setAllAdvertisements(mAds);
                // Opnum search barinn þegar gögnin eru kominn
                mSearchView.setIconifiedByDefault(false);
                // Setjum category ef það er búið að velja það
                if(getIntent().hasExtra("selected_category")) {
                    addSelectedCategory();
                }
            }
        });

        setUpFilterText();
        setUpFilterCategory();
        setUpFilterSellers();
    }

    private void setUpFilterText() {
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // Fjarlægja lyklaborð
                closeKeyboard();
                return false;
            }

            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public boolean onQueryTextChange(String newText) {
                // Uppfæra auglýsinga listann
                AdvertisementItemsAdapter adapter = (AdvertisementItemsAdapter) mAdvertisementItems.getAdapter();
                searchQuery = newText;
                adapter.filter(searchQuery, categoryArray, categoryList, sellerArray, sellerList);
                return false;
            }
        });
    }

    private void setUpFilterCategory() {
        categoryArray = Tag.getTags();
        Arrays.sort(categoryArray);
        selectedCategories = new boolean[Tag.values().length];
        setUpCategoryListener();
    }

    private void setUpFilterSellers() {
        // Sækjum alla sellers sem eru til
        networkController.getSellers(new NetworkCallback<List<Seller>>() {
            @Override
            public void onError(String error) {
                Toast.makeText(AdvertisementsActivity.this, error, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(List<Seller> sellers) {
                sellerArray = new String[sellers.size()];
                selectedSellers = new boolean[sellers.size()];
                for (int i=0; i<sellerArray.length; i++) {
                    sellerArray[i] = sellers.get(i).getName();
                }
                Arrays.sort(sellerArray);
                setUpSellerListener();
            }
        });
    }

    private void setUpCategoryListener() {
        mCategoryDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdvertisementsActivity.this);
                builder.setTitle("Veldu flokka");
                builder.setMultiChoiceItems(categoryArray, selectedCategories, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            if (!categoryList.contains(which)) categoryList.add(which);
                        } else {
                            if (categoryList.contains(which))
                                categoryList.remove(Integer.valueOf(which));
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("Velja", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i = 0; i < categoryList.size(); i++) {
                            item = item + categoryArray[categoryList.get(i)];
                            if (i != categoryList.size() - 1) {
                                item = item + "\n";
                            }
                        }
                        mCategoryList.setText(item);
                        if (categoryList.size() == 0) {
                            mCategoryList.setVisibility(View.GONE);
                        } else {
                            mCategoryList.setVisibility(View.VISIBLE);
                        }
                        AdvertisementItemsAdapter adapter = (AdvertisementItemsAdapter) mAdvertisementItems.getAdapter();
                        adapter.filter(searchQuery, categoryArray, categoryList, sellerArray, sellerList);
                    }
                });
                builder.setNegativeButton("Hætta við", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Hreinsa", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < selectedCategories.length; i++) {
                            selectedCategories[i] = false;
                        }
                        mCategoryList.setText("");
                        mCategoryList.setVisibility(View.GONE);
                        categoryList.clear();
                        AdvertisementItemsAdapter adapter = (AdvertisementItemsAdapter) mAdvertisementItems.getAdapter();
                        adapter.filter(searchQuery, categoryArray, categoryList, sellerArray, sellerList);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void setUpSellerListener() {
        mSellerDropdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AdvertisementsActivity.this);
                builder.setTitle("Veldu söluaðila");
                builder.setMultiChoiceItems(sellerArray, selectedSellers, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            if (!sellerList.contains(which)) sellerList.add(which);
                        } else {
                            if (sellerList.contains(which))
                                sellerList.remove(Integer.valueOf(which));
                        }
                    }
                });
                builder.setCancelable(false);
                builder.setPositiveButton("Velja", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String item = "";
                        for (int i = 0; i < sellerList.size(); i++) {
                            item = item + sellerArray[sellerList.get(i)];
                            if (i != sellerList.size() - 1) {
                                item = item + "\n";
                            }
                        }
                        mSellerList.setText(item);
                        if (sellerList.size() == 0) {
                            mSellerList.setVisibility(View.GONE);
                        } else {
                            mSellerList.setVisibility(View.VISIBLE);
                        }
                        AdvertisementItemsAdapter adapter = (AdvertisementItemsAdapter) mAdvertisementItems.getAdapter();
                        adapter.filter(searchQuery, categoryArray, categoryList, sellerArray, sellerList);
                    }
                });
                builder.setNegativeButton("Hætta við", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                builder.setNeutralButton("Hreinsa", new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.O)
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < selectedSellers.length; i++) {
                            selectedSellers[i] = false;
                        }
                        mSellerList.setText("");
                        mSellerList.setVisibility(View.GONE);
                        sellerList.clear();
                        AdvertisementItemsAdapter adapter = (AdvertisementItemsAdapter) mAdvertisementItems.getAdapter();
                        adapter.filter(searchQuery, categoryArray, categoryList, sellerArray, sellerList);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void addSelectedCategory() {
        Bundle extras = getIntent().getExtras();
        String tag = (String) extras.get("selected_category");
        // Finnum indexin á tag-inu
        int index = -1;
        for (int i=0; i<categoryArray.length; i++) {
            if (categoryArray[i].equals(tag)) {
                index = i;
                break;
            }
        }
        if (index > -1) {
            // Bætum index-inum við CategoryList og selectedCategories
            categoryList.add(index);
            selectedCategories[index] = true;
            mCategoryList.setText(tag);
            AdvertisementItemsAdapter adapter = (AdvertisementItemsAdapter) mAdvertisementItems.getAdapter();
            adapter.filter(searchQuery, categoryArray, categoryList, sellerArray, sellerList);
            mCategoryList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onAdClick(int position) {
        if (!token.isEmpty()) {
            Intent intent = AdvertisementActivity.newIntent(AdvertisementsActivity.this);
            AdvertisementItemsAdapter adapter = (AdvertisementItemsAdapter) mAdvertisementItems.getAdapter();
            intent.putExtra("selected_ad", adapter.getAd(position));
            intent.putExtra("token", token);
            startActivityForResult(intent, REQUEST_CODE_AD);
        }
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}