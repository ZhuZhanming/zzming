package top.zzming.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import top.zzming.service.LoginService;
/**
 * Spring security 的配置类
 */
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * 注入自定义的UserDetailsService
     * @see LoginService
     */
    @Autowired
    private UserDetailsService userService;

    /**
     * 数据库密码的加密bean
     */
    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userService).passwordEncoder(getPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/", "/**/register","/user/active/*","/notes/**","/try/**","/image/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .loginProcessingUrl("/user/login")
                .loginPage("/login")
                .failureUrl("/login?error")
                .defaultSuccessUrl("/home")
                .and()
                .logout().logoutUrl("/logout").permitAll();
    }

}
