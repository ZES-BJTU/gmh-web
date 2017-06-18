package com.zes.squad.gmh.web.common;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class JsonResult<T> {

    private static final int DEFAULT_SUCCESS_CODE = 0;

    private int              code;
    private String           error;
    private T                data;

    public static <T> JsonResult<T> success() {
        return success(null);
    }

    public static <T> JsonResult<T> success(T data) {
        JsonResult<T> result = new JsonResult<>();
        result.code = DEFAULT_SUCCESS_CODE;
        result.data = data;
        return result;
    }

    public static <T> JsonResult<T> fail(int code, String error) {
        JsonResult<T> result = new JsonResult<>();
        result.code = code;
        result.error = error;
        return result;
    }

}
