package store.view;

import store.domain.Order;
import store.domain.Product;

import java.util.List;
import java.util.Map;

public class OutputView {
    // 1. 초반 인사말
    public void intro() {
        System.out.println("안녕하세요. W편의점입니다.");
    }

    // 2. 재고 상품 출력
    public void printProducts(List<Product> products) {
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();

        for (Product product : products) {
            String price = String.format("%,d", product.getPrice());
            int quantity = product.getQuantity();

            if (quantity == 0) {
                System.out.println("- " + product.getName() + " " + price + "원 " + "재고 없음");
            } else {
                // promotion이 없음 TODO : 차후 METHOD로 분리
                if (product.getPromotion() == null) {
                    System.out.println("- " + product.getName() + " " + price + "원 " + product.getQuantity() + "개");
                } else { // promotion이 있음
                    System.out.println("- " + product.getName() + " " + price + "원 " + product.getQuantity() + "개 " + product.getPromotion().getName());
                }
            }
        }
        System.out.println();
    }

    // 3. 영수증 출력 : 증정, 행사 할인, 멤버십 할인, 총 금액 으로 나눠져 있다.
    public void printReceipt(List<Order> orders,
                             Map<Product, Integer> gifts,
                             int totalPrice,
                             int totalCount,
                             int disCountPrice,
                             int membership,
                             int finalPrice) {
        System.out.println("=============W 편의점=============");
        System.out.println("상품명       수량      금액");
        // 1. 상품들 프린트
        for (Order order : orders) {
            String price = String.format("%,d", order.getAmount() * order.getProduct().getPrice());
            System.out.println(order.getProduct().getName() + "     " + order.getAmount() + "     " + price);
        }

        // 2. 증정 프린트
        System.out.println("=============증 정=============");
        gifts.forEach((product, quantity) -> {
            System.out.println(product.getName() + "     " + quantity);
        });

        // 3. 총 구매액 프린트
        System.out.println("===============================");
        System.out.println("총구매액" + "     " + totalCount + "     " + String.format("%,d", totalPrice));
        System.out.println("행사할인" + "            " + "-" + String.format("%,d", disCountPrice));
        System.out.println("멤버십할인" + "            " + "-" + String.format("%,d", membership));
        System.out.println("내실돈" + "            " + String.format("%,d", finalPrice));

        System.out.println();
    }
}
