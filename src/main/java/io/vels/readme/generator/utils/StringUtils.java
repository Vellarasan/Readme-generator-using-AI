package io.vels.readme.generator.utils;

import org.springframework.stereotype.Component;

@Component
public class StringUtils {

    private StringUtils() {
    }

    public static String formatWithArray(String template, String[] values) {
        // Replace {0}, {1}, etc. with corresponding array values
        String result = template;
        for (int i = 0; i < values.length; i++) {
            result = result.replaceFirst("\\{(.*?)}", values[i]);
        }
        return result;
    }
}
