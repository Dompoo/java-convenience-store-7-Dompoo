package store;

public class Application {
    public static void main(String[] args) {
        getStoreApplication().run();
    }

    private static StoreApplication getStoreApplication() {
        return new DefaultStoreApplication();
    }
}
