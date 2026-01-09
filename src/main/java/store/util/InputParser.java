package store.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class InputParser {
    public List<List<String>> parse(String input) {
        List<List<String>> mainList = new ArrayList<>();

        String[] lines = input.split(",");
        for(String line : lines){
            validForm(line);
            line = line.replace('[',' ').replace(']',' ').trim();
            String[] subList = line.split("-");
            if(Integer.parseInt(subList[1]) < 0){
                throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
            }
            mainList.add(Arrays.asList(subList));
        }
        return mainList;
    }

    private void validForm(String line){
        if(!line.matches("\\[[가-힣]+-[0-9]+\\]")){
            throw new IllegalArgumentException("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
        }
    }
}
