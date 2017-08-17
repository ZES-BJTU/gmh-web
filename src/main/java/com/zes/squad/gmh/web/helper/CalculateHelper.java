package com.zes.squad.gmh.web.helper;

import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Years;

import com.zes.squad.gmh.common.exception.ErrorMessage;

public class CalculateHelper {

    public static int calculateAgeByBirthday(Date birthday) {
        LogicHelper.ensureConditionSatisfied(birthday != null, ErrorMessage.memberBirthdayIsNull);
        Years years = Years.yearsBetween(new DateTime(birthday.getTime()), new DateTime());
        return years.getYears();
    }

}
