package store.repository;

import store.domain.Order;

import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final List<Order> orders;

    public OrderRepository() {
        this.orders = new ArrayList<>();
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void clear() {
        orders.clear(); // 또는 orders.clear()
    }

//    // 프로모션 까지 있는것 찾기
//    public Order findOrderHavePromotion(String productName, String promotionName) {
//        // TODO : Stream으로 두 가지 조건 넣는법 알아두기
//        return orders.stream()
//                .filter(order -> order.getProductName().equals(productName))
//                .filter(order -> order.getPromotion().getName().equals(promotionName))
//                .findFirst().orElse(null);
//    }
//
//    // 프로모션 없는 것 찾기
//    public Order findOrderNoPromotion(String productName) {
//        return orders.stream()
//                .filter(order -> order.getProduct().getName().equals(productName))
//                .findFirst().orElse(null);
//    }
//
//    // 전부 찾기
//    public List<Order> findProducts(String productName) {
//        return orders.stream()
//                .filter(order -> order.getProduct().getName().equals(productName))
//                .toList();
//    }
//
//    // 총 구매 수량 가져오기
//    public int countOrders() {
//        int count = 0;
//
//        for (Order order : orders) {
//            count += order.getQuantity();
//        }
//
//        return count;
//    }
}
