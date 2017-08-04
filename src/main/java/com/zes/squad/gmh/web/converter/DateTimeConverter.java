package com.zes.squad.gmh.web.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

import com.google.common.base.Strings;

public class DateTimeConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        if (Strings.isNullOrEmpty(source)) {
            return null;
        }
        Long dateTime = Long.valueOf(source);
        return new Date(dateTime);
    }

}
