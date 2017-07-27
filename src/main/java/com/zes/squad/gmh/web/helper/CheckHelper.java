package com.zes.squad.gmh.web.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckHelper {

    public static boolean isValidMobile(String mobile) {
        Pattern pattern = Pattern.compile("[0-9]{11}");
        Matcher matcher = pattern.matcher(mobile);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }
    
}
