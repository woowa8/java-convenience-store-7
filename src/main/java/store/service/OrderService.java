package store.service;

import store.domain.Product;
import store.domain.Promotions;
import store.repository.OrderRepository;
import store.repository.ProductRepository;
import store.repository.PromotionsRepository;
import store.util.InitData;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
- 기능 구현
    - [ ] 오늘 날짜가 해당 프로모션에 해당하는 날짜 사이인 경우
    - [ ] 상품 재고 추가하는 기능
    - [ ] 상품 재고 깎는 기능
    - [ ] 주문에서 삭제하는 기능
    - [ ] 주문 추가하는 기능
    - [ ] 프로모션 상품 재고 판단하는 기능
    - [ ] 해당 상품이 프로모션 상품인지 판단하는 기능
    - [ ] 해당 상품이 프로모션 상품인데 수량이 다 적용 되었는지 판단하는 기능
- 예외 처리
    - [ ] 존재하지 않는 상품을 입력한 경우
    - [ ] 구매 수량이 재고 수량을 초과해서, 구매가 불가능한 경우
 */
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

            Product product = getProductsHavePromotion(productName);    // 프로모션 있는 상품
            if (product == null) {    // 프로모션 상품이 없는 상품
                int quantity = productRepository.findProductByNameAndPromotions(productName, null).getQuantity();

                // 예외 : 구매 수량보다 입력 값이 많을 경우
                if (quantity < amount) {
                    throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
                }
            } else {
                int quantity = getPromotionsCount(product, amount);   // 프로모션 적용 가능 수량
                // TODO : 전체 - 프로모션 적용 가능 해서 남은 수량 구하기
                int remainQuantity = amount - quantity;
                // TODO : 프로모션 남은 수량 구하기
                int remainPromotions = product.getQuantity() - quantity;
                // TODO : 일반에서 차감해야 하는 수량 구하기, 여기서 프로모션으로만 처리가 가능할 경우 일반에서 빼야 하는건 없다.
                int standardQuantity = (remainQuantity - remainPromotions) > 0 ? (remainQuantity - remainPromotions) : 0;
                // TODO : 위에서 계산했을때 남은게 일반보다 많으면 에러
                int standard = productRepository.findProductByNameAndPromotions(productName, null).getQuantity();

                if (standard < standardQuantity) {
                    throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
                }

                // TODO : MAP으로 프로모션 적용에 추가
                gifts.put(product, gifts.getOrDefault(product, 0) + quantity);
            }
        }

        // 3. 예외 : 프로모션 구매 가능 수량 + 일반 가능 수량이 넘어서는지 확인 (재고) *** TODO : 이건 확인 필요
        // 3. 오늘 날짜 추출
        // 4, 주문 저장
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

    // 프로모션 상품 적용 가능 수량 구하기, promoiton이 있는 상품만 들어온다.
    public int getPromotionsCount(Product product, int quantity) {
        Promotions promotions = product.getPromotion();

        return (quantity / (promotions.getGetProduct() + promotions.getBuyProduct())) * promotions.getBuyProduct();
    }

    // 오늘 날짜가 해당 프로모션에 해당하는 날짜 사이인 경우 출력하기
    public boolean isPromotionDay(LocalDate startDate, LocalDate endDate) {
        LocalDate now = LocalDate.now();

        if (startDate.isAfter(now) && endDate.isBefore(now)) {
            return true;
        }

        return false;
    }

    // 상품 재고 추가하는 기능
    public
}
