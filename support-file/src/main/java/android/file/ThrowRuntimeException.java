package android.file;

class ThrowRuntimeException {

    static <T> void run(T t, String msg) {
        if (t == null) {
            throw new RuntimeException(msg);
        }
    }
}
