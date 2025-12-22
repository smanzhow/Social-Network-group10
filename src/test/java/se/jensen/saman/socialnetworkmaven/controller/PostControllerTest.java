package se.jensen.saman.socialnetworkmaven.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import se.jensen.saman.socialnetworkmaven.model.Post;
import se.jensen.saman.socialnetworkmaven.model.User;
import se.jensen.saman.socialnetworkmaven.repository.PostRepository;
import se.jensen.saman.socialnetworkmaven.repository.UserRepository;
import se.jensen.saman.socialnetworkmaven.util.JwtUtil;
import tools.jackson.databind.ObjectMapper;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private String token;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setup(){
        postRepository.deleteAll();
        userRepository.deleteAll();
        User user = new User();
        user.setRole("ADMIN");
        user.setUsername("admin");
        user.setPassword(passwordEncoder.encode("Password1"));
        user.setEmail("robinsson@gmail.com");
        user.setBio("Hej och välkommen till testet!");
        user.setDisplayName("test_display_name");
        user.setProfileImagePath("random_path_profile_pic");
        User savedUser = userRepository.save(user);

        this.token = jwtUtil.generateToken(savedUser);

        Post post1 = new Post( "First post :)", LocalDateTime.now(), user);
        Post post2 = new Post( "Second post ;)", LocalDateTime.now(), user);
        Post post3 = new Post( "Third post  :D", LocalDateTime.now(), user);
        Post post4 = new Post( "Fourth post :S", LocalDateTime.now(), user);
        postRepository.save(post1);
        postRepository.save(post2);
        postRepository.save(post3);
        postRepository.save(post4);

        User user1 = new User();
        user1.setRole("USER");
        user1.setUsername("admin1");
        user1.setPassword(passwordEncoder.encode("Password1"));
        user1.setEmail("admin1@gmail.com");
        user1.setBio("Hej och välkommen till testet!");
        user1.setDisplayName("test_display_name1");
        user1.setProfileImagePath("random_path_profile_pic1");
        User savedUser1 = userRepository.save(user1);

        this.token = jwtUtil.generateToken(savedUser1);

        Post post5 = new Post( "Fifth but first post :)", LocalDateTime.now(), user1);
        Post post6 = new Post( "Sixth but second post ;)", LocalDateTime.now(), user1);
        Post post7 = new Post( "Seventh but third post  :D", LocalDateTime.now(), user1);
        Post post8 = new Post( "Eighth but fourth post :S", LocalDateTime.now(), user1);
        postRepository.save(post5);
        postRepository.save(post6);
        postRepository.save(post7);
        postRepository.save(post8);

    }

    @Test
    void shouldGetAllPosts()throws Exception{

       var result =  mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andDo(print())
               .andReturn();

       String jsonResponse = result.getResponse().getContentAsString();
       Object jsonObject = objectMapper.readValue(jsonResponse, Object.class);
        System.out.println(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject));
    }

    @Test
    void shouldThrowTokenInvalid()throws Exception{

        String invalidToken = "invalidToken";

        var result =  mockMvc.perform(MockMvcRequestBuilders.get("/posts")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + invalidToken)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden())
                .andDo(print());

    }


}
