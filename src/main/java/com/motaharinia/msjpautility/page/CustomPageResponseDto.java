package com.motaharinia.msjpautility.page;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author eng.motahari@gmail.com<br>
 * کلاس مدل پاسخ صفحه بندی به کلاینت
 */

@Data
@NoArgsConstructor
public class CustomPageResponseDto<T> implements Serializable {
    /**
     * تعداد کل صفحات
     */
    private int totalPages;
    /**
     * تعداد کل سطرهای موجود قبل از صفحه بندی
     */
    private long totalElements;
    /**
     *تعداد سطر درخواستی در هر صفحه
     * مثلا ابتدا خواستیم هر صفحه 30 سطر داشته باشد و مقدار این فیلد 30
     */
    private int size;
    /**
     *شماره صفحه فعلی
     */
    private int page;
    /**
     * تعداد سطر واقعی موجود در این صفحه
     * مثلا ابتدا خواستیم هر صفحه 30 سطر داشته باشد ولی در دیتابیس کلا 10 سطر موجود بوده و مقدار این فیلد 10 میشود
     */
    private int numberOfElements;
    /**
     * لیست سطرهای داده
     */
    private List<T> content = new ArrayList<>();


    /**
     * اولین صفحه است؟
     */
    private boolean first;
    /**
     * آخرین صفحه است
     */
    private boolean last;
    /**
     * خالی است؟
     */
    private boolean empty;

    /**
     * متد سازنده
     * @param page
     */
    public CustomPageResponseDto(Page page) {
       this.totalPages= page.getTotalPages();
       this.totalElements=page.getTotalElements();
       this.size=page.getSize();
       this.page=page.getNumber();
       this.numberOfElements=page.getNumberOfElements();
       this.content=page.getContent();
       this.first=page.isFirst();
       this.last=page.isLast();
       this.empty=page.isEmpty();
    }
}
