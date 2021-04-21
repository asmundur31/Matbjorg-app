package is.hi.hbv601g.matbjorg_app.network;

import android.content.Context;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import java.util.Base64;
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
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import java.util.HashMap;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.models.Advertisement;
import is.hi.hbv601g.matbjorg_app.models.Buyer;
import is.hi.hbv601g.matbjorg_app.models.Location;
import is.hi.hbv601g.matbjorg_app.models.Order;
import is.hi.hbv601g.matbjorg_app.models.OrderItem;
import is.hi.hbv601g.matbjorg_app.models.Review;
import is.hi.hbv601g.matbjorg_app.models.Seller;
import is.hi.hbv601g.matbjorg_app.models.Tag;
import is.hi.hbv601g.matbjorg_app.models.User;

public class NetworkController {
    private static final String TAG = "NetworkController";
    // public static final String URL_REST = "https://matbjorg.herokuapp.com/rest/";
    public static final String URL_REST = "http://10.0.2.2:8080/rest/";
    // Remote linkur "https://matbjorg.herokuapp.com/rest/"
    // Local linkur "http://10.0.2.2:8080/rest/"

    private Context context;

    public NetworkController(Context context) {
        this.context = context;
    }

    public void getAdvertisements(NetworkCallback<List<Advertisement>> networkCallback) {
        String url = URL_REST + "advertisements";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        LocalDateTime lt = LocalDateTime.parse(json.getAsString());
                        return lt;
                    }
                }).create();
                String json = response.toString();
                Type collectionType = new TypeToken<List<Advertisement>>(){}.getType();
                List<Advertisement> ads = gson.fromJson(json, collectionType);
                networkCallback.onResponse(ads);
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

    public void getBuyers(NetworkCallback<List<Buyer>> networkCallback) {
        String url = URL_REST + "buyers";
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
        String url = URL_REST + "sellers";
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
        String url = URL_REST + String.format("login?email=%s&password=%s", email, password);
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
                Log.d(TAG, error.toString());
                networkCallback.onError("Gekk ekki að skrá notanda inn");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }

    public void signup(NetworkCallback<User> networkCallback, String type, String name, String email, String password, List<Location> locations) {
        String url = URL_REST + String.format("signup?type=%s&name=%s&email=%s&password=%s", type, name, email, password);
        JSONArray loc = new JSONArray();
        for(int i=0; i<locations.size(); i++) {
            JSONObject object = new JSONObject();
            try {
                object.put("longitude", locations.get(i).getLongitude());
                object.put("latitude", locations.get(i).getLatitude());
                object.put("name", locations.get(i).getName());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            loc.put(object);
        }
        JSONObject postData = new JSONObject();
        try {
            postData.put("locations", loc);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d(TAG, postData.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Gson gson = new Gson();
                User user = gson.fromJson(response.toString(), new TypeToken<User>(){}.getType());
                networkCallback.onResponse(user);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki skráð notanda");
              }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }
            
    public void getActiveOrder(NetworkCallback<Order> networkCallback, String token) {
        String url = URL_REST + "orders/active?token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        LocalDateTime lt = LocalDateTime.parse(json.getAsString());
                        return lt;
                    }
                }).create();
                String json = response.toString();
                Order order = gson.fromJson(json, Order.class);
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
        String url = URL_REST + "orders";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
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
        String url = URL_REST + "orders/confirm?token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        LocalDateTime lt = LocalDateTime.parse(json.getAsString());
                        return lt;
                    }
                }).create();
                String json = response.toString();
                Order order = gson.fromJson(json, Order.class);
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
        String url = URL_REST + "orders/changeItem/" + id + "?amount=" + newAmount + "&token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        LocalDateTime lt = LocalDateTime.parse(json.getAsString());
                        return lt;
                    }
                }).create();
                String json = response.toString();
                Order order = gson.fromJson(json, Order.class);
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
        String url = URL_REST + "orders/changeItem/" + id + "?amount=" + 0 + "&token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        LocalDateTime lt = LocalDateTime.parse(json.getAsString());
                        return lt;
                    }
                }).create();
                String json = response.toString();
                Order order = gson.fromJson(json, Order.class);
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
        String url = URL_REST + "orders/" + String.format("buyer?buyerId=%s", buyerId.toString());;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
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
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void addAdvertisement(NetworkCallback<Advertisement> networkCallback, Long sellerId, String name,
                                 String description, double originalAmount, double price, LocalDateTime expireDate, List<String> tags, Location location, Bundle picture) {
        String url = URL_REST + "advertisements/" + String.format("add?sellerId=%s&name=%s&description=%s&originalAmount=%s&price=%s&expireDate=%s&locationId=%s", sellerId.toString(), name, description, ""+originalAmount, ""+price, expireDate.toString(), location.getId());
        JSONArray t = new JSONArray();
        for(int i=0; i<tags.size(); i++) {
            t.put(tags.get(i));
        }
        JSONObject postData = new JSONObject();
        try {
            postData.put("tags", t);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Bitmap bm = (Bitmap) picture.get("data");
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos); //bm is the bitmap object
        byte[] b = baos.toByteArray();
        String encodedImage = new String(Base64.getEncoder().encode(b));
        JSONArray pic = new JSONArray();
        pic.put(encodedImage);
        try {
            postData.put("pic", pic);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        LocalDateTime lt = LocalDateTime.parse(json.getAsString());
                        return lt;
                    }
                }).create();
                String json = response.toString();
                Type collectionType = new TypeToken<Advertisement>(){}.getType();
                Advertisement ad = gson.fromJson(json, collectionType);
                networkCallback.onResponse(ad);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að setja inn auglýsingu");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getLocations(NetworkCallback<List<Location>> networkCallback) {
        String url = URL_REST + "locations";
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                Gson gson = new Gson();
                List<Location> locations = gson.fromJson(response.toString(), new TypeToken<List<Location>>(){}.getType());
                networkCallback.onResponse(locations);
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

    public void getLocationsBySeller(NetworkCallback<List<Location>> networkCallback, long sellerId) {
        String url = URL_REST + String.format("locations/%s", sellerId);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                Gson gson = new Gson();
                List<Location> locations = gson.fromJson(response.toString(), new TypeToken<List<Location>>(){}.getType());
                networkCallback.onResponse(locations);
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

    public void addToCart(NetworkCallback<OrderItem> networkCallback, long id, double amount, String token) {
        String url = URL_REST + "orders/addToCart/"+ id + "?amount=" + amount + "&token=" + token;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                            throws JsonParseException {
                        LocalDateTime lt = LocalDateTime.parse(json.getAsString());
                        return lt;
                    }
                }).create();
                String json = response.toString();
                OrderItem orderItem = gson.fromJson(json, OrderItem.class);
                networkCallback.onResponse(orderItem);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að bæta við körfu");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getSellersAds(NetworkCallback<List<Advertisement>> networkCallback, long sellerId) {
        String url = URL_REST +  "advertisements/" + String.format("seller?sellerId=%s", ""+sellerId);
        JsonArrayRequest request =  new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                Gson gson = new GsonBuilder().registerTypeAdapter(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                    @Override
                    public LocalDateTime deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
                        LocalDateTime lt = LocalDateTime.parse(json.getAsString());
                        return lt;
                    }
                }).create();
                String json = response.toString();
                Type collectionType = new TypeToken<List<Advertisement>>() {
                }.getType();
                List<Advertisement> advertisements = gson.fromJson(json, collectionType);
                networkCallback.onResponse(advertisements);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að sækja auglýsingar");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }

    public void addReview(NetworkCallback<Review> networkCallback, long sellerId, String token, String review, double rating) {
        String url = URL_REST +  "reviews/add";
        Map<String, String> params = new HashMap<String, String>();
        params.put("sellerId", ""+sellerId);
        params.put("token", token);
        params.put("review", review);
        params.put("rating", ""+rating);
        JSONObject postData = new JSONObject(params);
        JsonObjectRequest request =  new JsonObjectRequest(Request.Method.POST, url, postData, new Response.Listener<JSONObject>() {
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                Gson gson = new Gson();
                Review review = gson.fromJson(response.toString(), Review.class);
                networkCallback.onResponse(review);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að sækja auglýsingar");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }

    public void getReviewsBySeller(NetworkCallback<List<Review>> networkCallback, long sellerId) {
        String url = URL_REST +  "reviews/" + sellerId;

        JsonArrayRequest request =  new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.d(TAG, response.toString());
                Gson gson = new Gson();
                List<Review> reviews = gson.fromJson(response.toString(), new TypeToken<List<Review>>(){}.getType());
                networkCallback.onResponse(reviews);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, error.toString());
                networkCallback.onError("Gekk ekki að sækja auglýsingar");
            }
        });
        NetworkSingleton.getInstance(context).addToRequestQueue(request);
    }
}
