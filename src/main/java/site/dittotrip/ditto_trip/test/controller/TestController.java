package site.dittotrip.ditto_trip.test.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import site.dittotrip.ditto_trip.user.domain.User;
import site.dittotrip.ditto_trip.user.repository.UserRepository;
import site.dittotrip.ditto_trip.utils.JwtProvider;

import java.util.Optional;

/**
 * 1. 테스트용 액세스 토큰 발급
 */
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TestController {

    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;

    @GetMapping("/access-token")
    public String issueTestToken() {
        User user = userRepository.findById(1L).get();
        return jwtProvider.generateToken(user.getEmail(), user.getAuthorities());
    }

}
