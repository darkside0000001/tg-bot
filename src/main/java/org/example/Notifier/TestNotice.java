package org.example.Notifier;

import java.util.ArrayList;
import java.util.List;

public class TestNotice implements SendNotice{

    private List<String> testList= new ArrayList<>();
    @Override
    public void sendMessage(Long chatId, String text) {
        //System.out.println(testList);
        if (testList.size() == 2) {
            System.exit(0);
        }
        testList.add(text);
    }

    public List<String> getTestList() {
        return testList;
    }
}
