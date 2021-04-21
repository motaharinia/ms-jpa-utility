package com.motaharinia.msjpautility.search.filter;

import com.motaharinia.msjpautility.jparepository.GenericSpecification;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
* @author https://github.com/motaharinia<br>
 * این کلاس برای جستجوی پیشرفته داده ها از کلاینت به سرور استفاده میگردد
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchFilterModel implements Serializable {
    /**
     * لیستی از شرطهای جستجو
     */
    private List<SearchFilterRestrictionModel> restrictionList = new ArrayList<>();
    /**
     * لیستی از ترتیبهای صعودی/نزولی دلخواه
     */
    private List<SearchFilterSortModel> sortList = new ArrayList<>();
    /**
     * صفحه مورد نظر برای دریافت در حالت صفحه بندی و اسمارت رندرینگ
     */
    private Integer page=0;
    /**
     * تعداد سطر مورد نیاز در هر صفحه
     */
    private Integer rows=Integer.MAX_VALUE;


    /**
     * متدی که مشخصات جستجو  ریپازیتوری مورد نظر را از ورودی دریافت میکند و تمام موارد لیست شرطهای جستجو را به آن اضافه میکند
     * تا در مرحله بعد این مشخصات جستجو به سمت ریپازیتوری برای جستجو در دیتابیس ارسال گردد
     *
     * @param genericSpecification مشخصات جستجو ریپازیتوری مورد نظر
     * @return خروجی: مشخصات جستجوی ریپازیتوری حاوی شرایط جستجو
     */
    public GenericSpecification makeSpecification(GenericSpecification genericSpecification) {
        if (!ObjectUtils.isEmpty(this.restrictionList)) {
            restrictionList.stream().forEach(item -> genericSpecification.add(item));
        }
        return genericSpecification;
    }

    /**
     * این متد شیی صفحه بندی-مرتب سازی را مطابق اطلاعات دریافتی مدل جستجو تولید میکند
     * تا در مرحله بعد این شیی به سمت ریپازیتوری برای جستجو در دیتابیس ارسال گردد
     *
     * @return خروجی: شیی صفحه بندی-مرتب سازی جهت استفاده در ریپازیتوری
     */
    public Pageable makePageable() {
        Sort allSort = null;
        for (SearchFilterSortModel searchFilterSortModel : this.getSortList()) {
            Sort newSort = null;
            if (searchFilterSortModel.getType().equals(SearchFilterSortTypeEnum.ASC)) {
                newSort = Sort.by(searchFilterSortModel.getFieldName()).ascending();
            }
            if (searchFilterSortModel.getType().equals(SearchFilterSortTypeEnum.DSC)) {
                newSort = Sort.by(searchFilterSortModel.getFieldName()).descending();
            }
            if (ObjectUtils.isEmpty(allSort)) {
                allSort = newSort;
            } else {
                allSort = allSort.and(newSort);
            }
        }
        if (ObjectUtils.isEmpty(allSort)) {
            return PageRequest.of(this.getPage(), this.getRows());
        } else {
            return PageRequest.of(this.getPage(), this.getRows(), allSort);
        }
    }
}
