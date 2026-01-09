package store.repository;

import store.domain.Gift;
import store.domain.Product;

import java.util.ArrayList;
import java.util.List;

public class GiftRepository {
    private final List<Gift> gifts;

    public GiftRepository() {
        gifts = new ArrayList<>();
    }

    public List<Gift> getGifts() {
        return gifts;
    }

    public void addGift(Gift gift) {
        gifts.add(gift);
    }

    public void clear() {
        gifts.clear(); // 또는 orders.clear()
    }
}
