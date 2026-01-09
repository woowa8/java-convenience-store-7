package store.domain;

public class Gift {
    private final String name;
    private int price;
    private int quantity;
    private final Promotion promotion;

    private Gift(String name, int price, int quantity, Promotion promotion) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotion = promotion;
    }

    // TODO : 원래 0으로 처리하면 안되는데 어차피 같은 상품군 하나씩이라..
    public static Gift of(Product product) {
        return new Gift(product.getName(),  product.getPrice(), 0, product.getPromotion());
    }

    public void addQuantity(int addAmount) {
        quantity += addAmount;
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
}
