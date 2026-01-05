package store.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Order {
    private final Product product;
    private final int amount;
    private final LocalDate orderDate;

    public Order(Product product, int amount, LocalDate orderDate) {
        this.product = product;
        this.amount = amount;
        this.orderDate = orderDate;
    }

    public Order of(Product product, int amount, String orderDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return new Order(product, amount, LocalDate.parse(orderDate, formatter));
    }

    public Product getProduct() {
        return product;
    }

    public int getAmount() {
        return amount;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }
}
