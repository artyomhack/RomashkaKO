package com.artyom.romashkako;

import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Random;

@Component
public class StringUtils {

    private final static Random RANDOM = new Random();

    private final static String ALPHABET_RU = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯ";
    private final static String ALPHABET_EN = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

    public String getRandomString(String prefix, int length) {
        var prefixNotNull = Objects.requireNonNullElse(prefix, "");
        return prefixNotNull + getRandomString(length);
    }

    public String getRandomString(int length) {
        return getRandomString(length, true, true);
    }

    public String getRandomStringEN(int length) {
        return getRandomString(length, true, false);
    }

    public String getRandomStringRU(int length) {
        return getRandomString(length, false, true);
    }

    public String getRandomString(int length, boolean en, boolean ru) {
        var alphabetic = new StringBuilder();

        if (en) alphabetic.append(ALPHABET_EN);
        if (ru) alphabetic.append(ALPHABET_RU);

        var randomBuilder = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = RANDOM.nextInt(alphabetic.length());
            var randomChar = alphabetic.charAt(index);
            randomBuilder.append(randomChar);
        }

        return randomBuilder.toString();
    }


}
