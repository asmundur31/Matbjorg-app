package is.hi.hbv601g.matbjorg_app.network;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.models.Buyer;
import is.hi.hbv601g.matbjorg_app.models.Order;
import is.hi.hbv601g.matbjorg_app.models.OrderItem;
import is.hi.hbv601g.matbjorg_app.models.Seller;
import is.hi.hbv601g.matbjorg_app.models.User;

public class NetworkController {
    private static final String TAG = "NetworkController";
    public static final String MATBJORG_URL_REST = "https://matbjorg.herokuapp.com/rest/";
    public static final String LOCAL_REST = "http://10.0.2.2:8080/rest/";

    private Context context;

    public NetworkController(Context context) {
        this.context = context;
    }

    public void getBuyers(NetworkCallback<List<Buyer>> networkCallback) {
        String url = LOCAL_REST + "buyers";
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
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að sækja gögn");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getSellers(NetworkCallback<List<Seller>> networkCallback) {
        String url = LOCAL_REST + "sellers";
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
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að sækja gögn");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }

    public void login(NetworkCallback<User> networkCallback, String email, String password) {
        String url = LOCAL_REST + String.format("login?email=%s&password=%s", email, password);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Gson gson = new Gson();
                User user = gson.fromJson(response.toString(), User.class);
                networkCallback.onResponse(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að skrá notanda inn");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getActiveOrder(NetworkCallback<Order> networkCallback, String token) {
        String url = LOCAL_REST + "orders/active?token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Gson gson = new Gson();
                Order order = gson.fromJson(response.toString(), Order.class);
                networkCallback.onResponse(order);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að sækja gögn");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }
    
  public void getOrders(NetworkCallback<List<Order>> networkCallback) {
        String url = LOCAL_REST + "orders";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, response.toString());
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        LocalDateTime lt = LocalDateTime.parse(json.getAsString());
                        return lt;
                    }
                }).create();
                String json = response.toString();
                Type collectionType = new TypeToken<List<Order>>(){}.getType();
                List<Order> orders = gson.fromJson(json, collectionType);
                networkCallback.onResponse(orders);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að sækja gögn");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }
  
    public void confirmOrder(NetworkCallback<Order> networkCallback, String token) {
        String url = LOCAL_REST + "orders/confirm?token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Gson gson = new Gson();
                Order order = gson.fromJson(response.toString(), Order.class);
                networkCallback.onResponse(order);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að staðfesta pöntun");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }

    public void changeOrderItem(NetworkCallback<Order> networkCallback, long id, double newAmount, String token) {
        String url = LOCAL_REST + "orders/changeItem/" + id + "?amount=" + newAmount + "&token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Gson gson = new Gson();
                Order order = gson.fromJson(response.toString(), Order.class);
                networkCallback.onResponse(order);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að breyta orderItem");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }

    public void deleteOrderItem(NetworkCallback<Order> networkCallback, long id, String token) {
        String url = LOCAL_REST + "orders/changeItem/" + id + "?amount=" + 0 + "&token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Gson gson = new Gson();
                Order order = gson.fromJson(response.toString(), Order.class);
                networkCallback.onResponse(order);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að eyða orderItem");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }
  
    public void getBuyerOrders(NetworkCallback<List<Order>> networkCallback, Long buyerId) {
        String url = LOCAL_REST + "orders/" + String.format("buyer?buyerId=%s", buyerId.toString());;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                Log.e(TAG, response.toString());
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        LocalDateTime lt = LocalDateTime.parse(json.getAsString());
                        return lt;
                    }
                }).create();
                String json = response.toString();
                Type collectionType = new TypeToken<List<Order>>(){}.getType();
                List<Order> orders = gson.fromJson(json, collectionType);
                networkCallback.onResponse(orders);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að sækja gögn");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }
}
