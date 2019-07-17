package android.permission;

public interface Request {
    Request checkPermission(String... permission);

    Request setCallback(RequestCallback callback);

    Request rationale(RationaleListener rationaleListener);

    void start();
}
