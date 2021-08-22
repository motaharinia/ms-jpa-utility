package com.motaharinia.msjpautility.entity;

import org.apache.commons.lang3.StringUtils;
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
 *     این کلاس مبدلی را ایجاد میکند که میتوان با گذاشتن آن بر روی فیلدهای از جنس لیست عدد Long در انتیتی ها آنها را در فیلد جدول دیتابیس به صورت رشته جدا شده با کاما ذخیره نمود
 */
@Converter
public class CustomLongListAttributeConverter implements AttributeConverter<List<Long>, String> {

    @Override
    public String convertToDatabaseColumn(List<Long> longList) {
        if(ObjectUtils.isEmpty(longList)){
            return "";
        }
        return longList.stream()
                .filter(Objects::nonNull)
                .map(String::valueOf)
                .collect(Collectors.joining(","));
    }

    @Override
    public List<Long> convertToEntityAttribute(String joined) {
        if (ObjectUtils.isEmpty(joined)) {
            return new ArrayList<>();
        }
        List<String> listString = Arrays.asList(joined.split(","));
        return listString.stream().filter(StringUtils::isNotEmpty)
                .map(Long::valueOf)
                .collect(Collectors.toList());
    }

}
