package is.hi.hbv601g.matbjorg_app.network;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;

import java.io.IOException;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.models.Buyer;
import is.hi.hbv601g.matbjorg_app.models.Seller;

public class NetworkController {
    private static final String TAG = "NetworkController";
    public static final String MATBJORG_URL_REST = "https://matbjorg.herokuapp.com/rest/";
    public static final String LOCAL_URL_REST = "http://localhost:8080/rest/";

    private Context context;

    public NetworkController(Context context) {
        this.context = context;
    }

    public void getBuyers(NetworkCallback<List<Buyer>> networkCallback) {
        String url = MATBJORG_URL_REST + "buyers";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                Gson gson = new Gson();
                String json = response.toString();
                List<Buyer> buyers = gson.fromJson(json, new TypeToken<List<Buyer>>(){}.getType());
                networkCallback.onResponse(buyers);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                networkCallback.onError("Gekk ekki að sækja gögn");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getSellers(NetworkCallback<List<Seller>> networkCallback) {
        String url = MATBJORG_URL_REST + "sellers";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                Gson gson = new Gson();
                List<Seller> sellers = gson.fromJson(response.toString(), new TypeToken<List<Seller>>(){}.getType());
                networkCallback.onResponse(sellers);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
                networkCallback.onError("Gekk ekki að sækja gögn");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }
}
