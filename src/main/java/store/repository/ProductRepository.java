package store.repository;

import store.domain.Order;
import store.domain.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private final List<Product> products;

    public ProductRepository() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public List<Product> findByProductName(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .toList();
    }

    // 프로모션 까지 있는것 찾기
    public Product findProductHavePromotion(String productName, String promotionName) {
        // TODO : Stream으로 두 가지 조건 넣는법 알아두기
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .filter(product -> product.getPromotion().getName().equals(promotionName))
                .findFirst().orElse(null);
    }

    // 프로모션 없는 것 찾기
    public Product findProductNoPromotion(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .findFirst().orElse(null);
    }

    public void minusStock(Product product, int amount) {
        product.minusQuantity(amount);
    }
}
