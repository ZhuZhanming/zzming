package top.zzming.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Web的配置类
 */
@Configuration
public class WebMvnConfig extends WebMvcConfigurerAdapter {

    /**
     * 快速配置视图跳转
     * @param registry
     */
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/try").setViewName("try/try");
        registry.addViewController("/home").setViewName("user/home");
        registry.addViewController("/login").setViewName("user/login");
        registry.addViewController("/register").setViewName("user/register");
        registry.addViewController("/notes").setViewName("/note/notes");
        registry.addViewController("/").setViewName("index");
    }
}
