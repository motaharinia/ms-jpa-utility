package com.motaharinia.msjpautility.search.data;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
* @author https://github.com/motaharinia<br>
 * این کلاس اطلاعاتی از هر یک از سطرهای گرید ارائه میکند
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchDataRowModel implements Serializable {
    /**
     * شناسه سطر داده جستجو
     */
    private Integer id;
    /**
     * آرایه مقادیر ستونهای دیتای جستجو
     */
    private Object[] rowCellArray;
}
