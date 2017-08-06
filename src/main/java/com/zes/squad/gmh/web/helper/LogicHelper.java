package com.zes.squad.gmh.web.helper;

import java.util.Collection;
import java.util.Map;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.MapUtils;

import com.google.common.base.Strings;
import com.zes.squad.gmh.common.exception.ErrorCodeEnum;
import com.zes.squad.gmh.common.exception.GmhException;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class LogicHelper {

    public static void ensureParameterExist(Object parameter, String errorMessage) {
        if (parameter == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS, errorMessage);
        }
        if (String.valueOf(parameter).isEmpty()) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS, errorMessage);
        }
    }

    public static void ensureParameterValid(boolean checkResult, String errorMessage) {
        if (!checkResult) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS, errorMessage);
        }
    }

    public static void ensureValueExist(String value, String errorMessage) {
        if (Strings.isNullOrEmpty(value)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, errorMessage);
        }
    }

    public static void ensureEntityExist(Object entity, String errorMessage) {
        if (entity == null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, errorMessage);
        }
    }

    public static void ensureEntityNotExist(Object entity, String errorMessage) {
        if (entity != null) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_ENTITY_NOT_FOUND, errorMessage);
        }
    }

    public static void ensureArrayExist(Object[] objs, String errorMessage) {
        if (objs == null || objs.length == 0) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_INVALID_PARAMETERS, errorMessage);
        }
    }

    public static <T> void ensureCollectionNotEmpty(Collection<T> collection, String errorMessage) {
        if (CollectionUtils.isEmpty(collection)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_COLLECTION_IS_EMPTY, errorMessage);
        }
    }

    public static <K, V> void ensureMapNotEmpty(Map<K, V> map, String errorMessage) {
        if (MapUtils.isEmpty(map)) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_COLLECTION_IS_EMPTY, errorMessage);
        }
    }

    public static void ensureConditionSatisfied(boolean condition, String errorMessage) {
        if (!condition) {
            throw new GmhException(ErrorCodeEnum.BUSINESS_EXCEPTION_CONDITION_NOT_SUPPORTTED, errorMessage);
        }
    }

}
