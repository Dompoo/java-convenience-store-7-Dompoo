package store.testUtil.testDouble;

import java.util.ArrayList;
import java.util.List;
import store.infra.database.Database;
import store.infra.entity.PromotionEntity;

public class PromotionFileDatabaseFake implements Database<PromotionEntity> {

    private final List<PromotionEntity> promotionEntities = new ArrayList<>();

    public void setPromotionEntities(List<PromotionEntity> promotionEntities) {
        this.promotionEntities.addAll(promotionEntities);
    }

    @Override
    public List<PromotionEntity> readAll() {
        return promotionEntities;
    }
}
