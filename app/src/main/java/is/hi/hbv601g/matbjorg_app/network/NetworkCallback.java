package is.hi.hbv601g.matbjorg_app.network;

public interface NetworkCallback<T> {
    void onError(String error);
    void onResponse(T response);

}
