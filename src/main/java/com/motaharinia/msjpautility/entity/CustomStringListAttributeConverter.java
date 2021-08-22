package com.motaharinia.msjpautility.entity;

import org.springframework.util.ObjectUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
* @author https://github.com/motaharinia<br>
 *     این کلاس مبدلی را ایجاد میکند که میتوان با گذاشتن آن بر روی فیلدهای از جنس لیست رشته در انتیتی ها آنها را در فیلد جدول دیتابیس به صورت رشته جدا شده با کاما ذخیره نمود
 */
@Converter
public class CustomStringListAttributeConverter implements AttributeConverter<List<String>, String> {

    @Override
    public String convertToDatabaseColumn(List<String> stringList) {
        if (ObjectUtils.isEmpty(stringList)) {
            return "";
        }
        return stringList.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<String> convertToEntityAttribute(String joined) {
        if (ObjectUtils.isEmpty(joined)) {
            return new ArrayList<>();
        }
        return Arrays.asList(joined.split(","));

    }

}
