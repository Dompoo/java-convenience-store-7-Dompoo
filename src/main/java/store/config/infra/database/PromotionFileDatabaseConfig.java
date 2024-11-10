package store.config.infra.database;

import store.infra.database.Database;
import store.infra.database.PromotionFileDatabase;
import store.infra.entity.PromotionEntity;

public class PromotionFileDatabaseConfig implements PromotionDatabaseConfig {

    private final Database<PromotionEntity> promotionDatabase;

    public PromotionFileDatabaseConfig() {
        this.promotionDatabase = new PromotionFileDatabase();
    }

    @Override
    public Database<PromotionEntity> getPromotionDatabase() {
        return this.promotionDatabase;
    }
}
