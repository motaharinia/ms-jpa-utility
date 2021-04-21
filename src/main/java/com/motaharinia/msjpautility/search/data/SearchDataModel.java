package com.motaharinia.msjpautility.search.data;

import com.motaharinia.msjpautility.search.annotation.SearchDataColumn;
import com.motaharinia.msjpautility.search.filter.SearchFilterModel;
import com.motaharinia.msjpautility.search.filter.SearchFilterOperationEnum;
import com.motaharinia.msutility.custom.customexception.utility.UtilityException;
import com.motaharinia.msutility.custom.customexception.utility.UtilityExceptionKeyEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.reflections.ReflectionUtils;
import org.springframework.data.domain.Page;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;

/**
 * @author https://github.com/motaharinia<br>
 * این کلاس مدل حاوی نتیجه جستجوی اطلاعات میباشد
 */

@Slf4j
@Data
@NoArgsConstructor
public class SearchDataModel implements Serializable {
    /**
     * شماره صفحه فعلی
     */
    private Integer page;
    /**
     * تعداد کل سطرهای قابل نمایش که صفحه بندی شده اند و به صفحات کوچکتر تبدیل شده اند
     */
    private Long records;
    /**
     * لیست سطرهای داده
     */
    private List<SearchDataRowModel> rowModelList = new ArrayList<>();
    /**
     * لیست اطلاعات ستونها
     */
    private List<SearchDataColModel> colModelList = new ArrayList<>();
    /**
     * تعداد صفحات در صفحه بندی اطلاعات
     */
    private Long total;
    /**
     * اطلاعات اضافی
     */
    private Map<String, String> userDataMap = new HashMap<>();


    /**
     * متد سازنده مدل جستجوی داده که صفحه ای از اینتیرفیس ریپازیتوری دریافت شده از دیتابیس و مدل جستجو و اطلاعات اضافی را از ورودی دریافت میکند و مدل جستجوی داده را طبق آنها برای ارسال به کلاینت می سازد
     *
     * @param viewPage                صفحه ای از اینتیرفیس ریپازیتوری دریافت شده از دیتابیس
     * @param searchFilterModel       مدل جستجو
     * @param searchViewTypeInterface کلاس اینترفیس نوع نمایش خروجی که ستونهای(فیلدهای) خروجی داخل آن تعریف شده است
     * @param userDataMap             خروجی: مدل جستجوی داده برای ارسال به کلاینت
     */
    public SearchDataModel(@NotNull Page<?> viewPage, @NotNull SearchFilterModel searchFilterModel, @NotNull Class searchViewTypeInterface, Map<String, String> userDataMap) {
        this.page = searchFilterModel.getPage();
        this.records = viewPage.getTotalElements();
        this.total = (long) viewPage.getTotalPages();
        if (userDataMap != null) {
            this.userDataMap = userDataMap;
        }

        //searchDataColModelList:
        HashMap<Integer, SearchDataColumn> indexAnnotationHashMap = new HashMap<>();
        List<SearchDataColModel> searchDataColModelList = new ArrayList<>();

        Set<Method> getterMethodSet1 = ReflectionUtils.getAllMethods(searchViewTypeInterface, ReflectionUtils.withModifier(Modifier.PUBLIC), ReflectionUtils.withPrefix("get"));
        getterMethodSet1.forEach(getterMethod -> {
            if (!ObjectUtils.isEmpty(getterMethod.getAnnotation(SearchDataColumn.class))) {
                indexAnnotationHashMap.put(getterMethod.getAnnotation(SearchDataColumn.class).index(), getterMethod.getAnnotation(SearchDataColumn.class));
            }
        });
        indexAnnotationHashMap.entrySet().stream().sorted(Map.Entry.comparingByKey()).forEach(entry -> {
            try {
                SearchDataColModel searchDataColModel = new SearchDataColModel();
                searchDataColModel.setAlign(entry.getValue().align());
                searchDataColModel.setFormatter(entry.getValue().formatter());
                searchDataColModel.setIndex(String.valueOf(entry.getValue().index()));
                searchDataColModel.setName(entry.getValue().name());
                searchDataColModel.setSearchable(entry.getValue().searchable());
                searchDataColModel.setSearchType(entry.getValue().searchType());
                searchDataColModel.setSortable(entry.getValue().sortable());
                searchDataColModel.setSortType(entry.getValue().sortType());
                searchDataColModel.setWidth(entry.getValue().width());
                if (searchDataColModel.getSortType().equals(SearchDataColSortTypeEnum.TEXT)) {
                    searchDataColModel.setSearchFilterOperationList(Arrays.asList(SearchFilterOperationEnum.EQUAL, SearchFilterOperationEnum.MATCH, SearchFilterOperationEnum.MATCH_END, SearchFilterOperationEnum.MATCH_START, SearchFilterOperationEnum.IN, SearchFilterOperationEnum.NOT_IN));
                } else {
                    searchDataColModel.setSearchFilterOperationList(Arrays.asList(SearchFilterOperationEnum.EQUAL, SearchFilterOperationEnum.GREATER_THAN, SearchFilterOperationEnum.GREATER_THAN_EQUAL, SearchFilterOperationEnum.LESS_THAN, SearchFilterOperationEnum.LESS_THAN_EQUAL, SearchFilterOperationEnum.IN, SearchFilterOperationEnum.NOT_IN));
                }
                searchDataColModelList.add(searchDataColModel);
            } catch (Exception exception) {
                log.error("UTILITY_EXCEPTION.SearchDataModel.SearchDataModel() exception:",exception);
            }
        });
        this.colModelList = searchDataColModelList;

        //searchDataRowModelList:
        List<SearchDataRowModel> searchDataRowModelList = new ArrayList<>();
        viewPage.stream().forEach(item -> {
            try {
                searchDataRowModelList.add(new SearchDataRowModel((Integer) item.getClass().getDeclaredMethod("getId").invoke(item), recursiveDataRowModelList(item, item.getClass(), new HashMap<>(), new HashMap<>()).toArray()));
            } catch (Exception exception) {
                log.error("UTILITY_EXCEPTION.SearchDataModel.SearchDataModel() exception:",exception);
            }
        });
        this.rowModelList = searchDataRowModelList;
    }


    /**
     * متد بازگشتی که شیی اینترفیس ریپازیتوری و هش مپ اندیس-متد و هش مپ اندیس-شیی آن اینترفیس را ورودی میگیرد و در نهایت لیستی از آرایه مقادیر ستونهای دیتای جستجو را خروجی میدهد
     *
     * @param object             شیی اینترفیس ریپازیتوری
     * @param indexMethodHashMap هش مپ اندیس-متد اینترفیس
     * @param indexObjectHashMap هش مپ اندیس-شیی اینترفیس
     * @return خروجی:  لیستی از آرایه مقادیر ستونهای دیتای جستجو
     */
    @NotNull
    private List<Object> recursiveDataRowModelList(@NotNull Object object, Class clazz, @NotNull HashMap<Integer, Method> indexMethodHashMap, @NotNull HashMap<Integer, Object> indexObjectHashMap) {
        if (ObjectUtils.isEmpty(clazz)) {
            throw new UtilityException(getClass(), UtilityExceptionKeyEnum.METHOD_PARAMETER_IS_NULL_OR_EMPTY, "clazz");
        }


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
                        recursiveDataRowModelList(childObject, getterMethod.getReturnType(), indexMethodHashMap, indexObjectHashMap);
                    }
                }
            } catch (Exception exception) {
                log.error("UTILITY_EXCEPTION.SearchDataModel.recursiveDataRowModelList() exception:",exception);
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
                log.error("UTILITY_EXCEPTION.SearchDataModel.recursiveDataRowModelList() exception:",exception);
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
}
