package com.motaharinia.msjpautility.jparepository;


import com.motaharinia.msutility.custom.customdto.search.filter.restriction.SearchFilterNextConditionOperatorEnum;
import com.motaharinia.msutility.custom.customdto.search.filter.restriction.SearchFilterRestrictionDto;
import com.motaharinia.msutility.custom.customfield.CustomDate;
import com.motaharinia.msutility.custom.customfield.CustomDateTime;
import com.motaharinia.msutility.custom.customjson.CustomObjectMapper;
import com.motaharinia.msutility.tools.calendar.CalendarTools;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

/**
* @author https://github.com/motaharinia<br>
 * این کلاس جنریک بیلدر لازم برای تمام کلاسهای مربوط به انجام کوئری های پیشرفته دیتابیس را دارا میباشد
 */
@Slf4j
public class GenericSpecification<T> implements Specification<T> {

    private final CustomObjectMapper customObjectMapper = new CustomObjectMapper();

    private final List<SearchFilterRestrictionDto> searchFilterRestrictionList;

    public GenericSpecification() {
        this.searchFilterRestrictionList = new ArrayList<>();
    }

    public void add(SearchFilterRestrictionDto searchFilterRestrictionDto) {
        searchFilterRestrictionList.add(searchFilterRestrictionDto);
    }

    @Override
    public Predicate toPredicate(@NotNull Root<T> root, @NotNull CriteriaQuery<?> query, @NotNull CriteriaBuilder builder) {

        Date date;
        String string;
        LinkedHashMap<String,String> linkedHashMap;

        Predicate newPredicate;
        Predicate predicate = null;

        //اضافه کردن شرطهای مدل فیلتر
        for (SearchFilterRestrictionDto searchFilterRestrictionDto : searchFilterRestrictionList) {
            //شرط جدید
            newPredicate = null;
            try {
                //مسیر انتیتی که روی آن شرط خواهیم زد
                Path path = this.getPath(root, searchFilterRestrictionDto.getFieldName());
                //مقداری که برای شرط در مدل فیلتر ست شده است
                Object fieldValue = searchFilterRestrictionDto.getFieldValue();

                //سوییچ روی نوع شرط مدل فیلتر
                switch (searchFilterRestrictionDto.getFieldOperation()) {

                    case GREATER_THAN:
                        // بزرگتر از عدد یا تاریخ
                        if (Integer.class.equals(fieldValue.getClass())) {
                            newPredicate = builder.greaterThan(path, (Integer) fieldValue);
                        } else {
                            linkedHashMap = (LinkedHashMap) fieldValue;
                            if (linkedHashMap.containsKey("hour")) {
                                date = CalendarTools.getDateFromCustomDateTime(this.customObjectMapper.convertValue(linkedHashMap, CustomDateTime.class));
                            } else {
                                date = CalendarTools.getDateFromCustomDate(this.customObjectMapper.convertValue(linkedHashMap, CustomDate.class));
                            }
                            newPredicate = builder.greaterThan(path, date);

                        }
                        break;

                    case LESS_THAN:
                        // کوچکتر از عدد یا تاریخ
                        if (Integer.class.equals(fieldValue.getClass())) {
                            newPredicate = builder.lessThan(path, (Integer) fieldValue);
                        } else {
                            linkedHashMap = (LinkedHashMap) fieldValue;
                            if (linkedHashMap.containsKey("hour")) {
                                date = CalendarTools.getDateFromCustomDateTime(this.customObjectMapper.convertValue(linkedHashMap, CustomDateTime.class));
                            } else {
                                date = CalendarTools.getDateFromCustomDate(this.customObjectMapper.convertValue(linkedHashMap, CustomDate.class));
                            }
                            newPredicate = builder.lessThan(path, date);
                        }
                        break;

                    case GREATER_THAN_EQUAL:
                        // بزرگتر مساوی از عدد یا تاریخ
                        if (Integer.class.equals(fieldValue.getClass())) {
                            newPredicate = builder.greaterThanOrEqualTo(path, (Integer) fieldValue);
                        } else {
                            linkedHashMap = (LinkedHashMap) fieldValue;
                            if (linkedHashMap.containsKey("hour")) {
                                date = CalendarTools.getDateFromCustomDateTime(this.customObjectMapper.convertValue(linkedHashMap, CustomDateTime.class));
                            } else {
                                date = CalendarTools.getDateFromCustomDate(this.customObjectMapper.convertValue(linkedHashMap, CustomDate.class));
                            }
                            newPredicate = builder.greaterThanOrEqualTo(path, date);
                        }
                        break;

                    case LESS_THAN_EQUAL:
                        // کوچکتر مساوی از عدد یا تاریخ
                        if (Integer.class.equals(fieldValue.getClass())) {
                            newPredicate = builder.lessThanOrEqualTo(path, (Integer) fieldValue);
                        } else {
                            linkedHashMap = (LinkedHashMap) fieldValue;
                            if (linkedHashMap.containsKey("hour")) {
                                date = CalendarTools.getDateFromCustomDateTime(this.customObjectMapper.convertValue(linkedHashMap, CustomDateTime.class));
                            } else {
                                date = CalendarTools.getDateFromCustomDate(this.customObjectMapper.convertValue(linkedHashMap, CustomDate.class));
                            }
                            newPredicate = builder.lessThanOrEqualTo(path, date);
                        }
                        break;

                    case EQUAL:
                        // برابر باشد با
                        newPredicate = builder.equal(path, fieldValue);
                        break;

                    case NOT_EQUAL:
                        // برابر نباشد با
                        newPredicate = builder.notEqual(path, fieldValue);
                        break;

                    case MATCH:
                        // تطبیق رشته ای داشته باشد با
                        if (String.class.equals(fieldValue.getClass())) {
                            string = (String) fieldValue;
                            newPredicate = builder.like(builder.lower(path), "%" + string.toLowerCase() + "%");
                        }
                        break;

                    case MATCH_START:
                        // با ابتدای عبارت تطبیق رشته ای داشته باشد
                        if (String.class.equals(fieldValue.getClass())) {
                            string = (String) fieldValue;
                            newPredicate = builder.like(builder.lower(path), string.toLowerCase() + "%");
                        }
                        break;

                    case MATCH_END:
                        // با انتهای عبارت تطبیق رشته ای داشته باشد
                        if (String.class.equals(fieldValue.getClass())) {
                            string = (String) fieldValue;
                            newPredicate = builder.like(builder.lower(path), "%" + string.toLowerCase());
                        }
                        break;

                    case IN:
                        //مقدار فیلد انتیتی در بین یکی از گزینه های لیست مقادیر ورودی دلخواه باشد
                        //SELECT a FROM EntityA a WHERE a.field IN :valueCollection
                        newPredicate = builder.in(path).value(searchFilterRestrictionDto.getFieldValue());
                        break;
                    case NOT_IN:
                        //مقدار فیلد انتیتی در بین هیچ یک از گزینه های لیست مقادیر ورودی دلخواه نباشد
                        //SELECT a FROM EntityA a WHERE a.field NOT IN :valueCollection
                        newPredicate = builder.not(path).in(searchFilterRestrictionDto.getFieldValue());
                        break;
                    case MEMBER_OF:
                        //مقدار ورودی دلخواه عضوی از گزینه های فیلد انتیتی از نوع لیست باشد
                        //SELECT a FROM EntityA a WHERE :value MEMBER OF a.fieldCollection
                        newPredicate = builder.isMember(searchFilterRestrictionDto.getFieldValue(), path);
                        break;
                    case NOT_MEMBER_OF:
                        //مقدار ورودی دلخواه عضوی از گزینه های فیلد انتیتی از نوع لیست نباشد
                        //SELECT a FROM EntityA a WHERE :value NOT MEMBER OF a.fieldCollection
                        newPredicate = builder.isNotMember(searchFilterRestrictionDto.getFieldValue(), path);
                        break;
                }
                if (!ObjectUtils.isEmpty(newPredicate)) {
                    predicate = this.addNextCondition(builder, searchFilterRestrictionDto, predicate, newPredicate);
                }
            } catch (Exception exception) {
                log.error("UTILITY_EXCEPTION.GenericSpecification.toPredicate() exception:",exception);
            }
        }
        return predicate;
    }


    /**
     * این متد سازنده و مدل و شرط قبلی و شرط جدید را از ورودی میگرد و بر اساس نوع and یا or مدل شرط نهایی آن دو شرط ورودی را خروجی میدهد
     *
     * @param builder                      سازنده
     * @param searchFilterRestrictionDto مدل
     * @param predicate                    شرط قبلی
     * @param newPredicate                 شرط جدید
     * @return خروجی: شرط نهایی
     */
    private Predicate addNextCondition(CriteriaBuilder builder, SearchFilterRestrictionDto searchFilterRestrictionDto, Predicate predicate, Predicate newPredicate) {
        if (ObjectUtils.isEmpty(predicate)) {
            predicate = newPredicate;
        } else {
            if (searchFilterRestrictionDto.getNextConditionOperator().equals(SearchFilterNextConditionOperatorEnum.AND)) {
                predicate = builder.and(predicate, newPredicate);
            } else {
                predicate = builder.or(predicate, newPredicate);
            }
        }
        return predicate;
    }


    /**
     * این متد برای به دست آوردن Path فیلد مورد نظر برای اضافه شدن شرایط میباشد. اگر این متد و path استفاده نشود در اینترفیسهای projection که میخواهیم فیلدی با جنس انتیتی را بخوانیم null pointer خواهیم گرفت
     *
     * @param root ریشه انتیتی
     * @param path نام فیلد (در صورت نیاز میتواند چند فیلد درون هم با نقطه از هم جدا شوند)
     * @return خروجی: مقدار path مورد نیاز برای استفاده در شرطها
     */
    private Path getPath(Root root, String path) {
        String[] pathArray = path.split("\\.");
        if (pathArray.length > 1) {
            //اگر داخل نام فیلد نقطه وجود داشته باشد باید به تعداد یکی کمتر از فیلدهای نقطه خورده جوین بزنیم تا بتوانیم روی آخرین جوین path فیلد نهایی را خروجی بدهیم
            Join join = null;
            for (int i = 0; i < pathArray.length - 1; i++) {
                if (join == null) {
                    join = root.join(pathArray[i], JoinType.LEFT);
                } else {
                    join = join.join(pathArray[i], JoinType.LEFT);
                }
            }
            if(join!=null){
                return join.get(pathArray[pathArray.length - 1]);
            }else{
                return root.get(path);
            }
        } else {
            //اگر داخل نام فیلد نقطه وجود نداشته باشد فقط path همان فیلد در root انتیتی را خروجی میدهیم
            return root.get(path);
        }
    }


}
