
package com.motaharinia.msjpautility.entity;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDateTime;

//https://vladmihalcea.com/prepersist-preupdate-embeddable-jpa-hibernate/
//https://stackoverflow.com/questions/49954812/how-can-you-make-a-created-at-column-generate-the-creation-date-time-automatical/49954903

/**
* @author https://github.com/motaharinia<br>
 *     این کلاس به عنوان کلاس پدر تمام کلاسهای انتیتی آماده شده است و به تمام انتیتی ها فیلدهای استانداردی را اضافه میکتد
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Data
public class CustomEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * آی پی کاربری که انتیتی را ایجاد کرده است
     */
    @Column(name = "create_user_ip")
    private String createUserIp;
    /**
     * آی پی کاربری که انتیتی را آخرین بار ویرایش کرده است
     */
    @Column(name = "update_user_ip")
    private String updateUserIp;

    /**
     * عدم نمایش انتیتی در بیزینس نرم افزار
     */
    @Column(name = "hidden")
    private Boolean hidden = false;
    /**
     * غیر فعال کردن انتیتی در بیزینس نرم افزار
     */
    @Column(name = "invalid")
    private Boolean invalid = false;

    /**
     * تاریخی که انتیتی ایجاد شده است
     */
    @CreatedDate
    @Column(name = "create_at", columnDefinition = DbColumnDefinition.COLUMN_DEFINITION_TIMESTAMP)
    private LocalDateTime createAt;
    /**
     * تاریخی که انتیتی آخرین بار ویرایش شده است
     */
    @LastModifiedDate
    @Column(name = "update_at", columnDefinition = DbColumnDefinition.COLUMN_DEFINITION_TIMESTAMP)
    private LocalDateTime updateAt;

    /**
     * آی دی کاربری که انتیتی را ایجاد کرده است
     */
    @CreatedBy
    @Column(name = "create_user_id")
    private Long createUserId;
    /**
     * آی دی کاربری که انتیتی را آخرین بار ویرایش کرده است
     */
    @LastModifiedBy
    @Column(name = "update_user_id")
    private Long updateUserId;

}
