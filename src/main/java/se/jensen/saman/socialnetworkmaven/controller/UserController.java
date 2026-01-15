package se.jensen.saman.socialnetworkmaven.controller;


import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import se.jensen.saman.socialnetworkmaven.dto.*;
import se.jensen.saman.socialnetworkmaven.service.UserService;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final static Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;

    }


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers() {
        List<UserResponseDTO> responseDTOList = userService.getAllUsers();

        return ResponseEntity.ok(responseDTOList);
    }


    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> findUserById(@PathVariable Long id) {
        UserResponseDTO responseDTO = userService.findUserById(id);

        return ResponseEntity.ok(responseDTO);

    }


    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO reqDTO) {
        logger.info("Trying to create a user with username: {}", reqDTO.username());
        UserResponseDTO respDTO = userService.createUser(reqDTO);
        logger.info("Successfully created a user with username and Id: {}: {}", respDTO.username(), respDTO.id());

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(respDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(@PathVariable Long id, @AuthenticationPrincipal String username, @Valid @RequestBody UserRequestDTO reqDTO) {
        logger.info("Trying to update a user with username: {}", reqDTO.username());
        UserResponseDTO userResponseDTO = userService.updateUser(id, username, reqDTO);
        logger.info("Successfully updated  user with username: {}", reqDTO.username());

        return ResponseEntity.ok(userResponseDTO);

    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id, @AuthenticationPrincipal String username) {
        logger.warn("Trying to delete user with Id: {}", id);
        userService.deleteById(id, username);
        logger.info("Successfully deleted user with Id: {}", id);

        return ResponseEntity.noContent().build();
    }


    @PostMapping("/login")
    public ResponseEntity<UserLoginResponseDTO> loginUser(@Valid @RequestBody UserLoginRequestDTO loginReqDTO) {
        UserLoginResponseDTO loginRespDTO = userService.loginUser(loginReqDTO);

        return ResponseEntity.ok(loginRespDTO);
    }


    @PutMapping("/{id}/profile")
    public ResponseEntity<UserResponseChangeProfileDTO> changeProfileUpdate(@PathVariable Long id, @AuthenticationPrincipal String username, @Valid @RequestBody UserRequestChangeProfileDTO reqDTO) {
        UserResponseChangeProfileDTO respDTO = userService.changeProfileUpdate(id, username, reqDTO);
        return ResponseEntity.ok(respDTO);
    }

    @PutMapping("/{id}/profile/editpassword")
    public ResponseEntity<UserResponseDTO> changePasswordUpdate(@PathVariable Long id, @AuthenticationPrincipal String username, @Valid @RequestBody UserRequestPasswordChangeDTO reqDTO) {
        UserResponseDTO respDTO = userService.changePasswordUpdate(id, username, reqDTO);
        return ResponseEntity.ok(respDTO);
    }

    @PostMapping("/{id}/profile/follow")
    public ResponseEntity<Void> followUser(@PathVariable Long id, @AuthenticationPrincipal String username, @RequestBody UserRequestFollowDTO reqDTO) {
        userService.followUser(username, reqDTO);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/profile/unfollow")
    public ResponseEntity<Void> unFollowUser(@PathVariable Long id, @AuthenticationPrincipal String username, @RequestBody UserRequestFollowDTO reqDTO) {
        userService.unFollowUser(username, reqDTO);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}/profile/followers")
    public ResponseEntity<UserResponseWithFollowersDTO> getFollowers(@PathVariable Long id){
      UserResponseWithFollowersDTO respDTO = userService.getFollowers((id));
        return ResponseEntity.ok(respDTO);
    }

    @GetMapping("/{id}/profile/following")
    public ResponseEntity<UserResponseWithFollowingDTO> getFollowing(@PathVariable Long id){
        UserResponseWithFollowingDTO respDTO = userService.getFollowing((id));
        return ResponseEntity.ok(respDTO);
    }
}



