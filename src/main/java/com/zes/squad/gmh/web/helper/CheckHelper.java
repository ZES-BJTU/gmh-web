package com.zes.squad.gmh.web.helper;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckHelper {

    public static boolean isValidPageNum(Integer pageNum) {
        if (pageNum == null || pageNum < 0) {
            return false;
        }
        return true;
    }

    public static boolean isValidPageSize(Integer pageSize) {
        if (pageSize == null || pageSize < 0) {
            return false;
        }
        return true;
    }

    public static boolean isValidMobile(String mobile) {
        Pattern pattern = Pattern.compile("[0-9]{11}");
        Matcher matcher = pattern.matcher(mobile);
        if (!matcher.matches()) {
            return false;
        }
        return true;
    }

}
