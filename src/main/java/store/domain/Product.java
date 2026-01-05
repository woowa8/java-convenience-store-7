package store.domain;

public class Product {
    private final String name;
    private final int price;
    private final int quantity;
    private final Promotions promotion;

    public Product(String name, int price, int quantity, Promotions promotions) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotions;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public Promotions getPromotion() {
        return promotion;
    }
}
