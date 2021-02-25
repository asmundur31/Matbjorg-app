package is.hi.hbv601g.matbjorg_app.network;

import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.MainActivity;
import is.hi.hbv601g.matbjorg_app.models.Buyer;
import is.hi.hbv601g.matbjorg_app.ui.home.HomeFragment;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkController {
    private static final String TAG = "NetworkController";
    private OkHttpClient client;
    private List<Buyer> buyers = new ArrayList<Buyer>();

    public NetworkController() {
        client = new OkHttpClient();
    }

    public List<Buyer> getBuyers() {
        String url = "https://matbjorg.herokuapp.com/rest/buyers";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                e.printStackTrace();
                Log.i(TAG, "Gekk ekki að sækja gögn");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if(response.isSuccessful()) {
                    String json = response.body().string();
                    Log.i(TAG, json);
                    Gson gson = new Gson();
                    Type buyerListType = new TypeToken<ArrayList<Buyer>>(){}.getType();
                    buyers = gson.fromJson(json, buyerListType);
                    Log.i(TAG, String.valueOf(buyers.size()));
                }
            }
        });
        return buyers;
    }
}
