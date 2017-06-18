package com.zes.squad.gmh.web.common;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CheckResult {

    private boolean passed;
    private String  error;

    public static CheckResult success() {
        CheckResult result = new CheckResult();
        result.passed = true;
        return result;
    }

    public static CheckResult fail(String error) {
        CheckResult result = new CheckResult();
        result.passed = false;
        result.error = error;
        return result;
    }

}
