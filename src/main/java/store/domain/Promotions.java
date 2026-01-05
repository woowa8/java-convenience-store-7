package store.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Promotions {
    private final String name;
    private final int buyProduct;
    private final int getProduct;
    private final LocalDate startDate;
    private final LocalDate endDate;

    public Promotions(String name, int buyProduct, int getProduct, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyProduct = buyProduct;
        this.getProduct = getProduct;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // 정적 팩토리 메서드로 매핑
    public static Promotions of(
            String name,
            String buyProduct,
            String getProduct,
            String startDate,
            String endDate
    ) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        return new Promotions(
                name,
                Integer.parseInt(buyProduct),
                Integer.parseInt(getProduct),
                LocalDate.parse(startDate, formatter),
                LocalDate.parse(endDate, formatter)
        );
    }

    public String getName() {
        return name;
    }

    public int getBuyProduct() {
        return buyProduct;
    }

    public int getGetProduct() {
        return getProduct;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
