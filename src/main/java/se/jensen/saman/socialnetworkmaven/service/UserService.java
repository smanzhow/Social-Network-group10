package se.jensen.saman.socialnetworkmaven.service;

import org.springdoc.api.OpenApiResourceNotFoundException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import se.jensen.saman.socialnetworkmaven.dto.*;
import se.jensen.saman.socialnetworkmaven.exception.DuplicateEmailExceptionHandler;
import se.jensen.saman.socialnetworkmaven.exception.DuplicateUsernameExceptionHandler;
import se.jensen.saman.socialnetworkmaven.mapper.UserMapper;
import se.jensen.saman.socialnetworkmaven.model.User;
import se.jensen.saman.socialnetworkmaven.model.UserFollow;
import se.jensen.saman.socialnetworkmaven.repository.UserRepository;
import se.jensen.saman.socialnetworkmaven.util.JwtUtil;

import java.util.Comparator;
import java.util.List;


@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, UserMapper userMapper, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public UserResponseDTO createUser(UserRequestDTO reqDTO) {
        if (userRepository.existsByUsername(reqDTO.username())) {

            throw new DuplicateUsernameExceptionHandler(reqDTO.username());
        }
        if (userRepository.existsByEmail(reqDTO.email())) {
            throw new DuplicateEmailExceptionHandler("Email is already in use.");
        }
        User user = userMapper.fromReqDTOtoUser(reqDTO);
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        User savedUser = userRepository.save(user);
        return userMapper.fromUserToResponseDTO(savedUser);

    }

    public List<UserResponseDTO> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(userMapper::fromUserToResponseDTO)
                .toList();

    }

    public UserResponseDTO findUserById(Long id) {
        return userRepository.findById(id)
                .map(userMapper::fromUserToResponseDTO)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found with that Id"));

    }


    public UserResponseDTO updateUser(Long id, String username, UserRequestDTO reqDTO) {

        User user = getUserAndVerifyUsername(id, username);

        userMapper.updateUserFromReqDTO(reqDTO, user);

        User savedUser = userRepository.save(user);

        return userMapper.fromUserToResponseDTO(savedUser);
    }


    public void deleteById(Long id, String username) {
        getUserAndVerifyUsername(id, username);
        userRepository.deleteById(id);
    }


    public UserLoginResponseDTO loginUser(UserLoginRequestDTO loginReqDTO) {

        User user = userRepository.findUserByUsername(loginReqDTO.username())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));

        if (!passwordEncoder.matches(loginReqDTO.password(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password.");
        }
        String token = jwtUtil.generateToken(user);
        return userMapper.loginFromUserToResponseDTO(user, token);

    }

    public User getFindByUsernameMethod(String username) {

        return userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }


    private User getUserAndVerifyUsername(Long id, String username) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found."));

        if (!user.getUsername().equals(username)) {
            throw new AccessDeniedException("Access denied");
        }
        return user;

    }

    @Transactional(readOnly = true)
    public UserWithPostsResponseDTO getUserWithPosts(Long id) {
        return userRepository.findById(id).map(userMapper::fromUserToUserWithPostsDTO)
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found"));
    }

    public UserResponseChangeProfileDTO changeProfileUpdate(Long id, String username, UserRequestChangeProfileDTO reqDTO) {
        User user = getUserAndVerifyUsername(id, username);
        userMapper.changeProfileUpdate(reqDTO, user);

        User savedUser = userRepository.save(user);
        return userMapper.userToProfileResponseDTO(savedUser);

    }

    public UserResponseDTO changePasswordUpdate(Long id, String username, UserRequestPasswordChangeDTO reqDTO) {
        User user = getUserAndVerifyUsername(id, username);
        if (!passwordEncoder.matches(reqDTO.oldPassword(), user.getPassword())) {
            throw new AccessDeniedException("password doesn't match");
        }
        userMapper.changePasswordUpdate(reqDTO, user);
        String newPassword = user.getPassword();
        user.setPassword(passwordEncoder.encode(newPassword));
        User savedUser = userRepository.save(user);

        return userMapper.fromUserToResponseDTO(savedUser);

    }

    public void  followUser(String username, UserRequestFollowDTO reqDTO){


        User userThatPromptedFollow = getUserAndVerifyUsername(reqDTO.theOneThatWantsToFollowId(), username);

        User userThatIsBeingFollowed = userRepository.findById(reqDTO.theOneThatIsBeingFollowedId())
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found"));


        userThatPromptedFollow.follow(userThatIsBeingFollowed);

      userRepository.save(userThatPromptedFollow);
    }

    public void unFollowUser(String username, UserRequestFollowDTO reqDTO){

        User userThatPromptedUnfollow = getUserAndVerifyUsername(reqDTO.theOneThatWantsToFollowId(), username);
        User userThatIsBeingUnfollowed = userRepository.findById(reqDTO.theOneThatIsBeingFollowedId())
                .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found"));

        userThatPromptedUnfollow.unfollow(userThatIsBeingUnfollowed);

        userRepository.save(userThatPromptedUnfollow);
    }

    public UserResponseWithFollowersDTO getFollowers(Long id){
       User selectedUsersFollowerList = userRepository.findById(id)
               .orElseThrow(() -> new OpenApiResourceNotFoundException("User not found"));

        List<UserFollowersDTO> followerList = selectedUsersFollowerList.getFollowerList()
                .stream().sorted(Comparator.comparing(UserFollow::getCreatedAt).reversed())
                .map(f -> new UserFollowersDTO(f.getFollower().getUsername(), f.getId()))
                .toList();

       return new UserResponseWithFollowersDTO(selectedUsersFollowerList.getUsername(), followerList);
    }

    public UserResponseWithFollowingDTO getFollowing (Long id){
        User selectedUserFollowingList = userRepository.findById(id)
                .orElseThrow(()-> new OpenApiResourceNotFoundException("User not found"));

        List<UserFollowingDTO> followingDTOList = selectedUserFollowingList.getFollowingList()
                .stream().sorted(Comparator.comparing(UserFollow::getCreatedAt).reversed())
                .map(f -> new UserFollowingDTO(f.getFollowed().getUsername(),f.getFollowed().getId()))
                .toList();
        return new UserResponseWithFollowingDTO(selectedUserFollowingList.getUsername(), followingDTOList);
    }

    @Transactional(readOnly = true)
    public UserWithHabitsResponseDTO getUserWithHabits(Long id){
        return userRepository.findById(id)
                .map(userMapper::fromUserToUserWithHabitsDTO)
                .orElseThrow(()-> new OpenApiResourceNotFoundException("User not found"));


    }



}
