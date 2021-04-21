package com.motaharinia.msjpautility.search;

import java.io.Serializable;

/**
* @author https://github.com/motaharinia<br>
 *     اینترفیس پدر تمامی اینترفیسهای جستجوی پیشرفته دیتابیس که حکم میکند تمام آنها باید متد گتر آی دی را داشته باشند<br>
 * تمام اینترفیسهای جستجوی پیشرفته دیتابیس باید از این اینترفیس گسترش یابند
 */
public interface SearchRowView extends Serializable{

    Integer getId();

    String toOut();
}
