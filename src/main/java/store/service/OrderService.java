package store.service;

import store.domain.Product;
import store.repository.OrderRepository;
import store.repository.ProductRepository;
import store.repository.PromotionsRepository;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderService {
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PromotionsRepository promotionsRepository;

    public OrderService(OrderRepository orderRepository, ProductRepository productRepository, PromotionsRepository promotionsRepository) {
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.promotionsRepository = promotionsRepository;
    }

    // List<List<String>> inputOrders 받아서 넣는 부분
    public void saveOrders(List<List<String>> orders) {
        Map<Product, Integer> gifts = new HashMap<>();

        // 1. 상품 이름, 상품 수량 분리
        for (List<String> order : orders) {
            String productName = order.get(0);
            int amount = Integer.parseInt(order.get(1));   // 전체 주문 갯수

            // 2. 예외 : 상품 이름으로 상품 찾아서 있는 상품인지 확인
            if (productRepository.findProductByName(productName) == null) {
                throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
            }

            // 계산을 통해 만든 로직 필요

        }
    }
    // TODO : 프로모션 적용 & 일반 적용 로직 만들기
    public int[] calculateGifts(String productName, int amount) {
        // 1. 일단 String productName을 통해 이게 프로모션 안에 있는지 보기
        Product product = getProductsHavePromotion(productName);
        if(product == null){   // 프로모션이 없는 상품
            return calculateStandard(productName, amount);
        }

        // TODO : 프로모션 날짜랑 벗어나 있으면 프로모션 적용 불가라서 일반으로 계산
        LocalDate startDate = product.getPromotion().getStartDate();
        LocalDate endDate = product.getPromotion().getEndDate();

        if(!isPromotionDay(startDate, endDate)){
            return calculateStandard(productName, amount);
        }

        // 3. 프로모션 안에 있으면 계산 시작.
        // 4. 구매 가능 프로모션 갯수 : min(amount/프로모션 단위, 프로모션 갯수/프로모션 단위)
        int get = product.getPromotion().getGetProduct();
        int buy = product.getPromotion().getBuyProduct();
        int quantity = product.getQuantity();

        int promotionNumber = Math.min(amount/(get + buy), quantity/(get + buy));
        // 5. 구매하는 프로모션 갯수 : 4 * 프로모션 단위
        int promotionCount = promotionNumber * (get + buy);
        // 6. 남은 갯수 : 전체 - 5.
        int remainCount = amount - promotionCount;
        // 7. 공짜 아이템 갯수 : 4에서 나온 것 * get 갯수
        int freeGift = promotionNumber * get;

        // return은 프로모션 구매 갯수, 일반 구매 갯수, 공짜 아이템 순
        return new int[]{promotionCount, remainCount, freeGift};
    }

    private int[] calculateStandard(String productName, int amount) {
        // 1. 상품 구하기
        Product product = getProductsNotHavePromotion(productName);

        int standardCount = product.getQuantity();

        if(standardCount > amount){
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
        return new int[]{0, amount, 0};
    }

    // 프로모션 상품 안에 있는지 확인
    public Product getProductsHavePromotion(String name) {
        List<Product> products = productRepository.findProductByName(name);
        for (Product product : products) {
            if (product.getPromotion() != null) {
                return product;
            }
        }
        return null;
    }

    public Product getProductsNotHavePromotion(String name) {
        List<Product> products = productRepository.findProductByName(name);
        for (Product product : products) {
            if (product.getPromotion() == null) {
                return product;
            }
        }
        return null;
    }

    // 오늘 날짜가 해당 프로모션에 해당하는 날짜 사이인 경우 출력하기
    public boolean isPromotionDay(LocalDate startDate, LocalDate endDate) {
        LocalDate now = LocalDate.now();

        if (startDate.isAfter(now) && endDate.isBefore(now)) {
            return true;
        }
        return false;
    }
}
