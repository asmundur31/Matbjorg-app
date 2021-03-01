package is.hi.hbv601g.matbjorg_app.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.R;
import is.hi.hbv601g.matbjorg_app.models.Buyer;
import is.hi.hbv601g.matbjorg_app.network.NetworkController;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class HomeFragment extends Fragment {
    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private NetworkController networkController = new NetworkController();
    private List<Buyer> mBuyers = new ArrayList<Buyer>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        networkController.getBuyers(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e(TAG, "Gekk ekki að sækja gögn");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                String json = response.body().string();
                Gson gson = new Gson();
                mBuyers = gson.fromJson(json, new TypeToken<List<Buyer>>(){}.getType());
                Log.d(TAG, String.valueOf(mBuyers.size()));
                for (int i=0; i<mBuyers.size(); i++) {
                    Log.d(TAG, mBuyers.get(i).toString());
                }
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}