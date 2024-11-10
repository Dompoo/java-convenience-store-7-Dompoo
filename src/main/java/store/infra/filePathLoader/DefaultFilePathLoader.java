package store.infra.filePathLoader;

public class DefaultFilePathLoader implements FilePathLoader {

    private static final String PRODUCT_FILE_PATH = "src/main/resources/products.md";
    private static final String PROMOTION_FILE_PATH = "src/main/resources/promotions.md";

    @Override
    public String getProductFilePath() {
        return PRODUCT_FILE_PATH;
    }

    @Override
    public String getPromotionFilePath() {
        return PROMOTION_FILE_PATH;
    }
}
