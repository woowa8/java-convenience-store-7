package store.service;

import store.domain.Gift;
import store.domain.Order;
import store.domain.Product;
import store.dto.CheckOrderDto;
import store.repository.GiftRepository;
import store.repository.OrderRepository;
import store.repository.ProductRepository;

import java.time.LocalDate;
import java.util.List;

public class OrderService {
    private final GiftRepository giftRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(GiftRepository giftRepository, ProductRepository productRepository, OrderRepository orderRepository) {
        this.giftRepository = giftRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public int getTotalSaleAmount(List<Gift> gifts){
        int totalAmount = 0;

        for(Gift gift : gifts){
            totalAmount += (gift.getQuantity() * gift.getPrice());
        }

        return totalAmount;
    }

    public int getTotalAmount(List<Order> orders){
        int totalAmount = 0;

        for(Order order : orders){
            Product product = productRepository.findProductNoPromotion(order.getProductName());
            totalAmount += (order.getQuantity() * product.getPrice());
        }

        return totalAmount;
    }

    public int getTotalCount(List<Order> orders){
        int totalCount = 0;

        for(Order order : orders){
            totalCount += order.getQuantity();
        }

        return totalCount;
    }

    public List<Product> getProducts() {
        return productRepository.getProducts();
    }

    public List<Order> getOrders() {
        return orderRepository.getOrders();
    }

    public List<Gift> getGifts() {
        return giftRepository.getGifts();
    }

    public void clear() {
        giftRepository.clear();
        orderRepository.clear();
    }

    // 확인하는 로직
    // TODO : controller에서 try-catch로 재입력 처리
    public CheckOrderDto check(List<String> orderInput){
        String orderProductName = orderInput.get(0);
        int orderAmount = Integer.parseInt(orderInput.get(1));

        // 1. 프로모션 상품인지 확인한다.
        List<Product> products = productRepository.findByProductName(orderProductName);

        if(products.size() == 0){
            // 없는 제품
            throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
        }

        if(products.size() == 1){    // 하나면 프로모션 없음, 일반로직으로
            return standardCalculation(orderInput);
        }

        // 2. 프로모션 날짜 안에 있는지 확인
        Product promotion = null;
        Product standard = null;

        for (Product product : products) {
            if (product.getPromotion() == null){
                standard = product;
            }else{
                promotion = product;
            }
        }

        if(!isPromotionDay(promotion)){    // 프로모션 날짜 아니면 프로모션 미적용
            return standardCalculation(orderInput);
        }

        // 3. 전체 재고 확인하기
        int totalStock = promotion.getQuantity() + standard.getQuantity();

        if(orderAmount > totalStock){
            // 재고 부족
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }

        // 4. 확인용 정보 뽑기
        int buy = promotion.getPromotion().getBuyAmount();    // 구매 단위
        int get = promotion.getPromotion().getGetAmount();    // 증정 단위
        int size = buy + get;
        int promotionSize = Math.min(orderAmount/size, promotion.getQuantity()/size);    // 프로모션 얼만큼 주는지 단위

        int totalGet = promotionSize * get;   // 총 줘야 하는 get

        int addGift = 0;    // 추가로 줘야 하는지 단위

        if(orderAmount - promotion.getQuantity() >= get){     // 전체보다 줄 수 있는게 더 많은데,
            if(orderAmount % size == buy){  // 근데 이제 나머지가 몫 만큼 있어야 한다.
                addGift = get;
            }
        }

        int promotionTotal = promotionSize * size;    // 전체 프로모션 주는 양
        int standardTotal = orderAmount - promotionTotal;    // 전체 일반 상품 주는 양

        return new CheckOrderDto(promotionTotal, standardTotal, orderProductName, addGift, promotion.getPromotion().getName(), totalGet);
    }

    // 일반 계산
    // TODO : controller에서 try-catch로 재입력 처리
    public CheckOrderDto standardCalculation(List<String> orderInput){
        String orderProductName = orderInput.get(0);
        int orderAmount = Integer.parseInt(orderInput.get(1));

        Product standard = productRepository.findProductNoPromotion(orderProductName);
        int quantity = standard.getQuantity();

        if(orderAmount > quantity){
            // 재고 부족
            throw new IllegalArgumentException("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
        }
        // 일반 계산의 경우, 그냥 남은 재고 그대로 내보낸다.
        return new CheckOrderDto(0, orderAmount, orderProductName, 0, null, 0);
    }

    // 저장하는 로직
    public void saveOrder(CheckOrderDto checkOrderDto){
        // order로 전체 저장하기
        int totalAmount = checkOrderDto.promotionTotal() + checkOrderDto.standardTotal();
        String productName = checkOrderDto.productName();

        Product product = productRepository.findProductNoPromotion(productName);
        Order order = new Order(productName, totalAmount, product.getPrice() * totalAmount);

        orderRepository.addOrder(order);    // 전체 통으로 주문 저장

        // promotion만 해서 giftRepo에 저장하기
        String promotionName = checkOrderDto.promotionName();

        if(promotionName != null){   // 프로모션 있음
            Product promotion = productRepository.findProductHavePromotion(productName, promotionName);
            Gift gift = Gift.of(
                    promotion
            );
            gift.addQuantity(checkOrderDto.totalGet());   // 증정 수량 늘리기
            giftRepository.addGift(gift);   // 증정에 더하기

            productRepository.minusStock(promotion, checkOrderDto.promotionTotal());
        }

        // 양쪽 다 재고 까기
        productRepository.minusStock(product, checkOrderDto.standardTotal());
    }

    // 프로모션 날짜 안에 있는지 확인
    public boolean isPromotionDay(Product promotion){
        LocalDate start = promotion.getPromotion().getStartDate();
        LocalDate end = promotion.getPromotion().getEndDate();
        LocalDate ex = LocalDate.of(2024, 5, 5);

        if(!ex.isBefore(start) && !ex.isAfter(end)){
            return true;
        }
        return false;
    }
}
