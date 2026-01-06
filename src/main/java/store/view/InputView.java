package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.util.InputParser;

import java.util.List;
import java.util.function.Supplier;

public class InputView {

    private InputParser inputParser;

    public InputView(InputParser inputParser) {
        this.inputParser = inputParser;
    }

    // 구매할 상품과 수량 입력받는 기능
    public List<List<String>> inputProduct() {
        return retryOnError(() -> {
            System.out.println("구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])");
            String input = Console.readLine();
            return inputParser.parse(input);
        });
    }

    // 프로모션 적용 불가능한 상품 입력 기능
    public boolean askCanNotApplyPromotions(String productName, int quantity) {
        return retryOnError(() -> {
            System.out.println("현재 " + productName + " " + quantity + "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)");
            String input = Console.readLine();

            if(!"Y".equalsIgnoreCase(input) && !"N".equalsIgnoreCase(input)) {
                throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }

            if("Y".equalsIgnoreCase(input)) {
                return true;
            }

            return false;
        });
    }

    // 프로모션 더 받을 수 있는 상품 입력 기능
    public boolean askApplyPromotions(String productName, int quantity) {
        return retryOnError(() -> {
            System.out.println("현재 " + productName + "은(는)" + quantity + "개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)");
            String input = Console.readLine();

            if(!"Y".equalsIgnoreCase(input) && !"N".equalsIgnoreCase(input)) {
                throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }

            if("Y".equalsIgnoreCase(input)) {
                return true;
            }

            return false;
        });
    }

    // 멤버십 할인 적용 입력받는 기능
    public boolean askApplyMembership() {
        return retryOnError(() -> {
            System.out.println("멤버십 할인을 받으시겠습니까? (Y/N)");
            String input = Console.readLine();

            if(!"Y".equalsIgnoreCase(input) && !"N".equalsIgnoreCase(input)) {
                throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }

            if("Y".equalsIgnoreCase(input)) {
                return true;
            }

            return false;
        });
    }

    // 다시 할껀지 물어보는 기능
    public boolean askReplay() {
        return retryOnError(() -> {
            System.out.println("감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)");
            String input = Console.readLine();

            if(!"Y".equalsIgnoreCase(input) && !"N".equalsIgnoreCase(input)) {
                throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }

            if("Y".equalsIgnoreCase(input)) {
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
