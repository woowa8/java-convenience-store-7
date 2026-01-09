package store.view;

import store.domain.Gift;
import store.domain.Order;
import store.domain.Product;
import store.dto.ReceiptResponseDto;

import java.util.List;

public class OutputView {
    // 인트로
    public void intro() {
        System.out.println("안녕하세요. W편의점입니다.");
        System.out.println("현재 보유하고 있는 상품입니다.");
        System.out.println();
    }

    // 메뉴 출력
    public void printMenu(List<Product> products) {
        for (Product product : products) {
            String name = product.getName();
            String money = String.format("%,d", product.getPrice());
            String quantity = String.format("%,d", product.getQuantity());
            String promotion = product.getPromotion() == null ? "" : product.getPromotion().getName();

            if("0".equals(quantity)){
                System.out.println("- " + name + " " + money + "원 " + "재고 없음");
            }else{
                System.out.println("- " + name + " " + money + "원 " + quantity + "개 " + promotion);
            }
        }
        System.out.println();
    }

    public void printReceipt(ReceiptResponseDto response){
        System.out.println("================W 편의점===================");

        // 구매 관련 출력
        System.out.println("상품명" + "      " + "수량 금액");
        List<Order> orderList = response.orders();
        for (Order order : orderList) {
            String name = order.getProductName();
            int quantity = order.getQuantity();
            String money = String.format("%,d", order.getPrice());

            System.out.println(name + "      " + quantity + "  " + money);
        }

        // 증정 출력
        System.out.println("================증 정=================");
        List<Gift> gifts = response.gifts();
        for (Gift gift : gifts) {
            String name = gift.getName();
            int quantity = gift.getQuantity();

            System.out.println(name + "      " + quantity);
        }

        // 총 관련 출력
        int totalCount = response.totalCount();
        String totalAmount = String.format("%,d", response.totalAmount());
        String saleAmount = String.format("%,d", response.saleAmount());
        String membership = String.format("%,d", response.membership());
        String finalAmount = String.format("%,d", response.finalAmount());

        System.out.println("===================================");
        System.out.println("총구매액" + "      " + totalCount + "  " + totalAmount);
        System.out.println("행사할인" + "      " + "-" + saleAmount);
        System.out.println("멤버십할인" + "      " + "-" + membership);
        System.out.println("내실돈" + "      " + finalAmount);

        System.out.println();
    }
}
