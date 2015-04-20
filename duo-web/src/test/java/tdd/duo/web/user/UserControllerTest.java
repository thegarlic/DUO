package tdd.duo.web.user;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import tdd.duo.config.AppConfig;
import tdd.duo.config.WebConfig;
import tdd.duo.domain.User;
import tdd.duo.repository.UserRepository;
import tdd.duo.web.MvcTestUtil;
import tdd.duo.web.user.UserController;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(MockitoJUnitRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class UserControllerTest {

    MockMvc mockMvc;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    UserController userController;

    @Before
    public void setUp() {
        mockMvc = MvcTestUtil.getMockMvc(userController);
    }

    @Test
    public void userRegisterForm() throws Exception {
        mockMvc.perform(get("/user/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("/user/register"))
                .andExpect(forwardedUrl(WebConfig.RESOLVER_PREFIX + "/user/register" + WebConfig.RESOLVER_SUFFIX));
    }

    @Test
    public void userRegister() throws Exception {
        mockMvc.perform(post("/user/register")
                .param("email", "test@gmail.com")
                .param("name", "김우승")
                .param("age", "31"))

                .andExpect(status().isFound())
                .andExpect(redirectedUrl("/user/home"));
    }

    @Test
    public void userRegisterWithAlreadyExistion() throws Exception {

        String testEmail = "test@gmail.com";
        String testPassword = "asdf";
        String testName = "김우승";
        int testAge = 31;

        User testUser = new User(testEmail, testPassword, testName, testAge);

        when(userRepository.findByEmail(testEmail)).thenReturn(testUser);


        mockMvc.perform(post("/user/register")
                .param("email", testEmail)
                .param("name", testName)
                .param("age", ""+testAge))

                .andExpect(status().isOk())
                .andExpect(forwardedUrl(WebConfig.RESOLVER_PREFIX + "/user/register" + WebConfig.RESOLVER_SUFFIX))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    public void userRegisterWithInvalidParameter() throws Exception{
        String testEmail = "";
        String testPassword = "asdf";
        String testName = "김우승";
        int testAge = 31;

        User testUser = new User(testEmail, testPassword, testName, testAge);

        when(userRepository.findByEmail(testEmail)).thenReturn(testUser);


        mockMvc.perform(post("/user/register")
                .param("email", testEmail)
                .param("name", testName)
                .param("age", ""+testAge))

                .andExpect(status().isOk())
                .andExpect(forwardedUrl(WebConfig.RESOLVER_PREFIX + "/user/register" + WebConfig.RESOLVER_SUFFIX))
                .andExpect(model().size(2))
                .andExpect(model().attributeExists("error"));
    }

}