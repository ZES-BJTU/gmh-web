package com.zes.squad.gmh.web.converter;

import java.util.Date;

import org.springframework.core.convert.converter.Converter;

public class DateTimeConverter implements Converter<String, Date> {

    @Override
    public Date convert(String source) {
        Long dateTime = Long.valueOf(source);
        return new Date(dateTime);
    }

}
