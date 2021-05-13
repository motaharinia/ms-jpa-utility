package com.motaharinia.msjpautility.search.annotation;





import com.motaharinia.msutility.custom.customdto.search.data.columnconfig.SearchDataColumnConfigAlignEnum;
import com.motaharinia.msutility.custom.customdto.search.data.columnconfig.SearchDataColumnConfigSearchTypeEnum;
import com.motaharinia.msutility.custom.customdto.search.data.columnconfig.SearchDataColumnConfigSortTypeEnum;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
* @author https://github.com/motaharinia<br>
 * از این انوتیشن برای تنظیم کردن مشخصات نمایشی ستونهای خروجی داده استفاده میشود<br>
 * فیلد ایندکس و نام ضروری هستند
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface SearchDataColumn {
    /**
     * نام فیلد ستون
     * @return خروجی:
     */
    String name();

    /**
     * اندیس و ترتیب قرارگیری ستون
     * @return خروجی:
     */
    int index();

    /**
     * جهت نمایش افقی
     * @return خروجی:
     */
    SearchDataColumnConfigAlignEnum align() default SearchDataColumnConfigAlignEnum.CENTER;

    /**
     * عرض ستون
     * @return خروجی:
     */
    int width() default 100;

    /**
     * نوع مرتب سازی ستون که عددی یا متنی است
     * @return خروجی:
     */
    SearchDataColumnConfigSortTypeEnum sortType() default SearchDataColumnConfigSortTypeEnum.TEXT;

    /**
     * نوع جستجوی ستون که متنی یا انتخابی است
     * @return خروجی:
     */
    SearchDataColumnConfigSearchTypeEnum searchType() default SearchDataColumnConfigSearchTypeEnum.TEXT;

    /**
     * رشته فرمت کننده ستون<br>
     * مثلا میخواهیم برای مقادیر صفر در ستون کلمه خیر بیاریم و برای مقادیر یک در ستون کلمه بلی بیاریم
     * @return خروجی:
     */
    String formatter() default "";

    /**
     * جستجوی پیشرفته دارد یا خیر
     * @return خروجی:
     */
    boolean searchable() default true;

    /**
     * قابل مرتب سازی داده هست یا خیر
     *  @return خروجی:
     */
    boolean sortable() default true;
}
