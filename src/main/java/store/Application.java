package store;

import java.util.ServiceLoader;
import store.common.exception.StoreExceptions;

public class Application {
    public static void main(String[] args) {
        getStoreApplication().run();
    }

    private static StoreApplication getStoreApplication() {
        ServiceLoader<StoreApplication> loader = ServiceLoader.load(StoreApplication.class);
        return loader.findFirst().orElseThrow(StoreExceptions.APPLICATION_LOAD_FAIL::get);
    }
}
