package store.infra.database;

import java.util.Map;
import store.config.infra.filePathLoader.FilePathLoaderConfig;
import store.infra.entity.ProductEntity;

public class ProductFileDatabase extends FileDatabase<ProductEntity> {

    @Override
    protected ProductEntity convertLineToObject(final Map<String, String> dataMap) {
        return ProductEntity.from(dataMap);
    }

    @Override
    protected String getFilePath() {
        return FilePathLoaderConfig.getFilePathLoader().getProductFilePath();
    }
}
