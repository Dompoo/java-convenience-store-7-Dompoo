package store.infra.database;

import java.util.Map;
import store.config.infra.filePathLoader.FilePathLoaderConfig;
import store.infra.entity.PromotionEntity;

public class PromotionFileDatabase extends FileDatabase<PromotionEntity> {

    @Override
    protected PromotionEntity convertLineToObject(final Map<String, String> dataMap) {
        return PromotionEntity.from(dataMap);
    }

    @Override
    protected String getFilePath() {
        return FilePathLoaderConfig.getFilePathLoader().getPromotionFilePath();
    }
}
