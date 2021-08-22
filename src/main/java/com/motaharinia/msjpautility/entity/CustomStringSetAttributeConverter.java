package com.motaharinia.msjpautility.entity;

import org.springframework.util.ObjectUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author https://github.com/motaharinia<br>
 * این کلاس مبدلی را ایجاد میکند که میتوان با گذاشتن آن بر روی فیلدهای از جنس ست رشته در انتیتی ها آنها را در فیلد جدول دیتابیس به صورت رشته جدا شده با کاما ذخیره نمود
 */
@Converter
public class CustomStringSetAttributeConverter implements AttributeConverter<Set<String>, String> {

    @Override
    public String convertToDatabaseColumn(Set<String> stringSet) {
        if (ObjectUtils.isEmpty(stringSet)) {
            return "";
        }
        return stringSet.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<String> convertToEntityAttribute(String joined) {
        if (ObjectUtils.isEmpty(joined)) {
            return new HashSet<>();
        }
        return new HashSet<>(Arrays.asList(joined.split(",")));

    }

}
