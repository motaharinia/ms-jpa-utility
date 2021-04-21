package com.motaharinia.msjpautility;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
* @author https://github.com/motaharinia<br>
 * پروژه یوتیلیتی کمک کننده به پروژه های دیگر مایکروسرویسی دیگر اسپرینگ بوت<br>
 * میتوان با اضافه کردن وابستگی این پروژه به پروژه اصلی از کلاسها و متدهای یوتیلیتی آن استفاده نمود<br>
 *     برای مطالعه نحوه استفاده از این پروژه توضیحات انتهای فایل pom.xml آن را مطالعه کنید
 */
@SpringBootApplication(scanBasePackages = {"com.motaharinia"})
public class MsJpaUtilityApplication {

    public static void main(String[] args) {
        SpringApplication.run(MsJpaUtilityApplication.class, args);
    }

}
