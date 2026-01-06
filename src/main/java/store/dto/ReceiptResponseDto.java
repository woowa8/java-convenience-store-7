package store.dto;

import store.domain.Gift;
import store.domain.Order;
import java.util.List;

public record ReceiptResponseDto (
        List<Order> orders,
        List<Gift> gifts,
        int totalCount,
        int totalAmount,
        int saleAmount,
        int membership,
        int finalAmount
) {
}
