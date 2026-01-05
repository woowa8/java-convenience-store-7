package store.repository;

import store.domain.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private List<Order> orders;

    public OrderRepository() {
        this.orders = new ArrayList<>();
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order){
        this.orders.add(order);
    }
}
