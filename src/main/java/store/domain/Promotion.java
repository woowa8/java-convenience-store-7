package store.domain;

import java.time.LocalDate;

public class Promotion {
    private final String name;
    private final int buyAmount;
    private final int getAmount;
    private final LocalDate startDate;
    private final LocalDate endDate;

    private Promotion(String name, int buyAmount, int getAmount, LocalDate startDate, LocalDate endDate) {
        this.name = name;
        this.buyAmount = buyAmount;
        this.getAmount = getAmount;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public static Promotion of(String name, String buyAmount, String getAmount, String startDate, String endDate) {
        return new Promotion(name, Integer.parseInt(buyAmount), Integer.parseInt(getAmount), LocalDate.parse(startDate), LocalDate.parse(endDate));
    }

    public  String getName() {
        return name;
    }
    public int getBuyAmount() {
        return buyAmount;
    }

    public int getGetAmount() {
        return getAmount;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }
}
