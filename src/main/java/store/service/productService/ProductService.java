package store.service.productService;

import java.util.List;
import store.common.dto.response.ProductResponse;

public interface ProductService {

    List<ProductResponse> readAllProducts();
}
