package store.repository;

import store.domain.Promotion;

import java.util.ArrayList;
import java.util.List;

public class PromotionRepository {
    private final List<Promotion> promotions;

    public PromotionRepository() {
        this.promotions = new ArrayList<>();
    }

    public void addPromotion(Promotion promotion) {
        this.promotions.add(promotion);
    }

    public Promotion findPromotionByName(String name) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equals(name))
                .findFirst().orElse(null);
    }
}
