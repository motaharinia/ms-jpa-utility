package com.motaharinia.msjpautility.search.filter;

/**
* @author https://github.com/motaharinia<br>
 * مقادیر ثابت and و or بر روی جستجوی پیشرفته
 */
public enum SearchFilterNextConditionOperatorEnum {
    /**
     * مقدار ورودی دلخواه عضوی از گزینه های فیلد انتیتی از نوع لیست باشد<br>
     * SELECT a FROM EntityA a WHERE :value MEMBER OF a.fieldCollection
     */
    AND("AND"),
    /**
     * مقدار ورودی دلخواه عضوی از گزینه های فیلد انتیتی از نوع لیست نباشد<br>
     * SELECT a FROM EntityA a WHERE :value NOT MEMBER OF a.fieldCollection
     */
    OR("OR"),
    ;

    private final String value;

    SearchFilterNextConditionOperatorEnum(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
