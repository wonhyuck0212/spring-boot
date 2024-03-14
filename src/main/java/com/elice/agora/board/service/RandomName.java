package com.elice.agora.board.service;

import java.util.List;

public class RandomName {
    private static List<String> animals = List.of(
        "cat",
        "dog",
        "bird",
        "rabbit",
        "snake"
    );

    public static String getRandomName(Long boardId) {
        int randomIndex = (int) (boardId % animals.size());
        return animals.get(randomIndex);
    }
}
