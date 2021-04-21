package com.motaharinia.msjpautility.search.data;

import com.motaharinia.msjpautility.search.filter.SearchFilterOperationEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
* @author https://github.com/motaharinia<br>
 * از این کلاس مدل برای تنظیم کردن مشخصات نمایشی ستونهای خروجی داده استفاده میشود
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDataColModel implements Serializable {
    /**
     * نام فیلد ستون
     */
    private String name;
    /**
     * اندیس و ترتیب قرارگیری ستون
     */
    private String index;
    /**
     * جهت نمایش افقی
     */
    private SearchDataColAlignEnum align = SearchDataColAlignEnum.CENTER;
    /**
     * عرض ستون
     */
    private Integer width;
    /**
     * نوع مرتب سازی ستون که عددی یا متنی است
     */
    private SearchDataColSortTypeEnum sortType;
    /**
     * نوع جستجوی ستون که متنی یا انتخابی است
     */
    private SearchDataColSearchTypeEnum searchType;
    /**
     * لیستی از آپشنهای جستجوی پیشرفته قابل استفاده در این ستون که بیشتر در جستجوی پیشرفته استفاده میشود
     */
    private List<SearchFilterOperationEnum> searchFilterOperationList;
    /**
     * رشته فرمت کننده ستون
     * مثلا میخواهیم برای مقادیر صفر در ستون کلمه خیر بیاریم و برای مقادیر یک در ستون کلمه بلی بیاریم
     */
    private String formatter;
    /**
     * جستجوی پیشرفته دارد یا خیر
     */
    private Boolean searchable;
    /**
     * قابل مرتب سازی داده هست یا خیر
     */
    private Boolean sortable;

}
