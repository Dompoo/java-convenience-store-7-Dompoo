package store.config.infra.repository;

import java.util.ArrayList;
import java.util.List;
import store.infra.repository.Repository;

public class RepositoriesConfig {

    private final List<Repository<?>> repositories = new ArrayList<>();

    public RepositoriesConfig(final ProductRepositoryConfig productRepositoryConfig) {
        repositories.add(productRepositoryConfig.getProductRepository());
    }

    public List<Repository<?>> getRepositories() {
        return repositories;
    }
}
