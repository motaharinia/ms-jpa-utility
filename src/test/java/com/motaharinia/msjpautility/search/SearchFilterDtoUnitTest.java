package com.motaharinia.msjpautility.search;

import com.motaharinia.msutility.custom.customdto.search.filter.SearchFilterDto;
import com.motaharinia.msutility.custom.customdto.search.filter.restriction.SearchFilterNextConditionOperatorEnum;
import com.motaharinia.msutility.custom.customdto.search.filter.restriction.SearchFilterOperationEnum;
import com.motaharinia.msutility.custom.customdto.search.filter.restriction.SearchFilterRestrictionDto;
import com.motaharinia.msutility.custom.customdto.search.filter.sort.SearchFilterSortDto;
import com.motaharinia.msutility.custom.customdto.search.filter.sort.SearchFilterSortTypeEnum;
import com.motaharinia.msutility.custom.customjson.CustomObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;

/**
* @author https://github.com/motaharinia<br>
 * کلاس تست SearchFilterDto
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
class SearchFilterDtoUnitTest {

    private final CustomObjectMapper mapper = new CustomObjectMapper();

    /**
     * این متد مقادیر پیش فرض قبل از هر تست این کلاس تست را مقداردهی اولیه میکند
     */
    @BeforeEach
    void initUseCase() {
        Locale.setDefault(Locale.US);
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
    void serializeDeserializeTest() {
        try {
            Locale.setDefault(new Locale("fa", "IR"));

            List<SearchFilterRestrictionDto> searchFilterRestrictionDtoList = new ArrayList<>();
            searchFilterRestrictionDtoList.add(new SearchFilterRestrictionDto("firstName", SearchFilterOperationEnum.MATCH, "mostafa", SearchFilterNextConditionOperatorEnum.AND));
            List<SearchFilterSortDto> searchFilterSortDtoList = new ArrayList<>();
            searchFilterSortDtoList.add(new SearchFilterSortDto("lastName", SearchFilterSortTypeEnum.ASC));
            SearchFilterDto searchFilterDto = new SearchFilterDto();
            searchFilterDto.setPageNo(1L);
            searchFilterDto.setPageRowSize(10L);
            searchFilterDto.setRestrictionList(searchFilterRestrictionDtoList);
            searchFilterDto.setSortList(searchFilterSortDtoList);

            String json = mapper.writeValueAsString(searchFilterDto);
            SearchFilterDto searchFilterDto2 = mapper.readValue(json, SearchFilterDto.class);
            assertThat(searchFilterDto2.getRestrictionList().get(0).getFieldValue()).isEqualTo("mostafa");
        } catch (Exception ex) {
            fail(ex.toString());
        }
    }
}
