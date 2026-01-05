package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.domain.Promotions;
import store.util.InputParser;

import java.util.List;
import java.util.function.Supplier;

public class InputView {
    private final InputParser inputParser;

    public InputView(InputParser inputParser) {
        this.inputParser = inputParser;
    }

    // 1. 구매 상품명과 수량을 입력 받는 부분
    public List<List<String>> inputOrders() {
        return retryOnError(() -> {
            System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
            String input = Console.readLine();

            return inputParser.parse(input);
        });
    }

    // 2. 프로모션 받을 수 있는 경우 print
    public boolean inputApplyPromotions(String product, Promotions promotions) {
        return retryOnError(() -> {
            System.out.println("현재 " + product + "은(는) " + promotions.getGetProduct() + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
            String input = Console.readLine();

            if(!input.equals("Y") && !input.equals("N")){
                throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }

            if (input.equals("Y")) {
                return true;
            }

            return false;
        });
    }

    // 3. 프로모션 할인 받을 수 없는 경우 print
    public boolean inputApplyBasic(String product, int amount) {
        return retryOnError(() -> {
            System.out.println("현재 " + product + " " + amount + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
            String input = Console.readLine();

            if(!input.equals("Y") && !input.equals("N")){
                throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }

            if (input.equals("Y")) {
                return true;
            }

            return false;
        });
    }

    // 4. 멤버십 할인 적용 여부 입력 받기
    public boolean inputApplyMembership() {
        return retryOnError(() -> {
            System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
            String input = Console.readLine();

            if(!input.equals("Y") && !input.equals("N")){
                throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }

            if (input.equals("Y")) {
                return true;
            }

            return false;
        });
    }

    // 5. 추가 상품 구매 여부 입력 받기
    public boolean inputApplyAdditionalItems() {
        return retryOnError(() -> {
            System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
            String input = Console.readLine();

            if(!input.equals("Y") && !input.equals("N")){
                throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }

            if (input.equals("Y")) {
                return true;
            }

            return false;
        });
    }

    private <T> T retryOnError(Supplier<T> supplier) {
        while (true) {
            try {
                return supplier.get();
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
