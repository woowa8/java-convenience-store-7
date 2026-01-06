package store.controller;

import store.domain.Gift;
import store.domain.Order;
import store.domain.Product;
import store.dto.CheckOrderDto;
import store.dto.ReceiptResponseDto;
import store.repository.GiftRepository;
import store.repository.OrderRepository;
import store.repository.ProductRepository;
import store.repository.PromotionRepository;
import store.service.OrderService;
import store.util.InitData;
import store.util.InputParser;
import store.view.InputView;
import store.view.OutputView;

import java.util.List;

public class OrderController {
    private InputView inputView;
    private OutputView outputView;
    private OrderService orderService;
    private InitData initData;

    public OrderController() {
        this.inputView = new InputView(new InputParser());
        this.outputView = new OutputView();

        GiftRepository giftRepository = new GiftRepository();
        ProductRepository productRepository = new ProductRepository();
        PromotionRepository promotionRepository = new PromotionRepository();
        OrderRepository orderRepository = new OrderRepository();

        this.initData = new InitData(promotionRepository, productRepository);
        this.orderService = new OrderService(giftRepository, productRepository, orderRepository);
    }

    public void run() {
        initData.init(); // 데이터 넣고 시작

        while (true) {
            // output 인트로
            outputView.intro();

            // menu 내보내기
            List<Product> products = orderService.getProducts();
            outputView.printMenu(products);

            while (!processOrder()) {
                orderService.clear();
            }

            // 멤버십 적용할껀지 물어보기
            boolean isMembership = inputView.askApplyMembership();

            //전달할 내용 조립하기
            List<Order> orders = orderService.getOrders();
            List<Gift> gifts = orderService.getGifts();
            int totalCount = orderService.getTotalCount(orders);
            int totalAmount = orderService.getTotalAmount(orders);
            int saleAmount = orderService.getTotalSaleAmount(gifts);

            int membership = 0;

            if (isMembership) {
                membership = ((int) (totalAmount * 0.3) / 1000) * 1000;
            }

            int finalAmount = totalAmount - membership - saleAmount;

            ReceiptResponseDto receiptResponseDto = new ReceiptResponseDto(
                    orders,
                    gifts,
                    totalCount,
                    totalAmount,
                    saleAmount,
                    membership,
                    finalAmount
            );

            // 영수증 출력하기
            outputView.printReceipt(receiptResponseDto);

            // 다시 할 껀지 물어보기
            boolean isReplay = inputView.askReplay();
            if (!isReplay) {
                break;
            }

            orderService.clear();
        }
    }

    public boolean processOrder(){
        // input으로 메뉴 입력 받기
        List<List<String>> inputProduct = inputView.inputProduct();

        for (List<String> input : inputProduct) {
            try {
                CheckOrderDto dto = orderService.check(input);

                // 1. dto에 addGift 있으면 다시 input 부르기
                if (dto.addGift() > 0) {
                    boolean isGift = inputView.askApplyPromotions(dto.productName(), dto.addGift());
                    // 더 받는거면 promotion에 하나 더 추가
                    if (isGift) {
                        dto = CheckOrderDto.of(
                                dto.promotionTotal() + dto.addGift(),
                                dto.standardTotal(),
                                dto.productName(),
                                dto.addGift(),
                                dto.promotionName(),
                                dto.totalGet() + dto.addGift()
                        );
                    }
                }

                // 2. dto에 dto.promotionTotal() 있는데 standardTotal()도 있는 경우
                if (dto.promotionTotal() > 0 && dto.standardTotal() > 0) {
                    boolean isGift = inputView.askCanNotApplyPromotions(dto.productName(), dto.standardTotal());

                    // 프로모션 적용 안되는거 뺄꺼면 standard를 0으로 만든다.
                    if (!isGift) {
                        dto = CheckOrderDto.of(
                                dto.promotionTotal(),
                                0,
                                dto.productName(),
                                dto.addGift(),
                                dto.promotionName(),
                                dto.totalGet()
                        );
                    }
                }

                // save 로직 부르기
                orderService.saveOrder(dto);

            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                return false;
            }
        }
        return true;
    }
}
