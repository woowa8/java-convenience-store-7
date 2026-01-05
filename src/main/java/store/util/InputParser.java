package store.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputParser {
    public List<List<String>> parse(String input) {
        List<List<String>> inputList = new ArrayList<>();

        String[] parsed = input.split(",");
        for(String str : parsed) {
            if(!str.matches("[가-힣]+-[0-9]+")){
                throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
            }

            String[] orderInfo = str.split("-");
            if(Integer.parseInt(orderInfo[1]) <= 0){
                throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }

            List<String> orderInfoList = Arrays.asList(orderInfo);
            inputList.add(orderInfoList);
        }

        return inputList;
    }
}
