package store;

import java.util.ServiceLoader;
import store.common.exception.StoreExceptions;

public class Application {
    public static void main(String[] args) {
        getStoreApplication().run();
    }

    private static StoreApplication getStoreApplication() {
        return new DefaultStoreApplication();
    }
}
