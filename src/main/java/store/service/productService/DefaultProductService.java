package store.service.productService;

import java.util.List;
import store.common.dto.response.ProductResponse;
import store.domain.Product;
import store.infra.repository.Repository;

public class DefaultProductService implements ProductService {

    private final Repository<Product> productRepository;

    public DefaultProductService(final Repository<Product> productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductResponse> readAllProducts() {
        List<Product> products = productRepository.findAll();
        return ProductResponse.fromList(products);
    }
}
