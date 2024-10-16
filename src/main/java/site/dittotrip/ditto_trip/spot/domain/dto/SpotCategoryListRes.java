package site.dittotrip.ditto_trip.spot.domain.dto;

import lombok.Data;
import site.dittotrip.ditto_trip.category.domain.Category;
import site.dittotrip.ditto_trip.category.domain.dto.CategoryData;

import java.util.ArrayList;
import java.util.List;

@Data
public class SpotCategoryListRes {

    private CategoryData categoryData;
    private List<SpotData> spotDataList = new ArrayList<>();
    private Integer totalPages;

}
