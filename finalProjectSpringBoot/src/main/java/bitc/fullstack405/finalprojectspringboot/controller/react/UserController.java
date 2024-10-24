package bitc.fullstack405.finalprojectspringboot.controller.react;

import bitc.fullstack405.finalprojectspringboot.database.dto.event.LoginDTO;
import bitc.fullstack405.finalprojectspringboot.database.dto.user.UserListForManageDTO;
import bitc.fullstack405.finalprojectspringboot.database.entity.Role;
import bitc.fullstack405.finalprojectspringboot.database.entity.UserEntity;
import bitc.fullstack405.finalprojectspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

  private final UserService userService;

//  Web 로그인
  @PostMapping("/login")
  public ResponseEntity<LoginDTO> login(@RequestBody Map<String, String> loginData) {
    String userAccount = loginData.get("userAccount");
    String userPw = loginData.get("userPw");

    LoginDTO loginDTO = userService.login(userAccount, userPw);

    if (loginDTO != null) {
      return ResponseEntity.ok(loginDTO);
    }
    else {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
  }

//  유저 관리 리스트 출력
  @GetMapping("/userManage")
  public ResponseEntity<List<UserListForManageDTO>> userManage() {
    List<UserEntity> userEntities = userService.userListForManage();

    List<UserListForManageDTO> userListForManageDTOs = userEntities.stream()
        .map(user -> UserListForManageDTO.builder()
            .userId(user.getUserId())
            .userAccount(user.getUserAccount())
            .name(user.getName())
            .userPhone(user.getUserPhone())
            .userDepart(user.getUserDepart())
            .role(user.getRole())
            .build())
        .collect(Collectors.toList());

    return ResponseEntity.ok(userListForManageDTOs);
  }

//  가입대기 유저 승인
  @PutMapping("/signAccept/{userId}")
  public ResponseEntity<Void> signAccept(@PathVariable Long userId) {

    userService.signAccept(userId);

    return ResponseEntity.ok().build();
  }

//  관리자가 직접 회원탈퇴 시키기(관리자권한, 탈퇴처리)
  @DeleteMapping("/signOut/{userId}")
  public ResponseEntity<Void> signOut(@PathVariable Long userId) {

    userService.signOut(userId);

    return ResponseEntity.ok().build();
  }

//  Web 데이터베이스 추가용 회원가입
  @PutMapping("/signup")
  public ResponseEntity<UserEntity> signup(@RequestBody Map<String, String> signupData) {
    String userPermission = signupData.get("userPermission");
    Role userPerm = null;

    if (Objects.equals(userPermission, "협회장")) {
      userPerm = Role.ROLE_PRESIDENT;
    }
    else if (Objects.equals(userPermission, "총무")) {
      userPerm = Role.ROLE_SECRETARY;
    }
    else if (Objects.equals(userPermission, "정회원")) {
      userPerm = Role.ROLE_REGULAR;
    }
    else if (Objects.equals(userPermission, "준회원")) {
      userPerm = Role.ROLE_ASSOCIATE;
    }
    UserEntity userEntity = UserEntity.builder()
        .userAccount(signupData.get("userAccount"))
        .userDepart(signupData.get("userDepart"))
        .userPhone(signupData.get("userPhone"))
        .password(signupData.get("userPw"))
        .name(signupData.get("name"))
        .role(userPerm)
        .build();

    userService.signup(userEntity);

    return ResponseEntity.ok(userEntity);
  }
}
