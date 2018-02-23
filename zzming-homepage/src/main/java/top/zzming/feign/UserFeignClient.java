package top.zzming.feign;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;
import top.zzming.model.User;

@FeignClient(name = "zzming-user")
public interface UserFeignClient {
    @GetMapping("/user/{username}")
    User findByUsername(@PathVariable("username") String username);

    @PostMapping(value = "/user/register")
    User registerUser(@RequestBody User user);

    @GetMapping(value = "/user/active/{activeCode}")
    int active(@PathVariable("activeCode") String activeCode);

}
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