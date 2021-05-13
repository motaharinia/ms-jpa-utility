package com.motaharinia.msjpautility.search;


import com.motaharinia.msjpautility.jparepository.GenericSpecification;
import com.motaharinia.msjpautility.search.annotation.SearchDataColumn;
import com.motaharinia.msutility.custom.customdto.search.data.SearchDataDto;
import com.motaharinia.msutility.custom.customdto.search.data.columnconfig.SearchDataColumnConfigDto;
import com.motaharinia.msutility.custom.customdto.search.data.columnconfig.SearchDataColumnConfigSortTypeEnum;
import com.motaharinia.msutility.custom.customdto.search.filter.SearchFilterDto;
import com.motaharinia.msutility.custom.customdto.search.filter.restriction.SearchFilterOperationEnum;
import com.motaharinia.msutility.custom.customdto.search.filter.sort.SearchFilterSortDto;
import com.motaharinia.msutility.custom.customdto.search.filter.sort.SearchFilterSortTypeEnum;
import com.motaharinia.msutility.custom.customexception.utility.UtilityException;
import com.motaharinia.msutility.custom.customexception.utility.UtilityExceptionKeyEnum;
import org.jetbrains.annotations.NotNull;
import org.reflections.ReflectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;


public interface SearchTools {

    /**
     * متد سازنده مدل جستجوی داده که صفحه ای از اینتیرفیس ریپازیتوری دریافت شده از دیتابیس و مدل جستجو و اطلاعات اضافی را از ورودی دریافت میکند و مدل جستجوی داده را طبق آنها برای ارسال به کلاینت می سازد
     *
     * @param viewPage                صفحه ای از اینتیرفیس ریپازیتوری دریافت شده از دیتابیس
     * @param searchFilterDto         مدل جستجو
     * @param searchViewTypeInterface کلاس اینترفیس نوع نمایش خروجی که ستونهای(فیلدهای) خروجی داخل آن تعریف شده است
     * @param userDataMap             اطلاعات اضافی
     * @return خروجی: مدل جستجوی داده برای ارسال به کلاینت
     */
    @NotNull
    static SearchDataDto buildSearchDataDto(@NotNull Page<?> viewPage, @NotNull SearchFilterDto searchFilterDto, @NotNull Class searchViewTypeInterface, Map<String, String> userDataMap) {
        SearchDataDto<Object[]> searchDataDto = new SearchDataDto();
        searchDataDto.setPageNo(searchFilterDto.getPageNo());
        searchDataDto.setTotalRecordSize(viewPage.getTotalElements());
        searchDataDto.setTotalPageSize((long) viewPage.getTotalPages());
        if (userDataMap != null) {
            searchDataDto.setUserDataMap(userDataMap);
        }

        //searchDataColDtoList:
        HashMap<Integer, SearchDataColumn> indexAnnotationHashMap = new HashMap<>();
        List<SearchDataColumnConfigDto> searchDataColumnConfigDtoList = new ArrayList<>();

        Set<Method> getterMethodSet1 = ReflectionUtils.getAllMethods(searchViewTypeInterface, ReflectionUtils.withModifier(Modifier.PUBLIC), ReflectionUtils.withPrefix("get"));
        getterMethodSet1.forEach(getterMethod -> {
            if (!ObjectUtils.isEmpty(getterMethod.getAnnotation(SearchDataColumn.class))) {
                indexAnnotationHashMap.put(getterMethod.getAnnotation(SearchDataColumn.class).index(), getterMethod.getAnnotation(SearchDataColumn.class));
            }
        });
        indexAnnotationHashMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            try {
                SearchDataColumnConfigDto searchDataColumnConfigDto = new SearchDataColumnConfigDto();
                searchDataColumnConfigDto.setAlign(entry.getValue().align());
                searchDataColumnConfigDto.setFormatter(entry.getValue().formatter());
                searchDataColumnConfigDto.setIndex(String.valueOf(entry.getValue().index()));
                searchDataColumnConfigDto.setName(entry.getValue().name());
                searchDataColumnConfigDto.setSearchable(entry.getValue().searchable());
                searchDataColumnConfigDto.setSearchType(entry.getValue().searchType());
                searchDataColumnConfigDto.setSortable(entry.getValue().sortable());
                searchDataColumnConfigDto.setSortType(entry.getValue().sortType());
                searchDataColumnConfigDto.setWidth(entry.getValue().width());
                if (searchDataColumnConfigDto.getSortType().equals(SearchDataColumnConfigSortTypeEnum.TEXT)) {
                    searchDataColumnConfigDto.setSearchFilterOperationList(Arrays.asList(SearchFilterOperationEnum.EQUAL, SearchFilterOperationEnum.MATCH, SearchFilterOperationEnum.MATCH_END, SearchFilterOperationEnum.MATCH_START, SearchFilterOperationEnum.IN, SearchFilterOperationEnum.NOT_IN));
                } else {
                    searchDataColumnConfigDto.setSearchFilterOperationList(Arrays.asList(SearchFilterOperationEnum.EQUAL, SearchFilterOperationEnum.GREATER_THAN, SearchFilterOperationEnum.GREATER_THAN_EQUAL, SearchFilterOperationEnum.LESS_THAN, SearchFilterOperationEnum.LESS_THAN_EQUAL, SearchFilterOperationEnum.IN, SearchFilterOperationEnum.NOT_IN));
                }
                searchDataColumnConfigDtoList.add(searchDataColumnConfigDto);
            } catch (Exception exception) {
                throw new UtilityException(SearchTools.class, UtilityExceptionKeyEnum.SEARCH_TOOLS_EXCEPTION, exception.getMessage());
            }
        });
        searchDataDto.setColumnConfigList(searchDataColumnConfigDtoList);

        //searchDataRowDtoList:
        List<Object[]> searchDataRowDtoList = new ArrayList<>();
        viewPage.stream().forEach(item -> {
            try {
                searchDataRowDtoList.add( recursiveDataRowDtoList(item, item.getClass(), new HashMap<>(), new HashMap<>()).toArray());
            } catch (Exception exception) {
                throw new UtilityException(SearchTools.class, UtilityExceptionKeyEnum.SEARCH_TOOLS_EXCEPTION, exception.getMessage());
            }
        });
        searchDataDto.setPageRowList(searchDataRowDtoList);
        return searchDataDto;
    }


    /**
     * متد بازگشتی که شیی اینترفیس ریپازیتوری و هش مپ اندیس-متد و هش مپ اندیس-شیی آن اینترفیس را ورودی میگیرد و در نهایت لیستی از آرایه مقادیر ستونهای دیتای جستجو را خروجی میدهد
     *
     * @param object             شیی اینترفیس ریپازیتوری
     * @param clazz             کلاس اینترفیس ریپازیتوری
     * @param indexMethodHashMap هش مپ اندیس-متد اینترفیس
     * @param indexObjectHashMap هش مپ اندیس-شیی اینترفیس
     * @return خروجی:  لیستی از آرایه مقادیر ستونهای دیتای جستجو
     */
    @NotNull
    static List<Object> recursiveDataRowDtoList(@NotNull Object object,@NotNull Class clazz, @NotNull HashMap<Integer, Method> indexMethodHashMap, @NotNull HashMap<Integer, Object> indexObjectHashMap) {
        Set<Method> getterMethodSet = ReflectionUtils.getAllMethods(clazz, ReflectionUtils.withModifier(Modifier.PUBLIC), ReflectionUtils.withPrefix("get"));
        getterMethodSet.forEach(getterMethod -> {
            try {
                if (!ObjectUtils.isEmpty(getterMethod.getAnnotation(SearchDataColumn.class))) {
                    if (getterMethod.getReturnType().getName().startsWith("java.lang") || getterMethod.getReturnType().getName().startsWith("java.util.Date")) {
                        indexMethodHashMap.put(getterMethod.getAnnotation(SearchDataColumn.class).index(), getterMethod);
                        indexObjectHashMap.put(getterMethod.getAnnotation(SearchDataColumn.class).index(), object);
                    } else {
                        getterMethod.setAccessible(true);
                        Object childObject = getterMethod.invoke(object);
                        recursiveDataRowDtoList(childObject, getterMethod.getReturnType(), indexMethodHashMap, indexObjectHashMap);
                    }
                }
            } catch (Exception exception) {
                throw new UtilityException(SearchTools.class, UtilityExceptionKeyEnum.SEARCH_TOOLS_EXCEPTION, exception.getMessage());
            }
        });
        List<Object> rowCellList = new ArrayList<>();
        indexMethodHashMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            try {
                entry.getValue().setAccessible(true);
                if (ObjectUtils.isEmpty(indexObjectHashMap.get(entry.getKey()))) {
                    rowCellList.add("");
                } else {
                    rowCellList.add(entry.getValue().invoke(indexObjectHashMap.get(entry.getKey())));
                }

            } catch (Exception exception) {
                throw new UtilityException(SearchTools.class, UtilityExceptionKeyEnum.SEARCH_TOOLS_EXCEPTION, exception.getMessage());
            }
        });
        return rowCellList;
    }


//    public SearchDataModel GridDataFixForExcel(@NotNull SearchDataModel gridData, @NotNull List<GridColModel> pageGridColModelList, @NotNull MessageSource messageSource, @NotNull  HashMap<String, List<Object[]>> formatters) throws Exception {
//        if (ObjectUtils.isEmpty(gridData) || ObjectUtils.isEmpty(pageGridColModelList)) {
//            throw new UtilityException(getClass(), UtilityExceptionKeyEnum.METHOD_PARAMETER_IS_NULL_OR_EMPTY, "");
//        }
//        List<Object[]> objectArrayList = gridData.getRows();
//        Object[] objectArray;
//        String colModelName = "";
//        if (objectArrayList != null) {
//            for (int i = 0; i < objectArrayList.size(); i++) {
//                objectArray = objectArrayList.get(i);
//                for (int j = 0; j < pageGridColModelList.size(); j++) {
//                    colModelName = pageGridColModelList.get(j).getName();
//                    if (formatters != null && formatters.containsKey(colModelName)) {
//                        List<Object[]> formatterArrayList = formatters.get(colModelName);
//                        objectArray[j] = fixFormatter(formatterArrayList, objectArray[j]);
//                    } else {
//                        if (".langKey".equals(colModelName.substring(Math.max(colModelName.length() - 8, 0)))) {
//                            objectArray[j] = fixLangKey((String) objectArray[j], messageSource);
//                        } else {
//                            switch (pageGridColModelList.get(j).getSortType()) {
//                                case "date":
//                                    objectArray[j] = CalDateTime.fixToLocaleDate((Date) objectArray[j], "/", LocaleContextHolder.getLocale());
//                                    break;
//                                case "dateTime":
//                                    objectArray[j] = CalDateTime.fixToLocaleDateTime((Date) objectArray[j], "/", LocaleContextHolder.getLocale());
//                                    break;
//                            }
//                        }
//                    }
//                    objectArrayList.set(i, objectArray);
//                }
//            }
//        }
//        return gridData;
//    }
//
//    //لیستی از فرمترهای گرید و یک داده را دریافت میکند و اگر آن داده ورودی با یکی از داده های لیست فرمترها بخواند مقدار فرمت شده فرمتر را بجای داده خروجی میدهد
//    @NotNull
//    public String fixFormatter(@NotNull List<Object[]> formatterArrayList, @NotNull Object object) throws UtilityException {
//        if (ObjectUtils.isEmpty(formatterArrayList)) {
//            throw new UtilityException(getClass(), UtilityExceptionKeyEnum.METHOD_PARAMETER_IS_NULL_OR_EMPTY, "formatterArrayList");
//        }
//        if (ObjectUtils.isEmpty(object)) {
//            throw new UtilityException(getClass(), UtilityExceptionKeyEnum.METHOD_PARAMETER_IS_NULL_OR_EMPTY, "object");
//        }
//        for (int i = 0; i < formatterArrayList.size(); i++) {
//            if (formatterArrayList.get(i)[0].toString().equals(object + "")) {
//                return formatterArrayList.get(i)[1].toString();
//            }
//        }
//        return "";
//    }
//
//    //مطابق با زبان سیستم در صورت نیاز کلید لاتین را به متن فارسی معادل آن ترجمه میکند و خروجی میدهد
//    public String fixLangKey(String langKey, MessageSource messageSource) {
//        if (!ObjectUtils.isEmpty(langKey)) {
//            return messageSource.getMessage(langKey, new Object[]{}, LocaleContextHolder.getLocale());
//        } else {
//            return "";
//        }
//    }


    /**
     * متدی که مشخصات جستجو  ریپازیتوری مورد نظر را از ورودی دریافت میکند و تمام موارد لیست شرطهای جستجو را به آن اضافه میکند
     * تا در مرحله بعد این مشخصات جستجو به سمت ریپازیتوری برای جستجو در دیتابیس ارسال گردد
     *
     * @param searchFilterDto      مدل فیلتر داده
     * @param genericSpecification مشخصات جستجو ریپازیتوری مورد نظر
     * @return خروجی: مشخصات جستجوی ریپازیتوری حاوی شرایط جستجو
     */
    static GenericSpecification makeSpecificationFromSearchFilter(SearchFilterDto searchFilterDto, GenericSpecification genericSpecification) {
        if (!ObjectUtils.isEmpty(searchFilterDto.getRestrictionList())) {
            searchFilterDto.getRestrictionList().stream().forEach(item -> genericSpecification.add(item));
        }
        return genericSpecification;
    }

    /**
     * این متد شیی صفحه بندی-مرتب سازی را مطابق اطلاعات دریافتی مدل جستجو تولید میکند
     * تا در مرحله بعد این شیی به سمت ریپازیتوری برای جستجو در دیتابیس ارسال گردد
     *
     * @param searchFilterDto مدل فیلتر داده
     * @return خروجی: شیی صفحه بندی-مرتب سازی جهت استفاده در ریپازیتوری
     */
    static Pageable makePageableFromSearchFilter(SearchFilterDto searchFilterDto) {
        Sort allSort = null;
        for (SearchFilterSortDto searchFilterSortDto : searchFilterDto.getSortList()) {
            Sort newSort = null;
            if (searchFilterSortDto.getType().equals(SearchFilterSortTypeEnum.ASC)) {
                newSort = Sort.by(searchFilterSortDto.getFieldName()).ascending();
            }
            if (searchFilterSortDto.getType().equals(SearchFilterSortTypeEnum.DSC)) {
                newSort = Sort.by(searchFilterSortDto.getFieldName()).descending();
            }
            if (ObjectUtils.isEmpty(allSort)) {
                allSort = newSort;
            } else {
                allSort = allSort.and(newSort);
            }
        }
        if (ObjectUtils.isEmpty(allSort)) {
            return PageRequest.of(searchFilterDto.getPageNo().intValue(), searchFilterDto.getPageRowSize().intValue());
        } else {
            return PageRequest.of(searchFilterDto.getPageNo().intValue(), searchFilterDto.getPageRowSize().intValue(), allSort);
        }
    }
}
