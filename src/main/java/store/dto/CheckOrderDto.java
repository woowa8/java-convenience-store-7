package store.dto;

public record CheckOrderDto (
        int promotionTotal,
        int standardTotal,
        String productName,
        int addGift,
        String promotionName,
        int totalGet
) {
    public static CheckOrderDto of(int promotionTotal, int standardTotal, String productName, int addGift, String promotionName, int totalGet) {
        return new CheckOrderDto(
                promotionTotal,
                standardTotal,
                productName,
                addGift,
                promotionName,
                totalGet
        );
    }
}
