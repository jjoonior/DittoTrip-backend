package site.dittotrip.ditto_trip.userpage.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.dittotrip.ditto_trip.auth.domain.CustomUserDetails;
import site.dittotrip.ditto_trip.userpage.domain.dto.UserPageRes;
import site.dittotrip.ditto_trip.userpage.service.UserPageService;

import static site.dittotrip.ditto_trip.auth.domain.CustomUserDetails.getUserIdFromUserDetails;

/**
 * 타인의 유저 페이지와 내 유저 페이지 조회의 데이터가 다를 경우 수정 필요
 * 1. 유저 페이지 조회
 */
@RestController
@RequestMapping("/user-page")
@RequiredArgsConstructor
public class UserPageController {

    private final UserPageService userPageService;

    @GetMapping("/{userId}")
    @Operation(summary = "유저 페이지 조회",
            description = "")
    public UserPageRes userPage(@AuthenticationPrincipal CustomUserDetails userDetails,
                                @PathVariable(name = "userId") Long userId) {
        Long reqUserId = getUserIdFromUserDetails(userDetails, false);
        return userPageService.findUserPage(reqUserId, userId);
    }

    @GetMapping("/my")
    @Operation(summary = "내 유저 페이지 조회",
            description = "")
    public UserPageRes myUserPage(@AuthenticationPrincipal CustomUserDetails userDetails) {
        Long reqUserId = getUserIdFromUserDetails(userDetails, false);
        return userPageService.findUserPage(reqUserId, reqUserId);
    }


}
