package com.motaharinia.msjpautility.search.sample;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.motaharinia.msjpautility.search.SearchRowView;
import com.motaharinia.msjpautility.search.annotation.SearchDataColumn;

import java.util.Date;

/**
* @author https://github.com/motaharinia<br>
 *     اینترفیس نمونه جهت تست SearchDataModelTests
 */
@JsonDeserialize
public interface SearchRowViewAdminUserBrief extends SearchRowView {

    @SearchDataColumn(index = 1,name = "id")
    Integer getId();

    @SearchDataColumn(index = 2,name = "firstName")
    String getFirstName();

    @SearchDataColumn(index = 3,name = "lastName")
    String getLastName();

    @SearchDataColumn(index = 4,name = "dateOfBirth")
    Date getDateOfBirth();

    @SearchDataColumn(index = -1,name = "defaultAdminUserContact")
    DefaultAdminUserContact getDefaultAdminUserContact();


    interface DefaultAdminUserContact {
        @SearchDataColumn(index = 5,name = "address")
        String getAddress();
    }


    @Override
    default String toOut() {
        return this.getId() + "," + this.getFirstName() + "," + this.getLastName();
    }
}
