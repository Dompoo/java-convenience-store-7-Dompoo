package store.infra.database;

import java.util.List;
import store.infra.entity.DatabaseEntity;

public interface Database<T extends DatabaseEntity> {

    List<T> readAll();

    void updateAll(List<T> objects);
}
