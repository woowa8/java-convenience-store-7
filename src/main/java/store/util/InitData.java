package store.util;

import store.domain.Product;
import store.domain.Promotions;
import store.repository.ProductRepository;
import store.repository.PromotionsRepository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// TODO : 파일 받아오는 것은 AI의 힘을 빌림. 알아두기
public class InitData {

    private final PromotionsRepository promotionsRepository;
    private final ProductRepository productRepository;

    public InitData(PromotionsRepository promotionsRepository, ProductRepository productRepository) {
        this.promotionsRepository = promotionsRepository;
        this.productRepository = productRepository;
    }

    // 외부에서는 이것만 사용해서 데이터 초기화
    public void init() {
        try {
            savePromotions();
            saveProducts();
        } catch (IOException e) {
            throw new IllegalStateException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    private List<List<String>> readFile(String fileName) throws IOException {
        List<List<String>> mainList = new ArrayList<List<String>>();
        // 1. 파일 읽어들이기
        InputStream inputStream = InitData.class.getClassLoader().getResourceAsStream(fileName);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        reader.readLine();    // 첫 줄 스킵

        String line;
        while ((line = reader.readLine()) != null) {
            String[] lists = line.split(",");
            List<String> subList = Arrays.asList(lists);
            mainList.add(subList);
        }
        return mainList;
    }

    // 4. 각각 repository에 저장하기
    private void savePromotions() throws IOException {
        List<List<String>> mainList = readFile("promotions.md");
        // name,buy,get,start_date,end_date 순서
        for (List<String> list : mainList) {
            Promotions promotions = Promotions.of(
                    list.get(0),
                    list.get(1),
                    list.get(2),
                    list.get(3),
                    list.get(4)
            );
            promotionsRepository.addPromotion(promotions);
        }
    }

    private void saveProducts() throws IOException {
        List<List<String>> mainList = readFile("products.md");
        // name,price,quantity,promotion 순서
        for (List<String> list : mainList) {
            Product product = new Product(
                    list.get(0),
                    Integer.parseInt(list.get(1)),
                    Integer.parseInt(list.get(2)),
                    promotionsRepository.getPromotionByName(list.get(3))
            );
            productRepository.addProduct(product);
        }
    }
}
