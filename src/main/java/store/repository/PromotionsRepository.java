package store.repository;

import store.domain.Promotions;

import java.util.ArrayList;
import java.util.List;

public class PromotionsRepository {
    private final List<Promotions> promotions;

    public PromotionsRepository() {
        this.promotions = new ArrayList<>();
    }

    public List<Promotions> getPromotions() {
        return promotions;
    }

    public void addPromotion(Promotions promotion) {
        this.promotions.add(promotion);
    }

    public Promotions getPromotionByName(String name) {
        return promotions.stream()
                .filter(promotion -> promotion.getName().equals(name))
                .findFirst().orElse(null);
    }
}
