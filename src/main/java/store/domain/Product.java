package store.domain;

public class Product {
    private final String name;
    private int price;
    private int quantity;
    private final Promotion promotion;

    private Product(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    public static Product of(String name, String price, String quantity, Promotion promotion) {
        return new Product(name, Integer.parseInt(price), Integer.parseInt(quantity), promotion);
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

    public Promotion getPromotion() {
        return promotion;
    }

    public void minusQuantity(int buyAmount) {
        quantity -= buyAmount;
    }
}
