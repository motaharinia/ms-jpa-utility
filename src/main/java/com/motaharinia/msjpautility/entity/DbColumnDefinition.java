package com.motaharinia.msjpautility.entity;

import org.jetbrains.annotations.NotNull;
import org.springframework.util.ObjectUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
* @author https://github.com/motaharinia<br>
 * این کلاس برای مدیریت یکپارچه متغیرهای مورد نیاز برای دیتابیس در سطح انتیتی ها آماده شده است
 */
public interface DbColumnDefinition {

    /**
     * تعریف میزان محاسه عدد اعشار تا 5 رقم
     */
    Integer BIG_DECIMAL_SCALE = 5;
    /**
     * تعریف نوع داده میزان پول با طول عدد صحیح 19 رقم و طول اعشار 4 رقم
     */
    String COLUMN_DEFINITION_AMOUNT_194 = "DECIMAL(19,4)";
    /**
     * تعریف نوع داده اعشاری با طول عدد صحیح 8 رقم و طول اعشار 4 رقم
     */
    String COLUMN_DEFINITION_DOUBLE_84 = "DECIMAL(8,4)";
    /**
     * تعریف نوع داده اعشاری با طول عدد صحیح 14 رقم و طول اعشار 4 رقم
     */
    String COLUMN_DEFINITION_DOUBLE_144 = "DECIMAL(14,4)";
    /**
     * تعریف نوع داده تاریخ معادل java.time.LocalDate
     */
    String COLUMN_DEFINITION_DATE = "DATE";
    /**
     * تعریف نوع داده زمان معادل java.time.LocalTime
     */
    String COLUMN_DEFINITION_TIME = "TIME";
    /**
     * تعریف نوع داده تاریخ-زمان معادل java.time.LocalDateTime
     */
    String COLUMN_DEFINITION_TIMESTAMP = "TIMESTAMP";
    /**
     * تعریف نوع داده زمان همراه با اختلاف زمانی معادل java.time.OffsetTime
     */
    String COLUMN_DEFINITION_TIME_WITH_TIMEZONE = "TIME_WITH_TIMEZONE";
    /**
     * تعریف نوع داده تاریخ-زمان همراه با اختلاف زمانی معادل java.time.OffsetDateTime
     */
    String COLUMN_DEFINITION_TIMESTAMP_WITH_TIMEZONE = "TIMESTAMP_WITH_TIMEZONE";
    /**
     * تعریف نوع داده کلید اصلی انتیتی در دیتابیس
     */
    String COLUMN_DEFINITION_PRIMARY_KEY = "NUMBER";

    /**
     * این متد برای تبدیل رشته تاریخ به عبارت متناظر آن در دیتابیس اوراکل میباشد
     *
     * @param dateString رشته تاریخ
     * @return خروجی: رشته متناسب تاریخ در دیتابیس اوراکل
     */
    @NotNull
    static String toDate(@NotNull String dateString) {
        if (ObjectUtils.isEmpty(dateString)) {
            dateString = "null";
        }
        return "TO_DATE('" + dateString + "','YYYY-MM-DD HH24:MI:SS')";
    }

    /**
     * این متد برای تبدیل Date تاریخ به عبارت متناظر آن در دیتابیس اوراکل میباشد
     *
     * @param date Date تاریخ
     * @return خروجی: رشته متناسب تاریخ در دیتابیس اوراکل
     */
    @NotNull
    static String toDate(@NotNull Date date) {
        if (!ObjectUtils.isEmpty(date)) {
            return toDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));
        } else {
            return toDate("null");
        }
    }

    /**
     * این متد ، متد fixQueryIN را با شرط IN فراخوانی میکند
     *
     * @param jpqlFieldName نام فیلد مورد نظر برای کوئری
     * @param itemCsv       مقادیر csv
     * @return خروجی: کوئری مناسب که مشکل محدودیت تعداد csv های دستور IN در اوراکل را نداشته باشد
     */
    static String fixQueryIN(String jpqlFieldName, String itemCsv) {
        return fixQueryIN(jpqlFieldName, itemCsv, " IN ");
    }

    /**
     * این متد ، متد fixQueryIN را با شرط NOT IN فراخوانی میکند
     *
     * @param jpqlFieldName نام فیلد مورد نظر برای کوئری
     * @param itemCsv       مقادیر csv
     * @return خروجی: کوئری مناسب که مشکل محدودیت تعداد csv های دستور IN در اوراکل را نداشته باشد
     */
    static String fixQueryNOTIN(String jpqlFieldName, String itemCsv) {
        return fixQueryIN(jpqlFieldName, itemCsv, " NOT IN ");
    }

    /**
     * این متد به دلیل محدودیتی که اوراکل در استفاده از دستور IN دارد نام فیلد و تعداد مقادیر به صورت csv و نوع IN یا NOTIN را میگیرد و در صورتی که تعداد مقادیر csv بیش از تعداد مجاز اوراکل باشد به چند شرط تبدیل میکند
     *
     * @param jpqlFieldName نام فیلد مورد نظر برای کوئری
     * @param itemCsv       مقادیر csv
     * @param inOrNotIn     کلمه IN یا NOTIN
     * @return خروجی: کوئری مناسب که مشکل محدودیت تعداد csv های دستور IN در اوراکل را نداشته باشد
     */
    static String fixQueryIN(String jpqlFieldName, String itemCsv, String inOrNotIn) {
        List<String> itemList = new ArrayList<>(Arrays.asList(itemCsv.split(",")));
        int i = 1;
        Integer counter = 1;
        StringBuilder resultInStringBuilder = new StringBuilder();
        StringBuilder resultStringBuilder = new StringBuilder();
        for (String item : itemList) {
            //تولید csv مقادیر
            if (!resultInStringBuilder.toString().equals("")) {
                resultInStringBuilder.append(",");
            }
            resultInStringBuilder.append(item);
            if (i < 1000) {
                //تا زمانی که تعداد مقادیر csv به 1000 نرسیده به i اضافه میکنیم
                i++;
                //اگر به انتهای لیست رسیدیم به result مقادیر csv آماده شده را اضافه میکنیم
                if (counter.equals(itemList.size())) {
                    if (!resultStringBuilder.toString().equals("")) {
                        resultStringBuilder.append(" OR ");
                    }
                    resultStringBuilder.append("(").append(jpqlFieldName).append(inOrNotIn).append("(").append(resultInStringBuilder.toString()).append("))");
                }
            } else {
                //وقتی تعداد مقادیر csv شده 1000 میشود در else میفتد به result مقادیر csv آماده شده را اضافه میکنیم
                if (!resultStringBuilder.toString().equals("")) {
                    resultStringBuilder.append(" OR ");
                }
                resultStringBuilder.append("(").append(jpqlFieldName).append(inOrNotIn).append("(").append(resultInStringBuilder.toString()).append("))");
                //مقادیر i و resultIN بعد از هر 1000 تا ریست میشوند
                resultInStringBuilder.setLength(0);
                i = 1;
            }
            counter++;
        }
        return "(" + resultStringBuilder.toString() + ")";
    }
}