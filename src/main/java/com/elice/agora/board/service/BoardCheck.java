package com.elice.agora.board.service;
import java.util.List;

public class BoardCheck {
    
    private static List<String> swearList = List.of(
        "개나소나",
        "저주받은",
        "어리석은",
        "허접한",
        "싫은",
        "바보같은",
        "불쌍한",
        "지나친",
        "예의없는",
        "조롱하는",
        "괴로운",
        "어리석은",
        "성가신",
        "비열한",
        "의심스러운",
        "불쌍한",
        "어이없는",
        "허세부리는",
        "자격없는",
        "지나친"
    );

    public static boolean checkSwearWords(String text) {
        for (String swearWord : swearList) {
            if (text.contains(swearWord)) {
                return false;
            }
        }
        return true;
    }
}
