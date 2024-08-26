package site.dittotrip.ditto_trip.spot.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import site.dittotrip.ditto_trip.auth.service.CustomUserDetails;
import site.dittotrip.ditto_trip.spot.domain.dto.SpotDetailRes;
import site.dittotrip.ditto_trip.spot.domain.dto.SpotListInMapRes;
import site.dittotrip.ditto_trip.spot.domain.dto.SpotListRes;
import site.dittotrip.ditto_trip.spot.domain.dto.SpotVisitListRes;
import site.dittotrip.ditto_trip.spot.service.SpotBookmarkService;
import site.dittotrip.ditto_trip.spot.service.SpotService;
import site.dittotrip.ditto_trip.user.domain.User;

import static site.dittotrip.ditto_trip.auth.service.CustomUserDetails.*;

/**
 * 1. Spot 리스트 지도 기반 조회 (카테고리 선택 후)
 * 2. 즐겨찾는 Spot 리스트 조회
 * 3. Spot 리스트 검색 조회
 * 4. 방문 spot 리스트 조회
 * 5. Spot 상세 조회
 */
@RestController
@RequiredArgsConstructor
public class SpotController {

    private final SpotService spotService;
    private final SpotBookmarkService spotBookmarkService;

    @GetMapping("/category/{categoryId}/spot/list")
    public SpotListInMapRes spotListInMap(@PathVariable(name = "categoryId") Long categoryId,
                                          @AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestParam(name = "startX") Double startX, @RequestParam(name = "endX") Double endX,
                                          @RequestParam(name = "startY") Double startY, @RequestParam(name = "endY") Double endY) {
        User user = getUserFromUserDetails(userDetails, false);
        return spotService.findSpotListInMap(categoryId, user, startX, endX, startY, endY);
    }

    @GetMapping("/spot/list/bookmark")
    public SpotListRes spotListByBookmark(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails, true);
        return spotService.findSpotListByBookmark(user);
    }

    @GetMapping("/spot/list/search")
    public SpotListRes spotListBySearch(@AuthenticationPrincipal CustomUserDetails userDetails,
                                        @RequestParam(name = "query") String query,
                                        @RequestParam(name = "page") Integer page) {
        User user = getUserFromUserDetails(userDetails, false);
        return spotService.findSpotListBySearch(user, query, page);
    }

    @GetMapping("/spot/list/visit")
    public SpotVisitListRes spotVisitList(@AuthenticationPrincipal CustomUserDetails userDetails,
                                          @RequestParam(name = "page") Integer page) {
        User user = getUserFromUserDetails(userDetails, true);
        return spotService.findSpotVisitList(user, page);
    }

    @GetMapping("/spot/{spotId}")
    public SpotDetailRes categoryDetail(@PathVariable(name = "spotId") Long spotId,
                                        @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails, false);
        return spotService.findSpotDetail(spotId, user);
    }

    /**
     * SpotBookmark
     */
    @PostMapping("/spot/{spotId}/bookmark")
    public void spotBookmarkAdd(@PathVariable(name = "spotId") Long spotId,
                                @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails, true);
        spotBookmarkService.addSpotBookmark(spotId, user);
    }

    @DeleteMapping("/spot/{spotId}/bookmark")
    public void spotBookmarkRemove(@PathVariable(name = "spotId") Long spotId,
                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = getUserFromUserDetails(userDetails, true);
        spotBookmarkService.removeSpotBookmark(spotId, user);
    }

}
