package tdd.duo.web;

import org.junit.Before;
import org.junit.Test;
import org.springframework.test.web.servlet.MockMvc;
import tdd.duo.config.WebConfig;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

/**
 * Created by yoon on 15. 3. 31..
 */

public class HomeControllerTest {

    MockMvc mockMvc;

    @Before
    public void setUp() {
        mockMvc = MvcTestUtil.getMockMvc(new HomeController());
    }

    @Test
    public void home() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andExpect(forwardedUrl(WebConfig.RESOLVER_PREFIX + "home" + WebConfig.RESOLVER_SUFFIX));
    }


}
