package top.zzming.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import top.zzming.model.User;

/**
 * 访问zzming-user微服务
 */
@FeignClient(name = "zzming-user")
public interface UserFeignClient {

    /**
     * 通过账号查找
     */
    @GetMapping("/user/{username}")
    User findByUsername(@PathVariable("username") String username);

    /**
     * 注册
     * @param user 要注册的信息
     */
    @PostMapping(value = "/user/register")
    User registerUser(@RequestBody User user);

    /**
     * 通过激活码激活数据库的用户
     */
    @GetMapping(value = "/user/active/{activeCode}")
    int active(@PathVariable("activeCode") String activeCode);

}

/**
 * feign错误回滚
 */
@Component
class UserFeignClientFallback implements UserFeignClient{
    @Override
    public User findByUsername(String username) {
        User user = new User();
        user.setUserId(-1);
        return user;
    }

    @Override
    public User registerUser(User user) {
        user.setUserId(-1);
        return user;
    }

    @Override
    public int active(String activeCode) {
        return 0;
    }
}