package store.repository;

import store.domain.Product;
import store.domain.Promotions;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {
    private List<Product> products;

    public ProductRepository() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void addProduct(Product product) {
        this.products.add(product);
    }

    public Product findProductByNameAndPromotions(String name, Promotions promotions) {
        // 1. 일단 product에 해당하는 것을 찾는다. 프로모션이 있는 것은 두 개가 나올 것이다.
        List<Product> products = this.products.stream()
                .filter(product -> product.getName().equals(name))
                .toList();    // TODO : 여러개 가져오려면 바로 toList();

        return products.stream()
                .filter(product -> product.getPromotion().equals(promotions))
                .findFirst().orElse(null);
    }

    public List<Product> findProductByName(String name) {
        return this.products.stream()
                .filter(product -> product.getName().equals(name))
                .toList();
    }
}
