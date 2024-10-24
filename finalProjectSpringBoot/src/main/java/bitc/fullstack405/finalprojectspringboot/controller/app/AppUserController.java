package bitc.fullstack405.finalprojectspringboot.controller.app;

import bitc.fullstack405.finalprojectspringboot.database.dto.app.user.CheckedIdResponse;
import bitc.fullstack405.finalprojectspringboot.database.dto.app.user.InsertUserRequest;
import bitc.fullstack405.finalprojectspringboot.database.dto.app.user.LoginResponse;
import bitc.fullstack405.finalprojectspringboot.database.dto.app.user.UpdateAppUserRequest;
import bitc.fullstack405.finalprojectspringboot.database.entity.UserEntity;
import bitc.fullstack405.finalprojectspringboot.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class AppUserController {

  private final UserService userService;

  // 로그인 회원정보 찾기
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> findLoginUser(@RequestBody UserEntity user){
    LoginResponse res = userService.findLoginUser(user.getUserAccount(),user.getPassword());
    return ResponseEntity.ok().body(res);
  }

  // 회원가입
  @PostMapping("/signup")
  public ResponseEntity<UserEntity> insertUser(@RequestBody InsertUserRequest req){
    System.out.println(req);
    UserEntity save = userService.save(req);

    return ResponseEntity.ok().body(save);
  }

  // 아이디 중복확인
  @GetMapping("/signup/{userAccount}")
  public ResponseEntity<CheckedIdResponse> checkUserAccount(@PathVariable("userAccount") String userAccount){
    CheckedIdResponse user = userService.findUserAccount(userAccount);

    if(user == null){ // 같은 계정이 없으면 null
      return ResponseEntity.ok().build();
    }
    return ResponseEntity.ok(user); // 있으면 user 전송
  }

  // 회원 1명 정보
  @GetMapping("/app/user/{userId}")
  public ResponseEntity<LoginResponse> findByUserId(@PathVariable("userId") Long userId){
    LoginResponse user = userService.findByUserId(userId);
    return ResponseEntity.ok(user);
  }

  // 회원 정보 업데이트
  @PutMapping("/app/user/{userId}")
  public ResponseEntity<Void> updateAppUser(@PathVariable("userId") Long userId, @RequestBody UpdateAppUserRequest req){
    userService.updateAppUser(userId, req);

    return ResponseEntity.ok().build();
  }

  // 회원 탈퇴
  @PutMapping("/app/user/delete/{userId}")
  public ResponseEntity<Void> deleteAppUser(@PathVariable("userId") Long userId){
    userService.deleteAppUser(userId);

    return ResponseEntity.ok().build();
  }
}
