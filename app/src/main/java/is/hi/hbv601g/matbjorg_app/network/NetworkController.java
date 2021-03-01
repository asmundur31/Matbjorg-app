package is.hi.hbv601g.matbjorg_app.network;

import java.util.ArrayList;
import java.util.List;

import is.hi.hbv601g.matbjorg_app.models.Buyer;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class NetworkController {
    private static final String TAG = "NetworkController";
    private OkHttpClient client;
    private List<Buyer> buyers = new ArrayList<Buyer>();

    public NetworkController() {
        client = new OkHttpClient();
    }

    public void getBuyers(Callback cb) {
        String url = "https://matbjorg.herokuapp.com/rest/buyers";
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(cb);
    }
}
