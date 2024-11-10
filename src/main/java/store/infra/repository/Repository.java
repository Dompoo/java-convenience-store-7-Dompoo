package store.infra.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T> {

    List<T> findAll();

    Optional<T> findByName(String name);

    void update(T object);

    void updateDatabaseInBatch();
}
