package site.dittotrip.ditto_trip.auth.contoroller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.BadRequestException;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.*;
import site.dittotrip.ditto_trip.auth.domain.dto.*;
import site.dittotrip.ditto_trip.auth.service.AuthService;

import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
  private final AuthService authService;

  @EventListener(ApplicationReadyEvent.class)
  public void ready() {
    log.info("controller register");
  }

  @GetMapping("/duplication-check")
  @Operation(summary = "이메일 중복 확인",
      description = "")
  public boolean duplicationCheck(String email, String nickname) {
    return authService.duplicationCheck(email, nickname);
  }

  @GetMapping("/send-code")
  @Operation(summary = "인증 코드 전송",
      description = "")
  public String sendCode(String email) throws BadRequestException {
    return authService.sendCode(email);
  }

  @PostMapping("/verify-code")
  @Operation(summary = "인증 코드 확인",
      description = "")
  public boolean verifyCode(@RequestBody VerifyCodeReq dto) {
    return authService.verifyCode(dto.getEmail(), dto.getCode());
  }

  @PostMapping("/signup")
  @Operation(summary = "회원 가입",
      description = "")
  public boolean signup(@RequestBody SignupReq dto, HttpServletResponse response) throws IOException {
    authService.signup(dto);
    return true;
  }

  @PostMapping("/login")
  @Operation(summary = "로그인",
      description = "")
  public TokenRes login(@RequestBody LoginReq dto) throws Exception {
    return authService.login(dto);
  }

  @PostMapping("/refresh")
  @Operation(summary = "토큰 재발급",
      description = "")
  public TokenRes refreshToken(@RequestHeader("Authorization") String refreshToken) {
    return authService.refresh(refreshToken);
  }

  @DeleteMapping("/logout")
  @Operation(summary = "로그아웃",
      description = "")
  public void logout(@RequestHeader("Authorization") String refreshToken) {
    authService.logout(refreshToken);
  }

  @PostMapping("/reset-password")
  @Operation(summary = "비밀번호 재발급",
      description = "")
  public void resetPassword(@RequestBody ResetPasswordReq dto) {
    authService.resetPassword(dto.getEmail());
  }
}
