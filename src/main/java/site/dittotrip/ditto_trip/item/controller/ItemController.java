package site.dittotrip.ditto_trip.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.dittotrip.ditto_trip.auth.service.CustomUserDetails;
import site.dittotrip.ditto_trip.item.domain.UserBadge;
import site.dittotrip.ditto_trip.item.domain.dto.UserBadgeListRes;
import site.dittotrip.ditto_trip.item.domain.dto.UserItemListRes;
import site.dittotrip.ditto_trip.item.service.ItemService;
import site.dittotrip.ditto_trip.user.domain.User;

/**
 * 1. 유저의 보유 아이템 리스트 조회 (자신의 아이템 리스트만 조회 가능 (수정 시))
 * 2. 유저의 보유 뱃지 리스트 조회 (타 유저도 가능)
 */
@RestController
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;

    @GetMapping("/item/list")
    public UserItemListRes usersItemList(@AuthenticationPrincipal CustomUserDetails userDetails) {
        User user = userDetails.getUser();
        return itemService.findUsersItemList(user);
    }

    @GetMapping("/user/{userId}/badge/list")
    public UserBadgeListRes usersBadgeListRes(@PathVariable(name = "userId") Long userId) {
        return itemService.findUsersBadgeList(userId);
    }

}