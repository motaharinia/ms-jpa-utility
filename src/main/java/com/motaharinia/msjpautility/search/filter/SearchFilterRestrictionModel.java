package com.motaharinia.msjpautility.search.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @author https://github.com/motaharinia<br>
 * این کلاس مدل شرطی است که میخواهیم در جستجو هایی مانند جستجوی گرید از آن استفاده کنیم
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilterRestrictionModel implements Serializable {
    /**
     * نام فیلدی که روی آن شرط گذاشته میشود
     */
    private String fieldName;
    /**
     * عملیات شرط برای فیلد مورد نظر در جستجو
     */
    private SearchFilterOperationEnum fieldOperation;
    /**
     * مقدار شرط برای فیلد مورد نظر در جستجو
     */
    private Object fieldValue;
    /**
     *  مقادیر ثابت and و or بر روی جستجوی پیشرفته
     */
    SearchFilterNextConditionOperatorEnum nextConditionOperator= SearchFilterNextConditionOperatorEnum.AND;
}
