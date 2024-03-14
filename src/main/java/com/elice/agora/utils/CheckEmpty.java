package com.elice.agora.utils;

public class CheckEmpty {
    public static void checkNull(String text, String errorMessage) throws Exception {
        if (text == null || text == "") {
            throw new Exception(errorMessage);
        }
    }
    public static void checkNull(Boolean bool, String errorMessage) throws Exception {
        if (bool == null || bool.toString().isBlank()) {
            throw new Exception(errorMessage);
        }
    }
}
