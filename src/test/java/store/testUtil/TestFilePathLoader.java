package store.testUtil;

import store.infra.filePathLoader.FilePathLoader;

public class TestFilePathLoader implements FilePathLoader {

    private static final String PRODUCT_FILE_PATH = "src/test/resources/products_test.md";
    private static final String PROMOTION_FILE_PATH = "src/test/resources/promotions_test.md";

    @Override
    public String getProductFilePath() {
        return PRODUCT_FILE_PATH;
    }

    @Override
    public String getPromotionFilePath() {
        return PROMOTION_FILE_PATH;
    }
}
