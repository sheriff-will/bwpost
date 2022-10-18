package com.application.iserv.ui.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Commons {

    public static boolean validateNumbers(String number) {
        boolean isValid = false;

        Pattern pattern = Pattern.compile("^[0-9]{1,45}$");
        Matcher matcher = pattern.matcher(number);

        if (matcher.find()) {
            isValid = true;
        }

        return isValid;
    }

}
