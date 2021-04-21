package com.motaharinia.msjpautility.search.filter;

/**
* @author https://github.com/motaharinia<br>
 *     مقادیر ثابت نوع جستجو<br>
 * مقادیر ثابت نوع جستجوی ماژولهای دیگر از این کلاس اکستند میشوند
 */
public enum SearchFilterParameterModeEnum {
    /**
     *نوع جستجوی عمومی<br>
     */
    GENERAL("GENERAL"),
    ;

    private final String value;

     SearchFilterParameterModeEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
