package se.jensen.saman.socialnetworkmaven.controller;

import jakarta.validation.Valid;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.bind.annotation.*;
import se.jensen.saman.socialnetworkmaven.dto.*;
import se.jensen.saman.socialnetworkmaven.model.Post;
import se.jensen.saman.socialnetworkmaven.model.User;
import se.jensen.saman.socialnetworkmaven.repository.PostRepository;
import se.jensen.saman.socialnetworkmaven.repository.UserRepository;
import se.jensen.saman.socialnetworkmaven.service.PostService;
import se.jensen.saman.socialnetworkmaven.service.UserService;
import se.jensen.saman.socialnetworkmaven.util.JwtUtil;
import tools.jackson.databind.ObjectMapper;


import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;

    @Autowired
    private UserService userService;


    @Autowired
    private PostRepository postRepository;


    @BeforeEach
    public void setUp() {
        userRepository.deleteAll();
        User user = new User();
        user.setRole("ADMIN");
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("Password1"));
        user.setEmail("robinsson@gmail.com");
        user.setBio("Hej och välkommmen till testet!");
        user.setDisplayName("test_display_name");
        user.setProfileImagePath("random_path_profile_pic");
        User savedUser = userRepository.save(user);

       this.token = jwtUtil.generateToken(savedUser);


    }

    @Test
    void shouldGetAllUsers() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
    //happypath

    @Test
    void shouldGetUserById() throws Exception {
        User storedUser = userRepository.findUserByUsername("admin").get();
        Long userId = storedUser.getId();
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.displayName").value("test_display_name"))
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.email").value("robinsson@gmail.com"));

    }


    @Test
    void shouldReturn404WhenUserNotFound() throws Exception {
        Long userId = 999L; //nonexistant
        mockMvc.perform(MockMvcRequestBuilders.get("/users/{id}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andDo(print())
                .andExpect(content().string("User not found with that Id"));
    }
    // happypath


    @Test
    void shouldCreateUser() throws Exception {

        UserRequestDTO requestDTO = new UserRequestDTO("TestUser",
                "Hejsan1",
                "kallekallesson@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value("kallekallesson@gmail.com"))
                .andExpect(jsonPath("$.username").value("TestUser"))
                .andExpect(jsonPath("$.id").exists());
    }

    /* public UserResponseDTO updateUser(Long id, String username, UserRequestDTO reqDTO) {

            User user = getUserAndVerifyUsername(id, username);

            userMapper.updateUserFromReqDTO(reqDTO, user);

            User savedUser = userRepository.save(user);

            return userMapper.fromUserToResponseDTO(savedUser);
        }*/
    @Test
    void shouldUpdateUser() throws Exception {
        User user = userService.getFindByUsernameMethod("admin");

        Long userId = user.getId();
        UserRequestDTO requestDTO = new UserRequestDTO("UpdatedAdminUser",
                "Password1ThatIsntUsed",
                "updatedEmail@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("UpdatedAdminUser"))
                .andExpect(jsonPath("$.email").value("updatedEmail@gmail.com"))
                .andExpect(jsonPath("$.id").exists())
                .andDo(print());
    }
    @Test
    void shouldUpdatePassword()throws Exception{
        User user = userRepository.getByUsername("admin");
        Long userId = user.getId();
        UserRequestPasswordChangeDTO reqDTO = new UserRequestPasswordChangeDTO("Password1", "newPassword1");

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/profile/editpassword", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer  " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDTO)))
                .andExpect(status().isOk())
                .andDo(print());

    }

    @Test
    void shouldNotUpdatePassword()throws Exception{
        User user = userRepository.getByUsername("admin");
        Long userId = user.getId();
        UserRequestPasswordChangeDTO reqDTO = new UserRequestPasswordChangeDTO("WrongPassword1", "newPassword1");

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/profile/editpassword", userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer  " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDTO)))
                .andExpect(status().isForbidden())
                .andDo(print());

    }


    @Test
    void shouldDeleteUser()throws Exception{
        User user = userRepository.getByUsername("admin");
        Long userId = user.getId();


        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent())
                .andDo(print());
    }

    @Test
    void shouldDenyDeleteOtherUser()throws Exception{
        User victim = new User();
        victim.setUsername("victimUser");
        victim.setEmail("victim@test.com");
        victim.setPassword(passwordEncoder.encode("Password1"));
        victim.setRole("USER");
        victim = userRepository.save(victim);

        mockMvc.perform(MockMvcRequestBuilders.delete("/users/{userId}", victim.getId())
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());
    }

    @Test
    void shouldGetUserAndPostsById()throws Exception{
        User user = userRepository.getByUsername("admin");
        Long userId = user.getId();

        Post post0 = new Post( "First post on index [0]", LocalDateTime.now(), user);
        Post post1 = new Post( "Second post on index [1]", LocalDateTime.now(), user);
        postRepository.save(post0);
        postRepository.save(post1);

        var result  = mockMvc.perform(MockMvcRequestBuilders.get("/users/{userId}/posts",userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer "+ token)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.posts").exists())
                .andReturn();

        String jsonResponse = result.getResponse().getContentAsString();

        Object jsonObject = objectMapper.readValue(jsonResponse, Object.class);

        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject));

    }

    @Test
    void shouldThrowExpiredTokenNotUpdatePassWord()throws Exception{
        User user = userRepository.getByUsername("admin");
        Long userId = user.getId();
        String expiredToken = jwtUtil.generateExpiredToken(user);
        UserRequestPasswordChangeDTO reqDTO = new UserRequestPasswordChangeDTO("Password1",
                "Password2");

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/profile/editpassword",userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + expiredToken)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reqDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldThrowInvalidTokenNotUpdatePassword()throws Exception{
        User user = userRepository.getByUsername("admin");
        Long userId = user.getId();
        String invalidToken = "invalidAf";
        UserRequestPasswordChangeDTO reqDTO = new UserRequestPasswordChangeDTO("Password1",
                "Password2");

        mockMvc.perform(MockMvcRequestBuilders.put("/users/{userId}/profile/editpassword",userId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reqDTO)))
                .andExpect(status().isForbidden());

    }
    @Test
   void shouldLoginUserSuccessful()throws Exception{
        User user = userRepository.getByUsername("admin");
        Thread.sleep(1000);

        UserLoginRequestDTO LoginReqDTO = new UserLoginRequestDTO(user.getUsername(), "Password1");
        mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(LoginReqDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists())
                .andExpect(jsonPath("$.username").value("admin"))
                .andDo(print());

    }

    @Test
    void shouldFailLoginUnsuccessful()throws Exception{


        UserLoginRequestDTO LoginReqDTO = new UserLoginRequestDTO("admin", "WRONG_PASSWORd1");
        mockMvc.perform(MockMvcRequestBuilders.post("/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(LoginReqDTO)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.token").doesNotExist())
                .andExpect(jsonPath("$.username").doesNotExist())
                .andDo(print());

    }
    @Test
    void shouldReturn400WhenCreatingInvalidUser() throws Exception {

        UserRequestDTO invalidUser = new UserRequestDTO("BadUser", "123", "not-an-email");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidUser)))
                .andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    void shouldFailToRegisterWithDuplicateUsername() throws Exception {

        UserRequestDTO duplicateNameUser = new UserRequestDTO("admin", "Password1", "newemail@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateNameUser)))
                .andExpect(status().isConflict())
                .andDo(print());
    }

    @Test
    void shouldFailToRegisterWithDuplicateEmail() throws Exception {

        UserRequestDTO duplicateEmailUser = new UserRequestDTO("NewUser", "Password1", "robinsson@gmail.com");

        mockMvc.perform(MockMvcRequestBuilders.post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(duplicateEmailUser)))
                .andExpect(status().isConflict())
                .andDo(print());
    }





//arrange act assert tripple-A, BDD Behaviour driven development.

}
