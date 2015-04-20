package tdd.duo.web;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import tdd.duo.config.WebConfig;

/**
 * Created by yoon on 15. 4. 6..
 */
public class MvcTestUtil {

    public static MockMvc getMockMvc(Object controller) {
        WebConfig webConfig = new WebConfig();
        return MockMvcBuilders.standaloneSetup(controller).setViewResolvers(webConfig.internalResourceViewResolver()).addFilter(new HiddenHttpMethodFilter()).build();
    }

    public static MockMvc getInterceptorMockMvc(Object controller) {
        WebConfig webConfig = new WebConfig();
        return MockMvcBuilders.standaloneSetup(controller)
                .setViewResolvers(webConfig.internalResourceViewResolver())
                .addInterceptors(webConfig.loginInterceptor()).build();
    }
}
