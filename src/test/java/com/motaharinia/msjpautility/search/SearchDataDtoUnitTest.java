package com.motaharinia.msjpautility.search;

import com.motaharinia.msjpautility.search.sample.SearchRowViewAdminUserBriefImpl;
import com.motaharinia.msjpautility.search.sample.SearchRowViewAdminUserBrief;
import com.motaharinia.msutility.custom.customdto.search.data.SearchDataDto;
import com.motaharinia.msutility.custom.customdto.search.filter.SearchFilterDto;
import com.motaharinia.msutility.custom.customdto.search.filter.restriction.SearchFilterNextConditionOperatorEnum;
import com.motaharinia.msutility.custom.customdto.search.filter.restriction.SearchFilterOperationEnum;
import com.motaharinia.msutility.custom.customdto.search.filter.restriction.SearchFilterRestrictionDto;
import com.motaharinia.msutility.custom.customdto.search.filter.sort.SearchFilterSortDto;
import com.motaharinia.msutility.custom.customdto.search.filter.sort.SearchFilterSortTypeEnum;
import com.motaharinia.msutility.custom.customfield.CustomDate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
* @author https://github.com/motaharinia<br>
 * کلاس تست SearchDataDto
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class SearchDataDtoUnitTest {

    /**
     * این متد مقادیر پیش فرض قبل از هر تست این کلاس تست را مقداردهی اولیه میکند
     */
    @BeforeEach
    void initUseCase() {
        Locale.setDefault(new Locale("fa", "IR"));
    }

    /**
     * این متد بعد از هر تست این کلاس اجرا میشود
     */
    @AfterEach
    void finalizeEach() {
        Locale.setDefault(Locale.US);
    }

    @Order(1)
    @Test
    void serializeTest()  {
        try {
            CustomDate dateOfBirth=new CustomDate();
            dateOfBirth.setYear(1399);
            dateOfBirth.setMonth(4);
            dateOfBirth.setDay(3);


            Locale.setDefault(new Locale("fa", "IR"));
            List<SearchFilterRestrictionDto> searchFilterRestrictionDtoList = new ArrayList<>();
            searchFilterRestrictionDtoList.add(new SearchFilterRestrictionDto("firstName", SearchFilterOperationEnum.MATCH, "mostafa", SearchFilterNextConditionOperatorEnum.AND));
            searchFilterRestrictionDtoList.add(new SearchFilterRestrictionDto("dateOfBirth", SearchFilterOperationEnum.GREATER_THAN, dateOfBirth, SearchFilterNextConditionOperatorEnum.AND));
            List<SearchFilterSortDto> searchFilterSortDtoList = new ArrayList<>();
            searchFilterSortDtoList.add(new SearchFilterSortDto("firstName", SearchFilterSortTypeEnum.ASC));
            searchFilterSortDtoList.add(new SearchFilterSortDto("lastName", SearchFilterSortTypeEnum.DSC));
            SearchFilterDto searchFilterDto = new SearchFilterDto();
            searchFilterDto.setPageNo(1);
            searchFilterDto.setPageRowSize(10);
            searchFilterDto.setRestrictionList(searchFilterRestrictionDtoList);
            searchFilterDto.setSortList(searchFilterSortDtoList);


            List<SearchRowViewAdminUserBrief> viewList = new ArrayList<>();
            viewList.add(new SearchRowViewAdminUserBriefImpl(1));
            viewList.add(new SearchRowViewAdminUserBriefImpl(2));
            Page<SearchRowViewAdminUserBrief> viewPage = new PageImpl<>(viewList);


            SearchDataDto searchDataDto = SearchTools.buildSearchDataDto(viewPage, searchFilterDto,SearchRowViewAdminUserBrief.class, null);
            log.info("UTILITY.searchDataDto.toString():" + searchDataDto.toString());

//            String json = mapper.writeValueAsString(searchDataModel);
            assertThat(searchDataDto.getTotalRecordSize()).isNotNull();
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }
}
