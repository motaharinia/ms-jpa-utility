
package com.motaharinia.msjpautility.document;


import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.*;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
* @author https://github.com/motaharinia<br>
 *     این کلاس به عنوان کلاس پدر تمام کلاسهای داکیومنت آماده شده است و به تمام داکیومنت ها فیلدهای استانداردی را اضافه میکتد
 */


//https://docs.spring.io/spring-data/data-mongo/docs/current/reference/html/#mapping-usage-annotations
//https://codeboje.de/spring-data-auditing/


@Document(collection = "custom_document")
@Getter
@Setter
public class CustomDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * آی پی کاربری که داکیومنت را ایجاد کرده است
     */
    @Field(name = "create_user_ip")
    private String createUserIp;
    /**
     * آی پی کاربری که داکیومنت را آخرین بار ویرایش کرده است
     */
    @Field(name = "update_user_ip")
    private String updateUserIp;

    /**
     * عدم نمایش داکیومنت در بیزینس نرم افزار
     */
    @Field(name = "hidden")
    private Boolean hidden = false;
    /**
     * غیر فعال کردن داکیومنت در بیزینس نرم افزار
     */
    @Field(name = "invalid")
    private Boolean invalid = false;

    /**
     * تاریخی که داکیومنت ایجاد شده است
     */
    @CreatedDate
    @Field(name = "create_at",  targetType = FieldType.TIMESTAMP)
    private LocalDateTime createAt;
    /**
     * تاریخی که داکیومنت آخرین بار ویرایش شده است
     */
    @LastModifiedDate
    @Field(name = "update_at", targetType = FieldType.TIMESTAMP)
    private LocalDateTime updateAt;

    /**
     * آی دی کاربری که داکیومنت را ایجاد کرده است
     */
    @CreatedBy
    @Field(name = "create_user_id")
    private Long createUserId;
    /**
     * آی دی کاربری که داکیومنت را آخرین بار ویرایش کرده است
     */
    @LastModifiedBy
    @Field(name = "update_user_id")
    private Long updateUserId;

    /**
     * هربار که سطری ویرایش شود یک عدد یه این فیلد اضافه میشود
     */
    @Version
    private Long version;
}
