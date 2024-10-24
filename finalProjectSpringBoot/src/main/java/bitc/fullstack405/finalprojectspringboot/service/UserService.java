package bitc.fullstack405.finalprojectspringboot.service;

import bitc.fullstack405.finalprojectspringboot.database.dto.app.user.CheckedIdResponse;
import bitc.fullstack405.finalprojectspringboot.database.dto.app.user.InsertUserRequest;
import bitc.fullstack405.finalprojectspringboot.database.dto.app.user.LoginResponse;
import bitc.fullstack405.finalprojectspringboot.database.dto.app.user.UpdateAppUserRequest;
import bitc.fullstack405.finalprojectspringboot.database.dto.event.LoginDTO;
import bitc.fullstack405.finalprojectspringboot.database.entity.Role;
import bitc.fullstack405.finalprojectspringboot.database.entity.UserEntity;
import bitc.fullstack405.finalprojectspringboot.database.repository.EventAppRepository;
import bitc.fullstack405.finalprojectspringboot.database.repository.EventRepository;
import bitc.fullstack405.finalprojectspringboot.database.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.List;

@RequiredArgsConstructor
@Service
public class UserService {

  private final UserRepository userRepository;
  private final EventAppRepository eventAppRepository;
  private final EventRepository eventRepository;

  ///////////////////////////
  ////////// <APP> //////////
  ///////////////////////////

  public LoginResponse findLoginUser(String userAccount, String password) {
    UserEntity user = userRepository.findUserByUserAccount(userAccount);// 아이디가 존재하는 user 정보 가져옴
    if(user!=null && user.getPassword().equals(password)) {
      return new LoginResponse(user);
    }

    return null;
  }

  public UserEntity save(InsertUserRequest req) {
    return userRepository.save(req.toUserEntity());
  }

  public CheckedIdResponse findUserAccount(String userAccount) {
    UserEntity user = userRepository.findUserByUserAccount(userAccount);
    return new CheckedIdResponse(Objects.requireNonNull(user));
  }

  public LoginResponse findByUserId(Long userId) {
    UserEntity user = userRepository.findByUserId(userId);
    return new LoginResponse(user);
  }

  @Transactional
  public void updateAppUser(Long userId, UpdateAppUserRequest req) {
    UserEntity user = userRepository.findByUserId(userId);
    user.updateAppUser(req.getPassword(), req.getUserPhone(), req.getUserDepart());
  }

  @Transactional
  public void deleteAppUser(Long userId) {
    UserEntity user = userRepository.findByUserId(userId);
    user.deleteAppUser();
  }


  ///////////////////////////
  ////////// <WEB> //////////
  ///////////////////////////

  //  로그인
  public LoginDTO login(String userAccount, String userPw) {
    UserEntity user = userRepository.findByUserAccountAndPassword(userAccount, userPw);

    LoginDTO loginDTO = LoginDTO.builder()
            .userAccount(user.getUserAccount())
            .name(user.getName())
            .role(user.getRole())
            .userDepart(user.getUserDepart())
            .userPhone(user.getUserPhone())
            .userId(user.getUserId())
            .build();

    return loginDTO;
  }

  //  회원가입(테스터용)
  public void signup(UserEntity userEntity) {
    userRepository.save(userEntity);
  }

  //  유저목록출력 (협회장 - 총무 - 준회원 - 정회원 - 탈퇴회원 순서)
  public List<UserEntity> userListForManage() {
    return userRepository.findUsersForManage();
  }


  //  회원가입승인
  @Transactional
  public void signAccept(Long userId) {
    UserEntity userEntity = userRepository.findById(userId).orElse(null);

    if (userEntity.getRole() == Role.ROLE_ASSOCIATE) {
      UserEntity updateUser = userEntity.toBuilder()
              .role(Role.ROLE_REGULAR)
              .userAccount(userEntity.getUserAccount())
              .name(userEntity.getName())
              .userDepart(userEntity.getUserDepart())
              .userPhone(userEntity.getUserPhone())
              .approvedEventList(userEntity.getApprovedEventList())
              .password(userEntity.getPassword())
              .attendAppList(userEntity.getAttendAppList())
              .postedEventList(userEntity.getPostedEventList())
              .attendInfoList(userEntity.getAttendInfoList())
              .build();

      userRepository.save(updateUser);
    }
  }

  //  회원강제탈퇴
  @Transactional
  public void signOut(Long userId) {
    UserEntity userEntity = userRepository.findById(userId).get();

    UserEntity signOutUser = userEntity.toBuilder()
            .userAccount(userEntity.getUserAccount())
            .name(userEntity.getName())
            .userDepart(userEntity.getUserDepart())
            .userPhone(userEntity.getUserPhone())
            .approvedEventList(userEntity.getApprovedEventList())
            .password(userEntity.getPassword())
            .attendAppList(userEntity.getAttendAppList())
            .postedEventList(userEntity.getPostedEventList())
            .attendInfoList(userEntity.getAttendInfoList())
            .role(Role.ROLE_DELETE)
            .build();

    userRepository.save(signOutUser);
  }
}
