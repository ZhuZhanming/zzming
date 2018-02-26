package top.zzming.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.zzming.model.User;
import top.zzming.service.UserService;

/**
 * 控制层
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/{username}")
    public User get(@PathVariable("username") String username){
        return userService.findByUsername(username);
    }
    @PostMapping("/register")
    public User register(@RequestBody User user){
        userService.register(user);
        return user;
    }
    @GetMapping("/active/{activeCode}")
    public int activeHandle(@PathVariable("activeCode") String activeCode){
        return   userService.active(activeCode);
    }
}
