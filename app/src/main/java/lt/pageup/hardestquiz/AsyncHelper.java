package lt.pageup.hardestquiz;

public class AsyncHelper {

    public static void runAsync(Runnable runnable) {
        new Thread(runnable).start();
    }
}
