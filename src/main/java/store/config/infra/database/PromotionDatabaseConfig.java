package store.config.infra.database;

import store.infra.database.Database;
import store.infra.entity.PromotionEntity;

public interface PromotionDatabaseConfig {

    Database<PromotionEntity> getPromotionDatabase();
}
