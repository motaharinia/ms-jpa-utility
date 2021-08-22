package com.motaharinia.msjpautility.entity;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ObjectUtils;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author https://github.com/motaharinia<br>
 * این کلاس مبدلی را ایجاد میکند که میتوان با گذاشتن آن بر روی فیلدهای از جنس ست عدد Integer در انتیتی ها آنها را در فیلد جدول دیتابیس به صورت رشته جدا شده با کاما ذخیره نمود
 */
@Converter
public class CustomIntegerSetAttributeConverter implements AttributeConverter<Set<Integer>, String> {

    @Override
    public String convertToDatabaseColumn(Set<Integer> integerSet) {
        if (ObjectUtils.isEmpty(integerSet)) {
            return "";
        }
        return integerSet.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public Set<Integer> convertToEntityAttribute(String joined) {
        if (ObjectUtils.isEmpty(joined)) {
            return new HashSet<>();
        }
        Set<String> stringSet = new HashSet<>(Arrays.asList(joined.split(",")));
        return stringSet.stream().filter(StringUtils::isNotEmpty)
                .map(Integer::valueOf)
                .collect(Collectors.toSet());
    }

}
